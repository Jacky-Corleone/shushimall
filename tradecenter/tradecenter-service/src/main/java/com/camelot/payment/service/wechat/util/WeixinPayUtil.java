package com.camelot.payment.service.wechat.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.camelot.common.constants.SysConstants;
import com.camelot.common.util.CommonUtil;
import com.camelot.openplatform.util.SysProperties;


public class WeixinPayUtil {
	
	/**
	 * 封装商户信息
	 */ 
	public static Map<String, String> packInfo() {
		Map<String, String> params =new HashMap<String, String>();
		params.put("appid", SysProperties.getProperty(SysConstants.WX_APP_ID));
		params.put("mch_id", SysProperties.getProperty(SysConstants.WX_MCH_ID));
		params.put("partnerKey", SysProperties.getProperty(SysConstants.WX_PARTNER_KEY));
		return params;
	}
	
	/**
	 * 商家生成随机字符串
	 * 
	 */
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
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
	
	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
	/** 
	  * 生成明文XML
	  *
	  * @param params 
	  * @param params 
	  * @return
	  * @throws 
	  **/ 
	@SuppressWarnings("rawtypes")
	public static String buildTranDataXml(Map<String ,String> params,String noncestr,String timeStart,String sign){
		StringBuffer sb=new StringBuffer();
		sb.append("<xml>");
		
		List list=CommonUtil.getFiledsInfo(mapToTranData(params, noncestr, timeStart, sign));
		for (int i = 0; i < list.size(); i++) {
			Map infoMap=(Map)list.get(i);
			if(infoMap.get("value")!=null&&!"".equals(infoMap.get("value"))){
				sb.append("<").append(infoMap.get("name")).append(">")
				.append(infoMap.get("value")).append("</").append(infoMap.get("name")).append(">");
			}
		}
		sb.append("</xml>");
		
		System.out.println("XML："+sb.toString());
		return sb.toString();
	}
	
	/** 
	  * 封装对象（用于微信支付、微信退款）
	  *
	  **/ 
	public static RequestInfo mapToTranData(Map<String ,String> params,String noncestr,String timeStart,String sign) {
		RequestInfo requestInfo = new RequestInfo();
		if(params != null){
			requestInfo.setAppid(params.get("appid"));//*微信分配的公众账号 ID
			requestInfo.setMch_id(params.get("mch_id"));//*微信支付分配的商户号
			requestInfo.setDevice_info(params.get("device_info"));//微信支付分配的终端设备号
			requestInfo.setNonce_str(noncestr);// * 随机字符
			if(StringUtils.isNotBlank(sign)){
				requestInfo.setSign("<![CDATA["+sign+"]]>");// * 签名
			}
			if(StringUtils.isNotBlank(params.get("body"))){
				requestInfo.setBody("<![CDATA["+params.get("body")+"]]>");// *描述
			}
			if(StringUtils.isNotBlank(params.get("attach"))){
				requestInfo.setAttach("<![CDATA["+params.get("attach")+"]]>");// 附加数据
			}
			requestInfo.setOut_trade_no(params.get("out_trade_no"));//*商户订单号
			requestInfo.setTotal_fee(params.get("total_fee"));//*订单总金额，单位为分
			requestInfo.setSpbill_create_ip(params.get("spbill_create_ip"));// *订单生成的机器 IP
			requestInfo.setTime_start(timeStart);// 交易起始时间
			requestInfo.setTime_expire(params.get("time_expire"));// 交易结束时间
			requestInfo.setGoods_tag(params.get("goods_tag"));//商品标记
			requestInfo.setNotify_url(params.get("notify_url"));// *接收微信支付成功通知 通知地址
			requestInfo.setTrade_type(params.get("trade_type"));// *交易类型 JSAPI、NATIVE、APP
			requestInfo.setOpenid(params.get("openid"));// 用户在商户 appid 下的唯一 trade_type 为 JSAPI 时，此参数必传
			requestInfo.setProduct_id(params.get("product_id"));// 只在 trade_type 为 NATIVE时需要填写。此 id 为二维码中包含的商品 ID，商户自行 维护。
			requestInfo.setOp_user_id(params.get("op_user_id"));
			requestInfo.setOut_refund_no(params.get("out_refund_no"));
			requestInfo.setRefund_fee(params.get("refund_fee"));
			requestInfo.setRefund_fee_type(params.get("refund_fee_type"));
		}
		return requestInfo;
	}
	
}
	