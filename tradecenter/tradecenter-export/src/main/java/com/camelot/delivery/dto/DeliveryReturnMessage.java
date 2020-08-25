package com.camelot.delivery.dto;

import java.io.Serializable;

/**
 * <p>
 * Description: [快递100订阅返回信息]
 * </p>
 * Created on 2015年8月10日
 * 
 * @author <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class DeliveryReturnMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -509470940671489636L;
	/**
	 * 
	 */

	private String result;
	private Integer returnCode;
	private String message;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "deliveryReturnMessage [result=" + result + ", returnCode="
				+ returnCode + ", message=" + message + "]";
	}

}
