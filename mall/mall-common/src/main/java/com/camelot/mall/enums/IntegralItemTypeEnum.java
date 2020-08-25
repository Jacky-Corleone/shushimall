package com.camelot.mall.enums;

public enum IntegralItemTypeEnum {
	BANNER(1,"轮播"),
	AD(2,"广告");

	private Integer id;
	private String name;

	private IntegralItemTypeEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static IntegralItemTypeEnum getEnumByName(String name) {
		for (IntegralItemTypeEnum integralItemTypeEnum:IntegralItemTypeEnum.values()) {
			if(integralItemTypeEnum.getName().equals(name)){
				return integralItemTypeEnum;
			}
		}
		return null;
	}

	public static IntegralItemTypeEnum getEnumById(Integer id) {
		for (IntegralItemTypeEnum integralItemTypeEnum:IntegralItemTypeEnum.values()) {
			if(integralItemTypeEnum.getId()==id){
				return integralItemTypeEnum;
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
