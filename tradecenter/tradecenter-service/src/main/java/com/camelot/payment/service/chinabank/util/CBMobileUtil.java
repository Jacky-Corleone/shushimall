package com.camelot.payment.service.chinabank.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.constants.SysConstants;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.dto.PayReqParam;

/**
 * Created by wywangzhenlong on 14-8-9.
 */
public class CBMobileUtil {
	private final static Logger logger = LoggerFactory.getLogger(CBMobileUtil.class);
// 不需要签名的object中的对象key
    private  static List<String> unSignKeyList = Arrays.asList("merchantSign", "token", "version","serialVersionUID");

    /**
	 * 封装商户信息
	 */ 
	public Map<String, String> packInfo() {
		Map<String, String> params =new HashMap<String, String>();
		params.put(SysConstants.CB_MER_ID, SysProperties.getProperty(SysConstants.CB_MER_ID));
		params.put(SysConstants.CB_PRI_KEY, SysProperties.getProperty(SysConstants.CB_PRI_KEY));
		return params;
	}
	
    public static PaySignEntity buildPaySignEntity(PayReqParam payReqParam) {
    	PaySignEntity wePayMerchantSignReqDTO = new PaySignEntity();
        wePayMerchantSignReqDTO.setToken(StringUtils.isNotBlank(payReqParam.getExtraParam())?payReqParam.getExtraParam():"");// 网银在线token值，免于二次输入账号密码
        wePayMerchantSignReqDTO.setMerchantNum(SysProperties.getProperty(SysConstants.CB_M_MER_ID));
        wePayMerchantSignReqDTO.setTradeNum(payReqParam.getOutTradeNo());
        wePayMerchantSignReqDTO.setTradeTime(DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
        wePayMerchantSignReqDTO.setTradeName(StringUtils.isNotBlank(payReqParam.getSubject())?payReqParam.getSubject():"普通商品");
        wePayMerchantSignReqDTO.setCurrency(SysConstants.CB_MONEY_TYPE);
        wePayMerchantSignReqDTO.setMerchantRemark(payReqParam.getExtraParam());
		Long amountByFen = payReqParam.getTotalFee().multiply(new BigDecimal("100")).longValue(); // 转换成分
        wePayMerchantSignReqDTO.setTradeAmount(amountByFen.toString());
        wePayMerchantSignReqDTO.setTradeDescription("手机支付");
        wePayMerchantSignReqDTO.setSuccessCallbackUrl(SysProperties.getProperty(SysConstants.CB_M_SUCCESS_URL));
        wePayMerchantSignReqDTO.setFailCallbackUrl(SysProperties.getProperty(SysConstants.CB_M_FAIL_URL));
        wePayMerchantSignReqDTO.setNotifyUrl(SysProperties.getProperty(SysConstants.CB_M_NOTIFY_URL));
    	return wePayMerchantSignReqDTO;
    }
    
    /**
     * 私钥对加密数据进行签名(加密)
     *
     * @param object        需要签名(加密)的对象
     * @param rsaPriKey     私钥
     * @param rsaPriKey     私钥
     * @return
     */
    public static PaySignEntity sign(PayReqParam payReqParam, String rsaPriKeyFile) {
    	String  rsaPriKey= SysProperties.getProperty(SysConstants.CB_M_RSA_PRI_KEY);
        PaySignEntity webPayReqDto =buildPaySignEntity(payReqParam);
        try {
            //获取签名需要字符串和类型
            String sourceSignString = CBMobileUtil.signString(webPayReqDto, unSignKeyList);
            //摘要
            String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString,null);
            //私钥对摘要进行加密
            byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
            webPayReqDto.setMerchantSign(RSACoder.encryptBASE64(newsks));
	        if ("1.0".equals(webPayReqDto.getVersion())) {
	            //敏感信息未加密
	        } else if ("2.0".equals(webPayReqDto.getVersion())) {
	            //敏感信息加密
                //获取商户 DESkey
                String desKey = SysProperties.getProperty(SysConstants.CB_M_DES_PRI_KEY);
                //对敏感信息进行 DES加密
                webPayReqDto.setMerchantRemark(DESUtil.encrypt(webPayReqDto.getMerchantRemark(), desKey, "UTF-8"));
                webPayReqDto.setTradeNum(DESUtil.encrypt(webPayReqDto.getTradeNum(), desKey, "UTF-8"));
                webPayReqDto.setTradeName(DESUtil.encrypt(webPayReqDto.getTradeName(), desKey, "UTF-8"));
                webPayReqDto.setTradeDescription(DESUtil.encrypt(webPayReqDto.getTradeDescription(), desKey, "UTF-8"));
                webPayReqDto.setTradeTime(DESUtil.encrypt(webPayReqDto.getTradeTime(), desKey, "UTF-8"));
                webPayReqDto.setTradeAmount(DESUtil.encrypt(webPayReqDto.getTradeAmount(), desKey, "UTF-8"));
                webPayReqDto.setCurrency(DESUtil.encrypt(webPayReqDto.getCurrency(), desKey, "UTF-8"));
                webPayReqDto.setNotifyUrl(DESUtil.encrypt(webPayReqDto.getNotifyUrl(), desKey, "UTF-8"));
                webPayReqDto.setSuccessCallbackUrl(DESUtil.encrypt(webPayReqDto.getSuccessCallbackUrl(), desKey, "UTF-8"));
                webPayReqDto.setFailCallbackUrl(DESUtil.encrypt(webPayReqDto.getFailCallbackUrl(), desKey, "UTF-8"));
	        }
        } catch (Exception e) {
        	logger.info("sign错误：",e.getMessage());
        }
        return webPayReqDto;
    }

