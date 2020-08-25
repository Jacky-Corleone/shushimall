package com.camelot.storecenter.dto;

import java.io.Serializable;

import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
/**
 * 
 * <p>Description: [店铺经营类目]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopCategoryDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private Long[]  shopIds;//入参用店铺ID组
		private java.lang.Long shopId;//  铺店id
		private java.lang.Long cid;//  平台类目id
		private java.lang.Long sellerId;//  卖家id
		private Integer status;//  类目状态;1是申请，2是通过，3是驳回，4是关闭，-1是删除
		private String statusLage; // 类目状态;1是申请，2是通过，3是驳回，4是关闭
		private java.lang.String comment;//  批注
		private java.lang.String platformUserId;//  批注人
		private java.util.Date created;//  创建时间
		private java.util.Date modified;//  更新时间
		private java.util.Date passTime;//  审核通过时间
		private Long isGroupBy;//是否根据店铺IDgroupBy
	
	
	public Long getIsGroupBy() {
			return isGroupBy;
		}

		public void setIsGroupBy(Long isGroupBy) {
			this.isGroupBy = isGroupBy;
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
	
	public java.lang.Long getCid() {
		return this.cid;
	}

	public void setCid(java.lang.Long value) {
		this.cid = value;
	}
	
	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public java.lang.String getComment() {
		return this.comment;
	}

	public void setComment(java.lang.String value) {
		this.comment = value;
	}
	public java.lang.String getPlatformUserId() {
		return platformUserId;
	}

	public void setPlatformUserId(java.lang.String platformUserId) {
		this.platformUserId = platformUserId;
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
	
	public java.util.Date getPassTime() {
		return this.passTime;
	}

	public void setPassTime(java.util.Date value) {
		this.passTime = value;
	}

	public String getStatusLage() {
		return statusLage;
	}

	public Long[] getShopIds() {
		return shopIds;
	}

	public void setShopIds(Long[] shopIds) {
		this.shopIds = shopIds;
	}

	public void setStatusLage(Integer status) {
		ShopCategoryStatus shopCategoryStatus = ShopCategoryStatus.getEnumBycode(status);
		if(shopCategoryStatus!=null){
			this.statusLage=shopCategoryStatus.getLabel();
		}
		this.status = status;
	}

	

}

