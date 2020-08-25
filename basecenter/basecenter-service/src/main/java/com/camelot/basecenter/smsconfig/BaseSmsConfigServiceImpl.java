package com.camelot.basecenter.smsconfig;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.basecenter.dao.BaseSmsConfigDAO;
import com.camelot.basecenter.domain.BaseSmsConfig;
import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.email.SendMailUtil;
import com.camelot.openplatform.common.email.SendMailUtil.EmailType;
import com.camelot.openplatform.common.sms.SendSmsResult;
import com.camelot.openplatform.util.SysProperties;

/** 
 * <p>Description: [本类用于操作短信和邮箱的设置以及邮件、短信的发送功能]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("baseSmsConfigService")
public class BaseSmsConfigServiceImpl implements BaseSmsConfigService {
	private final static Logger logger = LoggerFactory.getLogger(BaseSmsConfigServiceImpl.class);
	
	@Resource
	private BaseSmsConfigDAO baseSmsConfigDAO; 
	
	/**
	 * <p>Discription:[消息基础配置查询]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[周乐]
	 */
	@Override
	public SmsConfigDTO getSmsConfig() {
		SmsConfigDTO dto = baseSmsConfigDAO.getSmsConfig();
		return dto;
	}
	
	/**
	 * <p>Discription:[绿印消息基础配置查询]</p>
	 * Created on 2015年9月8日
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public SmsConfigDTO getSmsConfigGreen() {
		SmsConfigDTO dto = baseSmsConfigDAO.getSmsConfigGreen();
		return dto;
	}
	
	
	

	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[创建者中文名字]
	 */
	@Override
	public ExecuteResult<String> modifySMSConfig(SmsConfigDTO smsConfigDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		BaseSmsConfig smsConfig = new BaseSmsConfig();
		try {
			
			//smsConfig = EntityTranslator.transToDomain(smsConfigDTO, BaseSmsConfig.class);

			BeanUtils.copyProperties(smsConfigDTO, smsConfig);
	
			if(smsConfigDTO.getId()==null){
				baseSmsConfigDAO.add(smsConfig);
			}else{
				baseSmsConfigDAO.update(smsConfig);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.addErrorMsg(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 获取状态报告
	 * @param iReqType 请求类型(1:上行 2: 状态报告)
	 * 
	 * 报告返回的格式：
	 * 信息类型(状态报告标志1),时间,平台消息编号,通道号,手机号,MongateSendSubmit时填写的MsgId（用户自编消息编号）,*,状态值(0:成功 非0:失败),详细错误原因
	 * 
	 * 报告的最后一个字段的含义
	 *  1. DELIVRD 接收成功
		2. M2:0045 黑名单
		3. M2：0043 30秒拦截
		4. M2：0042  梦网平台拦截
		5. MK、MI、MN 。。。移动号码空号停机
		6. R:00XXX  联通电信号码空号或停机
		7. M2:0044 空内容
		8. DB:0140 白名单
		9. REJECTD 号码网关错误，消息被拒
		10. IB:0008 归属限速
		11.ID：XXXX 运营商关键字限制
		12. R1:0006 号段不支持
		13. M2:0013 非法号码
		14. EXPIRED 短信下发12小时内关机或暂停服务
		15.UNDELIV  号码处于未知故障状态（不能确定是停机、暂停服务、空号、号码处于未使用状态等）
		16.DB:0141 ，DB:0144 移动号码在运营商侧某省区是黑名单（曾经电话或热线投诉过运营商，需要本机号码打运营商客服电话，解除黑名单）
		17.NP:1243 携号转网（很少见，运营商跨网保号的号码暂不支持接收跨网短信）
		18.MB:XXXX，MC:XXXX，MI:XXXX，MN:XXXX 都是移动内部错误，信息丢失【原因是该手机用户停机或长期关机、SIM卡不处于服务状态（SIM卡不在手机中）致使短信过期丢弃】
		19.ACCEPTD 停机
		20.M:BLACK  移动号码在运营商侧是黑名单
		21.IC:0055 短信下发时12小时内无法接通
		22.CB:0001 CB:0005（移动联通电信）空号
		23.IA:0051 短信下发时12小时内号码无信号，短信无法送达
		24.REJECTD 号码网关错误，消息被拒

	 */
	@Override
	public void mongateGetDeliver(int iReqType){
		
		SmsConfigDTO smsConfig = getSmsConfig();
        String msgHost = smsConfig.getMsgHost();
        String msgUrl = smsConfig.getMsgUrl();
        String userId = smsConfig.getMsgAccount();
        String password = smsConfig.getMsgPassword();
        
        HttpClient httpClient = new HttpClient();
        //设置连接超时时间,30秒超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        //获取response返回数据超时时间，50秒超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
        //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
        PostMethod methodPost = new PostMethod(msgHost+msgUrl+"MongateGetDeliver");
        methodPost.addParameter("userId", userId);
        methodPost.addParameter("password", password);
        methodPost.addParameter("iReqType",iReqType+"");
        try {
        	for(int i = 0;i<5;i++){
        		int status  = httpClient.executeMethod(methodPost);
                String result = methodPost.getResponseBodyAsString();
                logger.info("状态报告："+result);
        	}
        } catch (Exception e) {
            logger.error("信息发送失败", e);
        } finally{
            methodPost.releaseConnection();
        }
		
	} 
	/**
	 * 根据发送短信返回的信息，判断发送短信的状态，是否发送成功，或者失败的原因
	 * 
	 * @author 王东晓
	 * 
	 * @param result,如果返回空，则发送成功
	 * @return
	 * @throws DocumentException
	 */
	private String getSendSmsResultCode(String result) throws DocumentException{
		logger.info("原生态返回："+result);
		Document document = DocumentHelper.parseText(result); 
        Element root = (Element) document.getRootElement();
        String resultCode = root.getText();
		return resultCode;
	}
	
	
}