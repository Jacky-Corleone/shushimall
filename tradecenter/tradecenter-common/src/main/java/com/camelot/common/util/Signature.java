package com.camelot.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class Signature{
	
	/**
	 * 签名：创建md5摘要，	加密。
	 * 
	 * @return sign - 签名结果
	 */
	public  static String createSign(Map<String, String> params,String keyPri) {
		String sortStr=sortStr(params, keyPri);
		return Base64.encode(DigestUtils.md5Hex(sortStr));
	}
	
	/**
	 * 验证：签名Base64解密与MD5字符串校验
	 */
	public static boolean verifySign(String decSign, Map<String, String> params,String keyPri) {
		if(StringUtils.isNotBlank(decSign)){
			decSign=Base64.decode(decSign);
			String verifyStr=DigestUtils.md5Hex(sortStr(params, keyPri));
			if (decSign!=null&&decSign.equals(verifyStr)) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * 按参数名称a-z排序,遇到空值/sign的参数不参加签名。
	 */
	private  static String sortStr(Map<String, String> params,String keyPri) {
		StringBuffer sb = new StringBuffer();
		if(params!=null){
			List<String> keys = new ArrayList<String>(params.keySet());
	        Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				String value = params.get(key);
				if (null != value && !"".equals(value) && !"sign".equals(key)) {
					sb.append(key + "=" + value + "&");
				}
			}
		}
		sb.append("key=" + keyPri);
		return  sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
	    String keyPri="123456";
		Map<String, String> params =new HashMap<String, String>();
		params.put("payBank", "8");
		params.put("payBank1", "18");
		System.out.println(DigestUtils.md5Hex("string1&key=8934e7d15453e97507ef794cf7b0519d"));
		String encSign =createSign(params, keyPri);
//		System.out.println(Base64.encode(DigestUtils.md5Hex("buyer=123456&orderNo=wxd930ea5d5a258f4f&totalFee=1.02&key=8934e7d15453e97507ef794cf7b0519d")));
		System.out.println(verifySign(encSign,params, keyPri));
	}

}
