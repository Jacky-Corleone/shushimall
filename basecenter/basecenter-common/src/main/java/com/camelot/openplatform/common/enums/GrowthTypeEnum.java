package com.camelot.openplatform.common.enums;

public enum GrowthTypeEnum {
	LOGIN(1,"登录"),
	SHOP(2,"购物"),
	EVALUATE(3,"评价"),
	SHARE(4,"评价+晒单");

	private Integer id;
	private String name;

	private GrowthTypeEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static GrowthTypeEnum getEnumByName(String name) {
		for (GrowthTypeEnum messageContentTypeEnum:GrowthTypeEnum.values()) {
			if(messageContentTypeEnum.getName().equals(name)){
				return messageContentTypeEnum;
			}
		}
		return null;
	}

	public static GrowthTypeEnum getEnumById(Integer id) {
		for (GrowthTypeEnum messageContentTypeEnum:GrowthTypeEnum.values()) {
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
