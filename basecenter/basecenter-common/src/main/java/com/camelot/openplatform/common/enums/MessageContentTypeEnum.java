package com.camelot.openplatform.common.enums;

public enum MessageContentTypeEnum {
	NOTIC(2,"通知"),
	VERIFY(1,"验证码");

	private Integer id;
	private String name;

	private MessageContentTypeEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static MessageContentTypeEnum getEnumByName(String name) {
		for (MessageContentTypeEnum messageContentTypeEnum:MessageContentTypeEnum.values()) {
			if(messageContentTypeEnum.getName().equals(name)){
				return messageContentTypeEnum;
			}
		}
		return null;
	}

	public static MessageContentTypeEnum getEnumById(Integer id) {
		for (MessageContentTypeEnum messageContentTypeEnum:MessageContentTypeEnum.values()) {
			if(messageContentTypeEnum.getId()==id){
				return messageContentTypeEnum;
			}
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
