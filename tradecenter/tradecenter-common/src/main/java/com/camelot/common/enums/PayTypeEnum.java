package com.camelot.common.enums;

/**
 *  支付方式
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-12
 */
public enum PayTypeEnum {

	PAY_CASH("CASH"), 	//货到付款
	PAY_ONLINE("ONLINE");//在线支付 	

	private String code;

	private PayTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	
}
