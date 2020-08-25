package com.camelot.goodscenter.dto.enums;

/**
 * 
 * <p>Description: [
 * 商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁， 30： 删除]</p>
 * Created on 2015-3-13
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum ItemStatusEnum{
	
	NOT_PUBLISH(1, "未发布"), AUDITING(2, "待审核"), SHELVING(3, "待上架"), SALING(4, "在售"), UNSHELVED(5, "已下架"), LOCKED(6, "锁定"), APPLYING(
			7, "申请解锁"), REJECTED(20, "审核驳回"), DELETED(30, "删除");
	
	private int code;
	private String label;

	ItemStatusEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ItemStatusEnum getEnumBycode(Integer code) {
		if (code != null) {
			for (ItemStatusEnum userType : ItemStatusEnum.values()) {
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
