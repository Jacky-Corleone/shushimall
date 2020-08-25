package com.camelot.openplatform.common.enums;

public enum MessageTypeEnum {
	SMS("2","短信"),
	MAIL("1","邮件");

	private String id;
	private String name;

	private MessageTypeEnum(String id,String name) {
		this.id = id;
		this.name = name;
	}

	public static MessageTypeEnum getEnumByName(String name) {
		for (MessageTypeEnum messageTypeEnum:MessageTypeEnum.values()) {
			if(messageTypeEnum.getName().equals(name)){
				return messageTypeEnum;
			}
		}
		return null;
	}

	public static MessageTypeEnum getEnumById(String id) {
		for (MessageTypeEnum messageTypeEnum:MessageTypeEnum.values()) {
			if(messageTypeEnum.getId().equals(id)){
				return messageTypeEnum;
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
