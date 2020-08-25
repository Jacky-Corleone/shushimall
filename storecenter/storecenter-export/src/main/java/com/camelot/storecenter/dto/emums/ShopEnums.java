package com.camelot.storecenter.dto.emums;


public class ShopEnums {
	
	
	/**
	 * 
	 * <p>Description: [店铺经营类目状态]</p>
	 * Created on 2015-3-11
	 * @author  yuht
	 * @version 1.0 
	 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	 */
	public enum ShopCategoryStatus {
		
		APPLY(1, "申请"), PASS(2, "通过"),REJECT(3,"驳回"),CLOSE(4,"关闭"), DELETED(-1, "删除");
		
		private int code;
		private String label;
		ShopCategoryStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}
		
		public static ShopCategoryStatus getEnumBycode(Integer code) {
			if (code != null) {
				for (ShopCategoryStatus userType : ShopCategoryStatus.values()) {
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

}
