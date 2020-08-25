package com.camelot.maketcenter.dto.emums;

/**
 * 
 * <p>Description: [优惠券类型枚举类]</p>
 * Created on 2015年12月8日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum CouponTypeEnum {
	/**
	 * 满减券
	 */
	FULL_REDUCTION(1, "满减券"),
	/**
	 * 折扣券
	 */
	DISCOUNT(2, "折扣券"),
	/**
	 * 现金券
	 */
	CASH(3, "现金券");

	private int code;
	private String label;

	CouponTypeEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static CouponTypeEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (CouponTypeEnum couponUsingRangeEnum : CouponTypeEnum.values()) {
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
