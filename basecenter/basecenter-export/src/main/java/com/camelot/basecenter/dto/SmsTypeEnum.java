package com.camelot.basecenter.dto;

public enum SmsTypeEnum {
	REGISTER("sms.register","用户注册"),
	UPLOGINPWD("sms.uploginpwd","修改登录密码"),
	UPBINDPHONE("sms.bindphone","修改绑定电话"),
	UPBINDEMAIL("sms.upbindemail","修改绑定邮箱"),
	SETPAYPWD("sms.setpaypwd","设置支付密码"),
	UPPAYPWD("sms.uppaypwd","修改支付密码"),
    UPGOLDACCOUNT("sms.upgoldaccount","修改出金账户"),
	ORDERSUBMITPROXY("sms.ordersubmitproxy","代下单");
	
	private SmsTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	private String code;
	private String desc;
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
