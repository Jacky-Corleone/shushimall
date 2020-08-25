package com.camelot.goodscenter.dto.enums;

/**
 * 
 * <p>Description: [
 * 商品产品库状态枚举
 * 1：未符合待入库2：待入库3：已入库4：删除]</p>
 * Created on 2015-3-13
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum ItemPlatLinkStatusEnum{
	
	SAVED(1,"未符合待入库"), STORING(2,"待入库"),STORED(3,"已入库"),DELETED(4,"删除");
	
	private int code;
	private String label;

	ItemPlatLinkStatusEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ItemPlatLinkStatusEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (ItemPlatLinkStatusEnum userType : ItemPlatLinkStatusEnum.values()) {
				if (userType.getCode() == code) {
					return userType;
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
