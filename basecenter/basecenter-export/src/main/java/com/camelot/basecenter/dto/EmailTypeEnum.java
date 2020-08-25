package com.camelot.basecenter.dto;

public enum EmailTypeEnum {
	
	EMAIL_REGISTER("email.register","用户注册"),
	EMAIL_REGISTER_SUCCESS("email.register_success","用户注册成功"),
	EMAIL_UP_LOGIN_PWD("email.up_login_pwd","修改登录密码"),
	EMAIL_UP_LOGIN_PWD_SUCCESS("email.up_login_pwd_success","修改登录密码成功"),
	EMAIL_UP_BIND_PHONE("email.up_bind_phone","修改绑定电话"),
	EMAIL_UP_BIND_PHONE_SUCCESS("email.bind_phone_success","修改绑定电话成功"),
	EMAIL_UP_BIND_EMAIL("email.up_bind_email","修改绑定邮箱"),
	EMAIL_UP_BIND_EMAIL_SUCCESS("email.up_bind_email_success","修改绑定邮箱成功"),
	EMAIL_SET_PAY_PWD("email.set_pay_pwd","设置支付密码"),
	EMAIL_UP_PAY_PWD("email.up_pay_pwd","修改支付密码"),
	EMAIL_ORDERSUBMITPROXY("email.ordersubmitproxy","代下单");
	
	private EmailTypeEnum(String code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	private String code;
	private String desc;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
