package com.camelot.payment.service.wechat.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.camelot.payment.service.wechat.util.TenpayUtil;


/*
 '微信支付服务器签名支付请求请求类
 '============================================================================
 'api说明：
 'init(app_id, app_secret, partner_key, app_key);
 '初始化函数，默认给一些参数赋值，如cmdno,date等。
 'setKey(key_)'设置商户密钥
 'getLasterrCode(),获取最后错误号
 'GetToken();获取Token
 'getTokenReal();Token过期后实时获取Token
 'createMd5Sign(signParams);生成Md5签名
 'genPackage(packageParams);获取package包
 'createSHA1Sign(signParams);创建签名SHA1
 'sendPrepay(packageParams);提交预支付
 'getDebugInfo(),获取debug信息
 '============================================================================
 '*/
public class RequestHandler {

	/** 商户参数 */
	private String appid;
	private String partnerkey;
	private String key;
	/** 请求的参数 */
	private SortedMap parameters;

	/**
	 * 初始构造函数。
	 * 
	 * @return
	 */
	public RequestHandler() {
		this.parameters = new TreeMap();
	}

	/**
	 * 初始化函数。
	 */
	public void init(String app_id, String partner_key) {
		this.appid = app_id;
		this.partnerkey = partner_key;
		this.key = partner_key;
	}

	public void init() {
	}

	/**
	 * 获取参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + this.getKey());
		System.out.println("md5 sb:" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(),TenpayUtil.getCharacterEncoding())
				.toUpperCase();
		System.out.println("packge签名:" + sign);
		return sign;

	}
	/**
	 * 创建package签名
	 */
	public boolean createMd5Sign(String signParams) {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}

		// 算出摘要
		String enc = TenpayUtil.getCharacterEncoding();
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

		String tenpaySign = this.getParameter("sign").toLowerCase();

		return tenpaySign.equals(sign);
	}

    //输出XML
	   public String parseXML() {
		   StringBuffer sb = new StringBuffer();
	       sb.append("<xml>");
	       Set es = this.parameters.entrySet();
	       Iterator it = es.iterator();
	       while(it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String k = (String)entry.getKey();
				String v = (String)entry.getValue();
				if(null != v && !"".equals(v) && !"appkey".equals(k)) {
					
					sb.append("<" + k +">" + getParameter(k) + "</" + k + ">\n");
				}
			}
	       sb.append("</xml>");
			return sb.toString();
		}

	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}
	public String getKey() {
		return partnerkey;
	}

}
