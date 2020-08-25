package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.Arrays;
/**
 * 
 * @author
 * 
 */
public class ShopBrandDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private Long[]  shopIds;//入参用店铺ID组
		private java.lang.Long shopId;//  铺店id
		private java.lang.Long brandId;//  品牌id
		private Integer status;//  店铺品牌状态：1为申请，2为通过，3为驳回，-1为删除
		private java.util.Date created;//  创建时间
		private java.util.Date modified;//  更新时间
		private java.lang.String operatorId;//  操作人
		private java.lang.String comment;//  回复
		private java.lang.Long sellerId;//  卖家id
		private java.lang.Long cid;//  三级类目id
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
	
	public java.lang.Long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(java.lang.Long value) {
		this.brandId = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
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
	

	
	public java.lang.String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(java.lang.String operatorId) {
		this.operatorId = operatorId;
	}

	public java.lang.String getComment() {
		return this.comment;
	}

	public void setComment(java.lang.String value) {
		this.comment = value;
	}
	
	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}
	
	public java.lang.Long getCid() {
		return this.cid;
	}

	public void setCid(java.lang.Long value) {
		this.cid = value;
	}

	public Long[] getShopIds() {
		return shopIds;
	}

	public void setShopIds(Long[] shopIds) {
		this.shopIds = shopIds;
	}

	@Override
	public String toString() {
		return "ShopBrandDTO [id=" + id + ", shopIds="
				+ Arrays.toString(shopIds) + ", shopId=" + shopId
				+ ", brandId=" + brandId + ", status=" + status + ", created="
				+ created + ", modified=" + modified + ", operatorId="
				+ operatorId + ", comment=" + comment + ", sellerId="
				+ sellerId + ", cid=" + cid + ", isGroupBy=" + isGroupBy + "]";
	}
}

