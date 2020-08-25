package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

public class VerifyCodeMessageDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;//标题
	private String[] address;//邮件/手机 地址
	private String content;//内容
	private String enumType;//模版类型
	private String key;//用于reids中存放时的key
	private String type;//类型 1:短信 2：邮件
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getAddress() {
		return address;
	}
	public void setAddress(String[] address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEnumType() {
		return enumType;
	}
	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