    /**
     * 生成签名原串
     *
     * @param object
     * @param unSignKeyList
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("rawtypes")
	public static String signString(Object object, List<String> unSignKeyList) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = CBMobileUtil.objectToMap(object);

        // 拼原String
        StringBuilder sb = new StringBuilder();
        // 删除不需要参与签名的属性
        for (String str : unSignKeyList) {
            map.remove(str);
        }
        // 连接
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue()) + "&");
        }
        // 去掉最后一个&
        String result = sb.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    /**
     * 对象转map
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static TreeMap<String, Object> objectToMap(Object object) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = new TreeMap<String, Object>();

        // 父类属性
        for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            // 添加属性key到list
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(object));
            }
        }
        return map;
    }
    
    public static ExecuteResult<AsynNotificationReqDto> vefitySign(String resp,String md5PriKey){
    	ExecuteResult<AsynNotificationReqDto> result =new ExecuteResult<AsynNotificationReqDto>();
    	byte[] decryptBASE64Arr =  BASE64.decode(resp); //首先对Base64编码的数据进行解密
    	AsynNotificationReqDto dto=parseXML(decryptBASE64Arr);//解析XML
    	 //验证签名
        try {
			String ownSign = generateSign(dto.getVersion(), dto.getMerchant(),
					dto.getTerminal(), dto.getData(), md5PriKey);
			if (dto.getSign().equals(ownSign)) {
				result.setResult(dto);
			}else{
           	 result.addErrorMessage("解析交易流程控制对象失败,签名验证错误");
           }
        } catch (Exception e) {
        	logger.info("vefitySign错误：",e.getMessage());
        	result.addErrorMessage("签名验证异常");
		}
    	return result;
    }
    
    //XML解析为Java对象
    @SuppressWarnings("unused")
	private static AsynNotificationReqDto parseXML(byte[] xmlString) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            InputStream is = new ByteArrayInputStream(xmlString);
            SAXReader sax = new SAXReader(false);
            document = sax.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        AsynNotificationReqDto dto = new AsynNotificationReqDto();
        Element rootElement = document.getRootElement();
        if (null == rootElement) {
            return dto;
        }
        Element versionEliment = rootElement.element("VERSION");
        if (null != versionEliment) {
            dto.setVersion(versionEliment.getText());
        }
        Element merchantEliment = rootElement.element("MERCHANT");
        if (null != merchantEliment) {
            dto.setMerchant(merchantEliment.getText());
        }
        Element terminalEliment = rootElement.element("TERMINAL");
        if (null != terminalEliment) {
            dto.setTerminal(terminalEliment.getText());
        }
        Element datalEliment = rootElement.element("DATA");
        if (null != datalEliment) {
            dto.setData(datalEliment.getText());
        }
        Element signEliment = rootElement.element("SIGN");
        if (null != signEliment) {
            dto.setSign(signEliment.getText());
        }
        return dto;
    }
    
    /**
     * 签名
     */
    private static String generateSign(String version, String merchant, String terminal, String data, String md5Key) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append(merchant);
        sb.append(terminal);
        sb.append(data);
        String sign = "";
        sign = md5(sb.toString(), md5Key);
        return sign;
    }
    
    public static String md5(String text, String salt) throws Exception {
        byte[] bytes = (text + salt).getBytes();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return sb.toString().toLowerCase();
    }
    
    private static String getCertStr(String args[], String keyName) {
		StringBuilder fileAddress = new StringBuilder(CBMobileUtil.class.getResource("/").getPath());
		if(args!=null){
			for (String arg : args) {
				// 拼接URI分隔符
				fileAddress.append(arg).append("/");
			}
		}
		fileAddress.append(keyName==null?"":keyName).toString();
		String fileAddressStr=fileAddress.toString().replace("test-classes", "classes");// TODO 测试时添加
		StringBuffer sb = new StringBuffer();
    	File file=new File(fileAddressStr);
        InputStreamReader read;
		try {
			read = new InputStreamReader(
			new FileInputStream(file),"UTF-8");
			 BufferedReader bufferedReader = new BufferedReader(read);
		        String lineTxt = null;
		        while((lineTxt = bufferedReader.readLine()) != null){
		        	if(!(lineTxt.contains("-----BEGIN RSA PRIVATE KEY-----")||lineTxt.contains("-----END RSA PRIVATE KEY-----"))){
		        		sb.append(lineTxt);
		        	}
		        }
		        read.close();
		} catch (Exception e) {
		} 
		return sb.toString();
	}
}
