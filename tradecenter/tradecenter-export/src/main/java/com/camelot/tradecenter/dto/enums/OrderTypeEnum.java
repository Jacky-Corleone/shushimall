package com.camelot.tradecenter.dto.enums;
/**
 * 
 * <p>Description: [订单类型枚举类： 0询价订单 1协议订单 2正常]</p>
 * Created on 2015年11月12日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum OrderTypeEnum {
	/**
	 * 询价订单
	 */
	INQUIRY(0, "询价订单"), 
	/**
	 * 协议订单
	 */
	AGREEMENT(1, "协议订单"), 
	/**
	 * 正常订单
	 */
	NORMAL(2, "正常订单");

	private int code;
	private String label;

	OrderTypeEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}
}
