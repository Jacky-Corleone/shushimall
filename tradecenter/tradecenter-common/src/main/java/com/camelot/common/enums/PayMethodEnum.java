package com.camelot.common.enums;

/**
 *  支付方式
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-12
 */
public enum PayMethodEnum {

	PAY_PERSONAL("个人支付",1), 	//个人支付
	PAY_ENTERPRISE("企业支付",2),//企业支付 	
	PAY_INTEGRAL("积分支付",3);//积分支付

	private String lable;
	private Integer code;//

	private PayMethodEnum(String lable,Integer code) {
		this.lable = lable;
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	public String getLable(){
		return lable;
	}
	
}
