package com.camelot.maketcenter.dto.emums;


/**
 * 
 * <p>Description: [比CentralPurchasingActivityStatusEnum更加详细的集采活动枚举类（此状态值不存库）：0、未发布 1、活动进行中 2、报名进行中 3、报名未开始 4、活动未开始 5、已售罄 6、活动终止 7、已结束]</p>
 * 对于集采活动展示页面：
 * 活动进行中可以对活动截止时间进行倒计时，报名进行中可以对报名截止时间进行倒计时
 * 报名未开始可以对报名开始时间进行倒计时，活动未开始可以对活动开始时间进行倒计时
 * Created on 2015年11月29日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum CentralPurchasingActivityRealStatusEnum {
	/**
	 * 未发布
	 */
	UNPUBLISHED(0, "未发布"),
	/**
	 * 活动进行中
	 */
	INACTIVITY(1, "活动进行中"),
	/**
	 * 报名进行中
	 */
	INSIGNUP(2, "报名进行中"),
	/**
	 * 报名未开始
	 */
	UNSIGNUP(3, "报名未开始"),
	/**
	 * 活动未开始
	 */
	UNACTIVITY(4, "活动未开始"),
	/**
	 * 已售罄
	 */
	ALREADY_SOLD_OUT(5, "已售罄"),
	/**
	 * 活动终止
	 */
	ACTIVITY_TERMINATION(6, "活动终止"),
	/**
	 * 活动结束
	 */
	ACTIVITY_END(7, "活动结束");

	private int code;
	private String label;

	CentralPurchasingActivityRealStatusEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}
	
	public static CentralPurchasingActivityRealStatusEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (CentralPurchasingActivityRealStatusEnum centralPurchasingActivityStatus : CentralPurchasingActivityRealStatusEnum.values()) {
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
