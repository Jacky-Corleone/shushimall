package com.camelot.goodscenter.dto.enums;

/**
 * 
 * <p>Description: [商品来源]</p>
 * Created on 2016年2月17日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public enum ItemAddSourceEnum{
	
	/**
	 * 普通商品
	 */
	NORMAL(1, "普通商品"), 
	/**
	 * 平台上传
	 */
	PLATFORM(2, "平台上传"), 
	/**
	 * 套装商品
	 */
	COMBINATION(3, "套装商品"),
	/**
	 * 基础服务商品
	 */
	BASICSERVICE(4,"基础服务商品"),
	/**
	 * 增值服务商品
	 */
	VALUEADDEDSERVICE(5,"增值服务商品"),
	/**
	 * 辅助材料商品
	 */
	AUXILIARYMATERIAL(6,"辅助材料商品");

	private int code;
	private String label;

	ItemAddSourceEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ItemAddSourceEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (ItemAddSourceEnum itemAddSource : ItemAddSourceEnum.values()) {
				if (itemAddSource.getCode() == code) {
					return itemAddSource;
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
