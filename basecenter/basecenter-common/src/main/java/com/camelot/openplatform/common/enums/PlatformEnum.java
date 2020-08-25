package com.camelot.openplatform.common.enums;

public enum PlatformEnum {
	KEYIN(0,"舒适100"),
	GREEN(2,"上海绿印中心");

	private Integer id;
	private String name;

	private PlatformEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static PlatformEnum getEnumByName(String name) {
		for (PlatformEnum platformEnum:PlatformEnum.values()) {
			if(platformEnum.getName().equals(name)){
				return platformEnum;
			}
		}
		return null;
	}

	public static PlatformEnum getEnumById(Integer id) {
		for (PlatformEnum platformEnum:PlatformEnum.values()) {
			if(platformEnum.getId()==id){
				return platformEnum;
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
