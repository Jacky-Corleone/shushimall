package com.camelot.openplatform.common.sms;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <p>Description: [短信发送服务类]</p>
 * Created on 2015年1月28日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class SendSmsUtil {
	 private static Logger logger = LoggerFactory.getLogger(SendSmsUtil.class);
	
	/*
     * 短信接口地址定义：具体地址配置在 webservice.properties文件中
     */
    //private final static String smsAddress = "http://bys.bcactc.org/websms/sendsms.aspx";
	
	private final static String smsAddress = "http://218.204.222.12:9003/MWGate/wmgw.asmx/";
    
	public static String sendSingleSMS(String phone, String content) {
        return sendMultiSMS(Arrays.asList(phone), content);
    }
	
	/**
     * 发送短信
     * @param  content  短信内容
     * @param  phone    手机电话号码集合 ，最终处理成格式为[1326929XXXX;15812345679]
     * @return 发送状态：发送成功 返回{status:success,content:''}；发送失败 返回{status:false,content:'手机号码不存在'}
     */
    public static String sendMultiSMS(List<String> phones, String content) {
        if(null==phones||phones.isEmpty()){
            throw new RuntimeException("电话号码不能为空！");
        }
        StringBuilder sb = new StringBuilder();
        for(String tel:phones){
            sb.append(tel);
            sb.append(";");
        }
        String telss = sb.substring(0, sb.lastIndexOf(";"));
        if (StringUtils.isEmpty(smsAddress)) {
             logger.error("短信地址为空值，发送失败！");
             return "{\"false\", \"短信地址为空值，发送失败！}\"";
        }
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(smsAddress); 
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");//在头文件中设置转码
        NameValuePair[] data ={ 
    		new NameValuePair("testNum", telss), 
    		new NameValuePair("testCont", content),
    		new NameValuePair("sms_userId", "J02216"),
    		new NameValuePair("sms_password", "939102")
        };
        post.setRequestBody(data);
        int statusCode = 0;
        String result = null;
        try {
            client.executeMethod(post);
            statusCode = post.getStatusCode();
            // result = new String(post.getResponseBodyAsString().getBytes("gbk"));
            result = post.getResponseBodyAsString();
            post.releaseConnection();
        } catch (HttpException e) {
        	logger.error(e.getMessage());
            throw new RuntimeException("短信发送失败", e);
        } catch (IOException e) {
        	logger.error(e.getMessage());
            throw new RuntimeException("短信发送失败", e);
        }
        if(statusCode==200 && "成功".endsWith(result)){
           return "{\"success\", \"\"}";
        }
        return "{\"false\", \"短信地址为空值，发送失败！}\"";
    }
}
