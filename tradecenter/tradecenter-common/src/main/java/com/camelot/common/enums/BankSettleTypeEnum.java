package com.camelot.common.enums;

/**
 *  支付方式
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-12
 */
public enum BankSettleTypeEnum {

	AP("AP",0), 	//支付宝
	CB("CB",1),//网银在线	
	WX("WX",5),//微信
	AP_MOBILE("AP_MOBILE",100),//支付宝_手机端
	CB_MOBILE("CB_MOBILE",101),//网银在线_手机端
	CITIC("CITIC",2),//中信银行
	OFFLINE("OFFLINE",3),//线下支付
	OTHER("OTHER",4),//支付宝其他银行
	WXPC("WXPC",6);//微信PC端
	
	private int code;
	private String lable;

	private BankSettleTypeEnum(String lable ,int code) {
		this.lable = lable;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
