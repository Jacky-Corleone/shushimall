package com.camelot.openplatform.common.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 秘钥生成器
 * @author Administrator
 *
 */
public class KeygenGenerator {
    
    
	/**
	 * 将固定字符串转换为ASC加密的秘钥
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
    public static SecretKeySpec getASCKey(String password) throws NoSuchAlgorithmException, NoSuchPaddingException{
	    SecretKeySpec key2 = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
    	KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	    kgen.init(128,random);  
	    SecretKey secretKey = kgen.generateKey();  
	    byte[] enCodeFormat = secretKey.getEncoded();  
	    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	    return key;
    }
    
    
   
}
