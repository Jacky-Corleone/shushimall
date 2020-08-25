package com.camelot.openplatform.common.enums;

/**
 *  促销的时间状态枚举
 *
 * @Description -
 * @author - 武超强
 * @createDate - 2015-11-10
 */
public enum PromotionTimeStatusEnum {
	UNBUGUN("未开始",1),
	UNDERWAY("进行中",2),
	ENDED("已结束",3),
	DISCONTINUE("已终止",4);

	private String lable;
	private Integer status;// 状态

	private PromotionTimeStatusEnum(String lable,Integer status) {
		this.lable = lable;
		this.status = status;
	}

	public static PromotionTimeStatusEnum getEnumByName(String name) {
		for (PromotionTimeStatusEnum enums:PromotionTimeStatusEnum.values()) {
			if(enums.name().equals(name)){
				return enums;
			}
		}
		return null;
	}

	public static PromotionTimeStatusEnum getEnumByStatus(Integer status) {
		for (PromotionTimeStatusEnum enums:PromotionTimeStatusEnum.values()) {
			if(enums.getStatus()==status){
				return enums;
			}
		}
		return null;
	}

	public Integer getStatus() {
		return status;
	}

	public String getLable() {
		return lable;
	}
}
