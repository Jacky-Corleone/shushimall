package com.camelot.payment.service.chinabank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.camelot.common.constants.SysConstants;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.dto.PayReqParam;

public class ChinaBankUtil {
	
	/**
	 * 封装商户信息
	 */ 
	public static Map<String, String> packInfo() {
		Map<String, String> params =new HashMap<String, String>();
		params.put(SysConstants.CB_MER_ID, SysProperties.getProperty(SysConstants.CB_MER_ID));
		params.put(SysConstants.CB_PRI_KEY, SysProperties.getProperty(SysConstants.CB_PRI_KEY));
		return params;
	}
	
	 /**
	  * 将订单中的v_amount v_moneytype v_oid v_mid v_url key
	  * 六个参数的value值拼成一个无间隔的字符串(顺序不要改变)       
      *得出的32位MD5值需转化为大写。
	  * @param params
	  * @return
	  */
	public static String Signature(PayReqParam payReqParam,Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		sb.append(payReqParam.getTotalFee())
				.append(SysConstants.CB_MONEY_TYPE)
				.append(payReqParam.getOutTradeNo())
				.append(params.get(SysConstants.CB_MER_ID));
		if(payReqParam.getPlatformId() == null){
			sb.append(SysProperties.getProperty(SysConstants.CB_RETURN_URL));
		}else if(payReqParam.getPlatformId() == PlatformEnum.GREEN.getId()){
			sb.append(SysProperties.getProperty(SysConstants.GREEN_CB_RETURN_URL));
		}
				
		sb.append(params.get(SysConstants.CB_PRI_KEY));
		return getMD5ofStr(sb.toString());
	 }
	
	public static String getMD5ofStr(String str) {
//		MD5 md=new MD5();
//		return md.getMD5ofStr(str).toUpperCase();
		return DigestUtils.md5Hex(str).toUpperCase();
	}
	
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String url, String strButtonName) {
        //待请求参数数组
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"form\" name=\"E_FORM\" action=\"" + url+ "\" method=\"post\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

        return sbHtml.toString();
    }
}
