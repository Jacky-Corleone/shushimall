package com.camelot.payment.service.chinabank.refund.util;

import com.camelot.common.constants.SysConstants;
import com.camelot.openplatform.util.SysProperties;



/**
 * 1、商户首先用OpenSSL工具产生密钥对，将产生的公钥告知网银；
 * 2、网银将自己的公钥告诉商户；
 * 3、商户向网银发送消息时，用网银的公钥加密后发送；
 * 4、商户接收网银发送的消息时，用商户的私钥解密；
 * @author wyshenzhongwei@chinabank.com.cn
 *
 */
public class RSAUtil {
	/**
	 * 网银的公钥
	 */
	private final static String wyPK = SysProperties.getProperty(SysConstants.CB_RSA_PUB_KEY);
	
	/**
	 * 商户的私钥
	 */
	private final static String merchantSK = SysProperties.getProperty(SysConstants.CB_RSA_PRI_KEY);
	private final static String CHAR_SET = "UTF-8";
	
	public static String encrypt(String data) throws Exception{
		String smi = BASE64.encode(RSA.encryptByPublicKey(data.getBytes(CHAR_SET), wyPK));
		return smi;
	}
	
	public static String decrypt(String data) throws Exception{
		String sming = new String(RSA.decryptByPrivateKey(BASE64.decode(data), merchantSK));
		return sming;
	}
	
	public static String sign(String data) throws Exception{
		return BASE64.encode(RSA.sign(data.getBytes(CHAR_SET), merchantSK));
	}
}
