package com.camelot.sellercenter.enums;

public enum MallTypeEnum {
	MALL(1,"商城"),
	INTEGRALMALL(4,"积分商城");

	private Integer id;
	private String name;

	private MallTypeEnum(Integer id,String name) {
		this.id = id;
		this.name = name;
	}

	public static MallTypeEnum getEnumByName(String name) {
		for (MallTypeEnum mallTypeEnum:MallTypeEnum.values()) {
			if(mallTypeEnum.getName().equals(name)){
				return mallTypeEnum;
			}
		}
		return null;
	}

	public static MallTypeEnum getEnumById(Integer id) {
		for (MallTypeEnum mallTypeEnum:MallTypeEnum.values()) {
			if(mallTypeEnum.getId()==id){
				return mallTypeEnum;
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
