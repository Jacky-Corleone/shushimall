package com.camelot.mall.service.impl;

import java.util.Random;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.mall.service.MessageService;
import com.camelot.mall.util.emay.SingletonClient;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.email.SendMailUtil;
import com.camelot.openplatform.common.sms.SendSmsResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;

/**
 * Created by Administrator on 2015/6/12.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Resource
    private RedisDB redisDB;

    @Resource
    private BaseSmsConfigService baseSmsConfigService;

    @Override
    public ExecuteResult<String> sendVerificationCode(String enumType, String key ,String[] verificationCodes, int verificationCodeType) {
    	logger.info("\n 方法[{}]，入参：[{}][{}][{}][{}]", "MessageServiceImpl-sendVerificationCode", enumType, key, JSON.toJSONString(verificationCodes), verificationCodeType);
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        for (String verificationCode : verificationCodes) {
            //生成验证码规则
            Random rd = new Random();
            String n="";
            int getNum;
            do {
                getNum = Math.abs(rd.nextInt())%10 + 48;//产生数字0-9的随机数
                //getNum = Math.abs(rd.nextInt())%26 + 97;//产生字母a-z的随机数
                char num1 = (char)getNum;
                String dn = Character.toString(num1);
                n += dn;
            } while (n.length()<6);
            if(verificationCodeType == 1){// 1  是给邮箱发送验证码
                //把验证码存进redis，并设置有效时间
                redisDB.setAndExpire(key, n, 1000);//暂时先用11代替key，等确认之后在修改
                EmailTypeEnum emailEnum = EmailTypeEnum.valueOf(enumType);
                executeResult = this.sendEmailByType(emailEnum, verificationCode, "舒适100邮箱验证（系统邮件，请勿回复）", n);
            }else{// 2  是给短信发送 验证码
                //把验证码存进redis，并设置有效时间
                redisDB.setAndExpire(key, n, 1000);
                SmsTypeEnum smsEnum = SmsTypeEnum.valueOf(enumType);
                executeResult =	this.sendMessage(smsEnum, verificationCode, n);
            }
        }
        return executeResult;
    }

    @Override
    public void saveGeneralUserVerificationCode(String key ,String generalUserVerificationCode) {
        //把验证码存进redis，并设置有效时间
        redisDB.setAndExpire(key , generalUserVerificationCode, 1000);//暂时先用11代替key，等确认之后在修改
    }

    /**
     * <p>Discription:[短信发送]</p>
     * Created on 2015年1月26日
     * @param smsType 短信类型：注册的、登录的、修改密码绑定的等等
     * @param telephone
     * @param content
     * @return
     * @author:[周乐]
     */

    @Override
    public ExecuteResult<String> sendMessage(SmsTypeEnum smsType, String telephone, String content) {
        //获取短信模版
        String smsTemp = SysProperties.getProperty(smsType.getCode());
        //将短信模版中的参数替换为具体的短信验证码
        smsTemp = smsTemp.replace("{validcode}", content);

        return sendSms(telephone, smsTemp);
    }

    /**
     * <p>Discription:[邮件发送]</p>
     * Created on 2015年1月26日
     * @param account 接收邮件的邮箱账号
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return
     * @author:[周乐]
     */

    @Override
    public ExecuteResult<String> sendEmail(String account, String subject, String content) {
        SmsConfigDTO dto = baseSmsConfigService.getSmsConfig();
        SendMailUtil sender = new SendMailUtil();
        sender.setSender(dto.getSendName());
        sender.setSendAddress(dto.getSendAddress());
        sender.setEmailType(SendMailUtil.EmailType.SMTP);
        sender.setSendServer(dto.getSendServer());
        sender.setUsername(dto.getLoginEmail());
        sender.setPassword(dto.getLoginPassword());
        //TODO 暂时不发送邮件
        //boolean b = true;
        boolean b = sender.send(account, subject, content, "text/html");
        ExecuteResult<String> result = new ExecuteResult<String>();
        if(!b){
            result.addErrorMessage("发送失败");
        }
        return result;
    }

    /*@Override
    public ExecuteResult<String> sendSms(String telephone, String content) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        SmsConfigDTO smsConfig = baseSmsConfigService.getSmsConfig();
        String msgHost = smsConfig.getMsgHost();
        String msgUrl = smsConfig.getMsgUrl();
        String userId = smsConfig.getMsgAccount();
        String password = smsConfig.getMsgPassword();

        byte[] encodeBase64 = new byte[0];
        try {
            encodeBase64 = Base64.encodeBase64(content.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            logger.error("转码字符集失败", e);
        }
        content = new String(encodeBase64);

        HttpClient httpClient = new HttpClient();
        //设置连接超时时间,30秒超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        //获取response返回数据超时时间，50秒超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
        //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
        PostMethod methodPost = new PostMethod(msgHost+msgUrl+"MongateMULTIXSend");
        methodPost.addParameter("userId", userId);
        methodPost.addParameter("password", password);
        //不需扩展、不需流水号
        methodPost.addParameter("multixmt", "0|*|"+telephone+"|"+content);
        try {
            //int status  = 200;

            //TODO 暂时不发送短信 start
            int status  = httpClient.executeMethod(methodPost);
            if(status ==200){
                String result = methodPost.getResponseBodyAsString();
                String resultCode = this.getSendSmsResultCode(result);
                String resultMessage = SendSmsResult.resultMap.get(resultCode);
                //如果返回的为空，则发送成功
                if(StringUtils.isBlank(resultMessage)){
                    logger.info("信息发送成功,状态码："+resultCode);
                }else{
                    executeResult.addErrorMessage("消息发送失败,状态码："+resultCode+";失败原因："+resultMessage);
                    logger.info("消息发送失败,状态码："+resultCode+";失败原因："+resultMessage);
                }
                //TODO 暂时不发送短信 end
            }else{
                executeResult.addErrorMessage("发送失败,http请求失败");
            }
        } catch (Exception e) {
            logger.error("信息发送失败", e);
        } finally{
            methodPost.releaseConnection();
        }
        return executeResult;
    }*/
    
	public ExecuteResult<String> sendSms(String telephone, String content) {
		logger.info("\n 方法[{}]，入参：[{}][{}]", "MessageServiceImpl-sendSms", telephone, content);
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		/* 开发期间不发送短信，因为短信条数有限*/
		try {
			SmsConfigDTO smsConfig = baseSmsConfigService.getSmsConfig();
			String softwareSerialNo = smsConfig.getMsgAccount();
			String key = smsConfig.getMsgPassword();

			int resultCode = SingletonClient.getClient(softwareSerialNo, key).sendSMS(new String[] { telephone },
					content + "回复TD退订", "", 5);
			String resultMessage = SendSmsResult.resultMapShushi100.get(String.valueOf(resultCode));
			if (resultCode == 0) {
				logger.info("信息发送成功,状态码：" + resultCode);
			} else {
				executeResult.addErrorMessage("消息发送失败,状态码：" + resultCode + ";失败原因：" + resultMessage);
				logger.info("消息发送失败,状态码：" + resultCode + ";失败原因：" + resultMessage);
			}
		} catch (Exception e) {
			logger.error("信息发送失败", e);
		}
		
		return executeResult;
	}
    
    /**
     * <p>Discription:[根据模版，邮件发送]</p>
     * Created on 2015年5月15日
     * @param emailType 使用的邮件模版
     * @param account 接收邮件的邮箱账号
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return
     * @author:[王东晓]
     */
    @Override
    public ExecuteResult<String> sendEmailByType(EmailTypeEnum emailType, String account, String subject, String content) {
        SmsConfigDTO dto = baseSmsConfigService.getSmsConfig();
        SendMailUtil sender = new SendMailUtil();
        sender.setSender(dto.getSendName());
        sender.setSendAddress(dto.getSendAddress());
        sender.setEmailType(SendMailUtil.EmailType.SMTP);
        sender.setSendServer(dto.getSendServer());
        sender.setUsername(dto.getLoginEmail());
        sender.setPassword(dto.getLoginPassword());
        //获取邮箱模版信息
        String emailTemp = SysProperties.getProperty(emailType.getCode());
        //如果模版信息为空,直接发送主题，防止验证码泄漏
        if(StringUtils.isEmpty(emailTemp)){
            emailTemp = subject;
        }
        //将邮箱模版中的变量替换为邮箱内容
        emailTemp = emailTemp.replace("{validcode}", content);
        //TODO 暂时不发送邮件
        //boolean b = true;
        boolean b = sender.send(account, subject, emailTemp, "text/html");
        ExecuteResult<String> result = new ExecuteResult<String>();
        if(!b){
        	logger.error("给用户"+account+"发送邮件失败");
            result.addErrorMessage("发送失败");
            return result;
        }
        logger.info("给用户"+account+"发送邮件成功");
        return result;
    }


    /**
     * 根据发送短信返回的信息，判断发送短信的状态，是否发送成功，或者失败的原因
     *
     * @author 王东晓
     *
     * @param result,如果返回空，则发送成功
     * @return
     * @throws org.dom4j.DocumentException
     */
    private String getSendSmsResultCode(String result) throws DocumentException {
        logger.info("原生态返回："+result);
        Document document = DocumentHelper.parseText(result);
        Element root = (Element) document.getRootElement();
        String resultCode = root.getText();
        return resultCode;
    }

    /**
     * 测试短信发送代码
     * @param args
     */
    public static void main(String[] args){
    	try {
			int i = SingletonClient.getClient("0SDK-EAA-6688-JEURQ","9d844fd1abf3b4a04fb39fc5e6075dae").sendSMS(new String[] { "13501133340" }, "【舒适100】尊敬的用户,您的手机验证码是8541,网上学知识看工程,价格公正透明轻松选购,回复TD退订", "",5);// 带扩展码
			System.out.println("testSendSMS=====" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*
        //接收手机号码
        String telephone = "18501252392";
        //接收的短信内容
        String content = "尊敬的用户，由于您未在7日内处理订单：11111的退款/退货申请，系统已默认同意买家的退款/退货申请，如有疑问请联系舒适100。";
        MessageServiceImpl messageServiceImpl = new MessageServiceImpl();

        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        String msgHost = "http://218.204.222.12:9003";//短信地址
        String msgUrl = "/MWGate/wmgw.asmx/";//发送接口
        String userId = "J02216";//账号
        String password = "127862";//密码

        byte[] encodeBase64 = new byte[0];
        try {
            encodeBase64 = Base64.encodeBase64(content.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            logger.error("转码字符集失败", e);
        }
        content = new String(encodeBase64);

        HttpClient httpClient = new HttpClient();
        //设置连接超时时间,30秒超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        //获取response返回数据超时时间，50秒超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
        //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
        PostMethod methodPost = new PostMethod(msgHost+msgUrl+"MongateMULTIXSend");
        methodPost.addParameter("userId", userId);
        methodPost.addParameter("password", password);
        //不需扩展、不需流水号
        methodPost.addParameter("multixmt", "0|*|"+telephone+"|"+content);
        try {
            int status  = httpClient.executeMethod(methodPost);
            if(status ==200){
                String result = methodPost.getResponseBodyAsString();
                String resultCode = messageServiceImpl.getSendSmsResultCode(result);
                String resultMessage = SendSmsResult.resultMap.get(resultCode);
                //如果返回的为空，则发送成功
                if(StringUtils.isBlank(resultMessage)){
                    logger.info("信息发送成功,状态码："+resultCode);
                }else{
                    executeResult.addErrorMessage("消息发送失败,状态码："+resultCode+";失败原因："+resultMessage);
                    logger.info("消息发送失败,状态码："+resultCode+";失败原因："+resultMessage);
                }
            }else{
                executeResult.addErrorMessage("发送失败,http请求失败");
            }
        } catch (Exception e) {
            logger.error("信息发送失败", e);
        } finally{
            methodPost.releaseConnection();
        }
        */
    }
}
