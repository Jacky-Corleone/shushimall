package com.camelot.openplatform.common.enums;

public enum MessageSignatureTypeEnum {
	KEYIN_SIGNATURE(1,"舒适100"),
	GREEN_SIGNATURE(2,"绿印中心");

	private Integer id;
	private String name;

	private MessageSignatureTypeEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static MessageSignatureTypeEnum getEnumByName(String name) {
		for (MessageSignatureTypeEnum messageContentTypeEnum:MessageSignatureTypeEnum.values()) {
			if(messageContentTypeEnum.getName().equals(name)){
				return messageContentTypeEnum;
			}
		}
		return null;
	}

	public static MessageSignatureTypeEnum getEnumById(Integer id) {
		for (MessageSignatureTypeEnum messageContentTypeEnum:MessageSignatureTypeEnum.values()) {
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
