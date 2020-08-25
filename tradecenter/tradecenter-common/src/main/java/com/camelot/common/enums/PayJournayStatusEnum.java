package com.camelot.common.enums;

/**
 *  流水单数据类型
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2014-11-21
 */
public enum PayJournayStatusEnum {
	INDEX_REQ("0","首页请求"),
	PAY_REQ("1","支付请求"),
	SYNCHRONOUS_NOTICE ("2","同步通知"),
	ASYNCHRONOUS_NOTICE ("3","异步通知"),
	ORDER_QUERY("4","订单查询"),
	WX_CALLBACK("5","微信回调"),
	CITIC_OUT("6","中信提现");
	
	private String code;
	private String info;
	
	public String getCode() {
		return code;
	}
	public String getInfo() {
		return info;
	}
	private PayJournayStatusEnum(String code,String info) {
		this.code = code;
	}
	
}
