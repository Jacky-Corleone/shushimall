package com.camelot.maketcenter.dto.emums;


/**
 * 
 * <p>Description: [集采活动枚举类：0、未发布；1、已发布；2、已售罄；3、活动中止；4、已删除]</p>
 * Created on 2015年11月29日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum CentralPurchasingActivityStatusEnum {
	/**
	 * 未发布
	 */
	UNPUBLISHED(0, "未发布"),
	/**
	 * 已发布
	 */
	PUBLISHED(1, "已发布"),
	/**
	 * 已售罄
	 */
	ALREADY_SOLD_OUT(2, "已售罄"),
	/**
	 * 活动中止
	 */
	ACTIVITY_TERMINATION(3, "活动中止"),
	/**
	 * 已删除
	 */
	DELETED(4, "已删除");

	private int code;
	private String label;

	CentralPurchasingActivityStatusEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}
	
	public static CentralPurchasingActivityStatusEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (CentralPurchasingActivityStatusEnum centralPurchasingActivityStatus : CentralPurchasingActivityStatusEnum.values()) {
				if (centralPurchasingActivityStatus.getCode() == code) {
					return centralPurchasingActivityStatus;
				}
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}
}
