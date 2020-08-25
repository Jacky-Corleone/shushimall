package com.camelot.delivery.dto;

import java.io.Serializable;

/**
 * <p>
 * Description: [物流信息发送基本信息]
 * </p>
 * Created on 2015年8月10日
 * 
 * @author <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ParametersDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1239574639391925157L;
	
	private String callbackurl; // 回调地址
	private String salt;// 电子签名
	private String resultv2;// 区域解析

	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getResultv2() {
		return resultv2;
	}

	public void setResultv2(String resultv2) {
		this.resultv2 = resultv2;
	}

	@Override
	public String toString() {
		return "Parameters [callbackurl=" + callbackurl + ", salt=" + salt
				+ ", resultv2=" + resultv2 + "]";
	}

}
