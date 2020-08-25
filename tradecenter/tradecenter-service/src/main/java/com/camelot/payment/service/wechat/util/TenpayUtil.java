package com.camelot.payment.service.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.camelot.common.constants.SysConstants;
import com.camelot.openplatform.util.SysProperties;



public class TenpayUtil {
	
	/**
	 * 封装商户信息
	 */ 
	public static Map<String, String> packInfo() {
		Map<String, String> params =new HashMap<String, String>();
		params.put("partnerId", SysProperties.getProperty(SysConstants.TENPAY_PARTNER_ID));
		params.put("partnerKey", SysProperties.getProperty(SysConstants.TENPAY_PARTNER_KEY));
		return params;
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 获取编码字符集
	 * @return String
	 */
	public static String getCharacterEncoding() {
		return SysProperties.getProperty(SysConstants.TENPAY_INPUT_CHARSET);
	}
	
}
