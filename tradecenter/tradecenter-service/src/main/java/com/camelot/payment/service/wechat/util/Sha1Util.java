package com.camelot.payment.service.wechat.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;


/*
'============================================================================
'api说明：
'createSHA1Sign创建签名SHA1
'getSha1()Sha1签名
'============================================================================
'*/
public class Sha1Util {

    //创建签名SHA1
	public static String createSHA1Sign(SortedMap<String, String> signParams) {
		StringBuffer sb = new StringBuffer();
		Set es = signParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if(null!=v&&!"".equals(v)){
				sb.append(k + "=" + v + "&");
				//要采用URLENCODER的原始值！
			}
		}
		String params = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("sha1 sb:" + params);
		return getSha1(params);
	}
	//Sha1签名
	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("GBK"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
	//V3的签名方式：MD5加密后大写
	public static String createV3SHA1Sign(SortedMap<String, String> signParams,String key) {
		StringBuffer sb = new StringBuffer();
		Set es = signParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if(null!=v&&!"".equals(v)){
				sb.append(k + "=" + v + "&");
				//要采用URLENCODER的原始值！
			}
		}
		String params = sb.append("key="+key).toString();
		System.out.println("sha1 sb:" + params);
		return MD5Util.MD5(params).toUpperCase();
	}
	
	public static void main(String[] args) {
		String s="appid=wxd930ea5d5a258f4f&auth_code=123456&body=test&device_info=123&mch_id=1 900000109&nonce_str=960f228109051b9969f76c82bde183ac&out_trade_no=1400755861&spbil l_create_ip=127.0.0.1&total_fee=1";
		String key="&key=8934e7d15453e97507ef794cf7b0519d";
				String params =s+"key="+key;
				System.out.println( MD5Util.MD5Encode(params, "UTF-8").toUpperCase());
	}
	
}
