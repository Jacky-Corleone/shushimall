package com.camelot.common.enums;

/** 
 * <p>Description: [促销活动枚举]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public enum ActivityTypeEnum {
	PLATFORMMARKDOWN("平台直降活动",1),
	PLATFORMFULLREDUCTION("平台满减活动",2),
	PLATFORMCOUPONS("平台优惠券活动",3),
	PLATFORMINTEGRATION("平台积分活动",4),
	SHOPMARKDOWN("店铺直降活动",5),
	SHOPFULLREDUCTION("店铺满减活动",6),
	SHOPCOUPONS("店铺优惠券活动",7),
	SHOPCHANGEPRICE("店铺改价优惠活动",8);

	private String lable;
	private Integer status;// 状态

	private ActivityTypeEnum(String lable,Integer status) {
		this.lable = lable;
		this.status = status;
	}

	public static ActivityTypeEnum getEnumByName(String name) {
		for (ActivityTypeEnum enums:ActivityTypeEnum.values()) {
			if(enums.name().equals(name)){
				return enums;
			}
		}
		return null;
	}

	public static ActivityTypeEnum getEnumByStatus(Integer status) {
		for (ActivityTypeEnum enums:ActivityTypeEnum.values()) {
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
