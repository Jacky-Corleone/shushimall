package com.camelot.payment.service.chinabank.refund.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


public class RSA {

	public static byte[] sign(byte data[], String privateKey) throws Exception {
		byte keyBytes[] = BASE64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		java.security.PrivateKey priKey = keyFactory
				.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(priKey);
		signature.update(data);
		return signature.sign();
	}

	public static boolean verify(byte data[], String publicKey, byte signByte[])
			throws Exception {
		byte keyBytes[] = BASE64.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		java.security.PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(pubKey);
		signature.update(data);
		return signature.verify(signByte);
	}

	public static byte[] decryptByPrivateKey(byte data[], String key)
			throws Exception {
		byte keyBytes[] = BASE64.decode(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(2, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte data[], String key)
			throws Exception {
		byte keyBytes[] = BASE64.decode(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(2, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte data[], String key)
			throws Exception {
		byte keyBytes[] = BASE64.decode(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(1, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPrivateKey(byte data[], String key)
			throws Exception {
		byte keyBytes[] = BASE64.decode(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(1, privateKey);
		return cipher.doFinal(data);
	}

	@SuppressWarnings("rawtypes")
	public static String getPrivateKey(Map keyMap) throws Exception {
		Key key = (Key) keyMap.get("RSAPrivateKey");
		byte base64bts[] = BASE64.encode(key.getEncoded())
				.getBytes("UTF-8");
		return new String(base64bts, "UTF-8");
	}

	@SuppressWarnings("rawtypes")
	public static String getPublicKey(Map keyMap) throws Exception {
		Key key = (Key) keyMap.get("RSAPublicKey");
		byte base64bts[] = BASE64.encode(key.getEncoded())
				.getBytes("UTF-8");
		return new String(base64bts, "UTF-8");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map keyMap = new HashMap(2);
		keyMap.put("RSAPublicKey", publicKey);
		keyMap.put("RSAPrivateKey", privateKey);
		return keyMap;
	}
	@SuppressWarnings("rawtypes")
	public static void main(String args[]) {
		try {		
			Map map = initKey();
			RSAPublicKey publicKey = (RSAPublicKey) map.get("RSAPublicKey");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("RSAPrivateKey");
			System.out.println("********************************");
			System.out.println((new StringBuilder()).append("公钥加密算法：")
					.append(publicKey.getAlgorithm()).toString());
			System.out.println((new StringBuilder()).append("公钥加密格式：")
					.append(publicKey.getFormat()).toString());
			System.out.println((new StringBuilder()).append("私钥加密算法：")
					.append(privateKey.getAlgorithm()).toString());
			System.out.println((new StringBuilder()).append("私钥加密格式：")
					.append(privateKey.getFormat()).toString());
			System.out.println("********************************");
			String pk = getPublicKey(map);
			String sk = getPrivateKey(map);
			System.out.println((new StringBuilder()).append("公钥长度=：")
					.append(pk.length()).toString());
			System.out.println((new StringBuilder()).append("公钥：").append(pk)
					.toString());
			System.out.println((new StringBuilder()).append("私钥长度=：")
					.append(sk.length()).toString());
			System.out.println((new StringBuilder()).append("私钥：").append(sk)
					.toString());
			System.out.println("********************************");
//			String pk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4HjVUzmoj3k9iOfgarE9qa6Y4kIDm6MhOy3LZCCMIBGgpGzrtQ+o5xzAD/9/i9+mC/Sx/o8dAumlWvzIMXHnzbUi7EReK4U/YsNrJxro/hlyoeLm+73ijCtU5Ptq0TCgb1rLBxJfUuS+ZiCzUsTZdO28XnPwpvUWuaoaAHCQtjwIDAQAB";
//			String sk = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALgeNVTOaiPeT2I5+BqsT2prpjiQgOboyE7LctkIIwgEaCkbOu1D6jnHMAP/3+L36YL9LH+jx0C6aVa/MgxcefNtSLsRF4rhT9iw2snGuj+GXKh4ub7veKMK1Tk+2rRMKBvWssHEl9S5L5mILNSxNl07bxec/Cm9Ra5qhoAcJC2PAgMBAAECgYA16iqlzdgI7tWCJwV/EgwjVqiTEcWdWHcnk63q3dYcR+YUe3PS/Ls+9hQaZ7gw53XKtMZuDhCP5MZp740BT6cp4N0kjMcGuKIqT2q7nLQebsfRYs/bOtC6ZVCB6cbecaB+YL/a+Oc077xaSB9M9emzmnf/3rnu4+x+umx/1lVZWQJBAOgKvttmKXXt/0dx7MQluuM8aDySJa8y4fidU4SRDX/6SP11O7VvprShcNweRspbiEe1Rr0+0ZBrMkIzyhMFoxUCQQDLIMCtHVFc9rFwR2hBliufnURttUBLMrqnfVcY13LLEpjzmKMCY3nyjmzpPN5FGn6rWb1coKEhnctlTwM1FYcTAkBPP/RFXJQGef14+jjiGPzGRUhYf5WtS5SP47O1kBDrR6EMJ7D326U1BfpUw2ZsEGzwCMKbOcw7JdFpeOSoMIGtAkEAoEzFoRg86FdKaigAD5o57OmIYeeiyHVNNfZFMLZ9weJ4T+zJ342vZAqfymSdp/0jr4fPV3TwE/5Z0CZNiqvlfwJBALSyWTRb6OC1lQXbPsIe4nOcOGKbTi4lwQF6Fn90fmkIXTvFzimqDA5pA9G/Ac0rbL9RMZQ2MkJ0naJQPfYxl0k=";
			String str = "123";
			System.out.println((new StringBuilder()).append("公钥需要加密的字符串：")
					.append(str).toString());
			System.out.println("********************************");
			byte publicKeyEncryptBts[] = encryptByPublicKey(
					str.getBytes("UTF-8"), pk);
			byte base64EncodeBts[] = BASE64.encode(publicKeyEncryptBts).getBytes("UTF-8");
			String base64EncodeStr = new String(base64EncodeBts, "UTF-8");
			System.out.println((new StringBuilder()).append("公钥加密后的数据：")
					.append(base64EncodeStr).toString());
			System.out.println("********************************");
			byte base64DecodeBts[] = BASE64.decode(base64EncodeStr);
			byte sks[] = decryptByPrivateKey(base64DecodeBts, sk);
			String skss = new String(sks, "UTF-8");
			System.out.println((new StringBuilder()).append("私钥解密后的数据：")
					.append(skss).toString());
			System.out.println("********************************");
			byte privateKeyEncryptBts[] = str.getBytes("UTF-8");
			System.out.println((new StringBuilder()).append("私钥需要加密的字符串：")
					.append(str).toString());
			System.out.println("********************************");
			byte newsks[] = encryptByPrivateKey(privateKeyEncryptBts, sk);
			String newskss = BASE64.encode(newsks);
			System.out.println((new StringBuilder()).append("私钥加密后的数据：")
					.append(newskss).toString());
			System.out.println("********************************");
			byte newpks[] = decryptByPublicKey(newsks, pk);
			String newpkss = new String(newpks, "UTF-8");
			System.out.println((new StringBuilder()).append("公钥对私钥数据解密：")
					.append(newpkss).toString());
			byte result1[] = sign(str.getBytes("UTF-8"), sk);
			System.out.println((new StringBuilder()).append("私钥对数据签名：")
					.append(Arrays.toString(result1)).toString());
			boolean flag = verify(str.getBytes("UTF-8"), pk, result1);
			System.out.println((new StringBuilder()).append("公钥对数据验签：")
					.append(flag).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String RSA = "RSA";
	public static final String KEY_ALGORITHM_DETAIL = "RSA/ECB/PKCS1Padding";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static final String RSA_PUBLIC_KEY = "RSAPublicKey";
	public static final String RSA_PRIVATE_KEY = "RSAPrivateKey";
}
