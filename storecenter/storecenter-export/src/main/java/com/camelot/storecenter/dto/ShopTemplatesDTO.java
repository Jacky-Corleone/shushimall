package com.camelot.storecenter.dto;

import java.io.Serializable;
/**
 * 
 * @author
 * 
 */
public class ShopTemplatesDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long shopId;//  店铺id
		private java.lang.String templatesName;//  模板名称
		private Integer status;//  状态 1：使用中，2：未使用
		private java.lang.String templatesInfo;//  模板说明
		private java.lang.String  color;//颜色 
		private java.util.Date created;//  创建日期
		private java.util.Date modified;//  修改日期
	
	
	public java.lang.String getColor() {
			return color;
		}
	public void setColor(java.lang.String color) {
			this.color = color;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	
	public java.lang.String getTemplatesName() {
		return this.templatesName;
	}

	public void setTemplatesName(java.lang.String value) {
		this.templatesName = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public java.lang.String getTemplatesInfo() {
		return this.templatesInfo;
	}

	public void setTemplatesInfo(java.lang.String value) {
		this.templatesInfo = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

}

