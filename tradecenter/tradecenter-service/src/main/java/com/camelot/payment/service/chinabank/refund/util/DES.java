package com.camelot.payment.service.chinabank.refund.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * DES通用类
 * 
 * @author gaozhenhai
 * @since 2013.01.15
 * @version 1.0.0_1
 * 
 */
public class DES {
    /**
	 * <ul>
	 * <li>1、开发日期：2014-4-17</li>
	 * <li>2、开发时间：上午10:33:39</li>
	 * <li>3、作          者：wangliang</li>
	 * <li>4、返回类型：SecretKey</li>
	 * <li>5、方法含义：</li>
	 * <li>6、方法说明：</li>
	 * </ul>
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static SecretKey getKey(String key) throws Exception{
		
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    return keyFactory.generateSecret(new DESKeySpec(BASE64.decode(key)));
	}
	/**
	 * 加密方法
	 * @param text 明文
	 * @param key 密钥 BASE64
	 * @param charset 字符集
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String text, String key, String charset) throws Exception {
		
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
		byte[] textBytes = text.getBytes(charset);
		byte[] bytes = cipher.doFinal(textBytes);
		
		String encryptBase64EncodeString = BASE64.encode(bytes);
		return StringUtils.trimToEmpty(encryptBase64EncodeString);
	}
	
	/**
	 * 解密方法
	 * 
	 * @param text 密文
	 * @param key 密钥 BASE64
	 * @param charset 字符集
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String text, String key, String charset) throws Exception {
		
	    Cipher cipher = Cipher.getInstance("DES");
	    cipher.init(Cipher.DECRYPT_MODE, getKey(key));
	    byte[] textBytes = BASE64.decode(text);
	    byte[] bytes = cipher.doFinal(textBytes);
	    
	    String decryptString = new String(bytes, charset);
		return StringUtils.trimToEmpty(decryptString);
	}
}