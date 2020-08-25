package com.camelot.usercenter.dto;

import java.io.Serializable;
/**
 * 
 * @author
 * 
 */
public class UserPermissionDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Integer resourceId;//  商城资源id
		private Integer[] resourceIds;//  商城资源id组
		private java.lang.Long userId;//  用户id
		private java.lang.String userName;//  用户名
		private Integer userType;//  用户权限类型（1：平台权限，2：店铺权限）
		private java.lang.Long shopId;//  店铺编号（-1为平台）
		private java.util.Date created;//  创建时间
		
		private java.lang.Integer modularType;//模块类型 ：1买家中心 2卖家中心  
	
	
		


	public java.lang.Integer getModularType() {
			return modularType;
		}

		public void setModularType(java.lang.Integer modularType) {
			this.modularType = modularType;
		}

	public Integer[] getResourceIds() {
			return resourceIds;
		}

		public void setResourceIds(Integer[] resourceIds) {
			this.resourceIds = resourceIds;
		}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Integer getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(java.lang.Integer value) {
		this.resourceId = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.String getUserName() {
		return this.userName;
	}

	public void setUserName(java.lang.String value) {
		this.userName = value;
	}
	
	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer value) {
		this.userType = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

}

