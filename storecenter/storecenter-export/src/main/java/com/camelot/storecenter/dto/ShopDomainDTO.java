package com.camelot.storecenter.dto;

import java.io.Serializable;
/**
 * 
 * <p>Description: [店铺自定义域名dto]</p>
 * Created on 2015-3-11
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopDomainDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long sellerId;//  卖家id
		private java.lang.Long shopId;//  店铺id
		private java.lang.String domain;//  自定义域名
		private java.lang.Integer state;//  自定义域名状态(1、待审核；2、驳回；3、审核通过)
		private java.util.Date createTime;//  创建时间
		private java.util.Date updateTime;//  更新时间
		private java.lang.String reviewer;//  审核人
	
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	

	
	public java.lang.String getDomain() {
		return domain;
	}

	public void setDomain(java.lang.String domain) {
		this.domain = domain;
	}

	public java.lang.Integer getState() {
		return this.state;
	}

	public void setState(java.lang.Integer value) {
		this.state = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.lang.String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(java.lang.String value) {
		this.reviewer = value;
	}

}

