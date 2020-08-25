package com.camelot.maketcenter.dto.emums;

/**
 * 
 * <p>Description: [优惠券使用平台枚举类]</p>
 * Created on 2015年12月8日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum CouponUsingPlatformEnum {
	/**
	 * 印刷家平台
	 */
	KY(1, "印刷家平台"),
	/**
	 * 绿印平台
	 */
	LY(2, "绿印平台"),
	/**
	 * 小管家
	 */
	WX(3, "小管家"),
	
	/**
	 * 科印和小管家
	 */
	KYANDWX(4, "全部");

	private int code;
	private String label;

	CouponUsingPlatformEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static CouponUsingPlatformEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (CouponUsingPlatformEnum couponUsingRangeEnum : CouponUsingPlatformEnum.values()) {
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
