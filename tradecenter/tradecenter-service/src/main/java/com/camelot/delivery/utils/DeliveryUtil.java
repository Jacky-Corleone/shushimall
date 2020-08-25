package com.camelot.delivery.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.delivery.dto.ParametersDTO;
import com.camelot.delivery.dto.SendBodyDTO;
import com.camelot.openplatform.util.SysProperties;
/** 
 * <p>Description: [快递100工具类]</p>
 * Created on 2015年8月10日
 * @author  <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class DeliveryUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(DeliveryUtil.class);
	/**
	 * 发送物流信息，订阅快递100推送信息
	 * @param sendBody
	 * @return
	 */
	public static String sendDeliveryMessage(SendBodyDTO sendBody) {
		LOGGER.info("\n 方法[{}]，入参：[{}]", "DeliveryUtil-sendDeliveryMessage", JSONObject.toJSONString(sendBody));
		StringBuilder postResult = new StringBuilder();
		Map<String, String> paras = new HashMap<String, String>();
		String date = "";
		paras.put("schema", "json");
		ParametersDTO p = new ParametersDTO();
		sendBody.setCompany(sendBody.getCompany());
		sendBody.setFrom(sendBody.getFrom());
		sendBody.setKey(getProperties().get("key"));
		sendBody.setNumber(sendBody.getNumber());
		p.setCallbackurl(getProperties().get("callbackurl"));
		p.setResultv2("");// 令牌随机生成
		p.setSalt("");
		sendBody.setParameters(p);
		sendBody.setTo(sendBody.getTo());
		try {
			date = JSON.toJSONString(sendBody);
		} catch (Exception e) {
			LOGGER.error("订阅快递100接口调用失败,基数数据转换JSON出错{}", e);
			return "error";
		}
		paras.put("param", date);
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(getProperties().get("requesturl"));
		httpPost.addParameter("schema", "json");
		httpPost.addParameter("param", date);
		try {
			httpClient.executeMethod(httpPost);
			InputStream inputStream = httpPost.getResponseBodyAsStream();
			String readLine = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((readLine = reader.readLine()) != null) {
				postResult.append(readLine);
			}
		} catch (HttpException e) {
			LOGGER.error("订阅快递100接口调用失败{}", e);
		} catch (IOException e) {
			LOGGER.error("快递100读取返回信息失败{}", e);
		}
		LOGGER.info("\n 方法[{}]，出参：[{}]", "DeliveryUtil-sendDeliveryMessage", JSONObject.toJSONString(postResult));
		return postResult.toString();
	}
	
	public static Map<String, String> getProperties() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String key = SysProperties.getProperty("key");
			String callbackurl = SysProperties.getProperty("callbackurl");
			String requesturl = SysProperties.getProperty("requesturl");
			LOGGER.info("物流的key是" + key + "物流的url是" + requesturl + "物流回调地址" + callbackurl);
			map.put("key", key);
			map.put("requesturl", requesturl);
			map.put("callbackurl", callbackurl);
		} catch (Exception e) {
			LOGGER.error("读取属性文件错误所读路径：" + e.getMessage());
		}
		return map;
	}
}
