package com.camelot.maketcenter.dto.emums;

/**
 * 
 * <p>Description: [优惠券使用范围枚举类]</p>
 * Created on 2015年12月8日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum CouponUsingRangeEnum {
	/**
	 * 平台通用
	 */
	PLATFORM(1, "平台通用"),
	/**
	 * 店铺通用
	 */
	SHOP(2, "店铺通用"),
	/**
	 * 类目通用
	 */
	CATEGORY(3, "类目通用"),
	/**
	 * SKU使用类
	 */
	SKU(4, "SKU使用类");

	private int code;
	private String label;

	CouponUsingRangeEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static CouponUsingRangeEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (CouponUsingRangeEnum couponUsingRangeEnum : CouponUsingRangeEnum.values()) {
				if (couponUsingRangeEnum.getCode() == code) {
					return couponUsingRangeEnum;
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
