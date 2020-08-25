package com.camelot.payment.service.wechat.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.camelot.payment.service.wechat.util.TenpayUtil;


/**
 * 应答处理类
 * 应答处理类继承此类，重写isTenpaySign方法即可。
 * @author miklchen
 *
 */
@SuppressWarnings("rawtypes")
public class ResponseHandler { 
	
	/** 密钥 */
	private String key;
	/** 应答的参数 */
	private SortedMap parameters; 
	/** debug信息 */
	private String debugInfo;
	
	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(Map<String, String> params)  {

		this.key = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";

		Map<String, String> m = params;
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = m.get(k);			
			this.setParameter(k, v);
		}
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	@SuppressWarnings("unchecked")
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * 返回所有的参数
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	
	/**
	 * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @return boolean
	 */
	public boolean isTenpaySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		
		//算出摘要
		String enc = TenpayUtil.getCharacterEncoding();
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();
		
		String tenpaySign = this.getParameter("sign").toLowerCase();
		
		//debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	*获取debug信息
	*/
	public String getDebugInfo() {
		return debugInfo;
	}
	
	/**
	*设置debug信息
	*/
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
}
