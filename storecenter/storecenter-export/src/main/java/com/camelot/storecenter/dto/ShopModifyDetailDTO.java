package com.camelot.storecenter.dto;

import java.io.Serializable;
/**
 * 
 * @author
 * 
 */
public class ShopModifyDetailDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long modifyInfoId;//  关联modify_info表的id
		private java.lang.String propertiesColumn;//  申请修改的属性列名
		private java.lang.String beforeChange;//  修改前的内容
		private java.lang.String afterChange;//  修改后的内容
		private java.util.Date createTime;//  申请创建时间
		private java.lang.String reviewReason;//  审核理由
		private java.lang.Long reviewUserId;//  审核人id
		private java.util.Date reviewTime;//  审核时间
		
		
		private Long shopId;//店铺ID 不会返回参数
		
		
	
	
	public Long getShopId() {
			return shopId;
		}

		public void setShopId(Long shopId) {
			this.shopId = shopId;
		}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getModifyInfoId() {
		return this.modifyInfoId;
	}

	public void setModifyInfoId(java.lang.Long value) {
		this.modifyInfoId = value;
	}
	
	public java.lang.String getPropertiesColumn() {
		return this.propertiesColumn;
	}

	public void setPropertiesColumn(java.lang.String value) {
		this.propertiesColumn = value;
	}
	
	public java.lang.String getBeforeChange() {
		return this.beforeChange;
	}

	public void setBeforeChange(java.lang.String value) {
		this.beforeChange = value;
	}
	
	public java.lang.String getAfterChange() {
		return this.afterChange;
	}

	public void setAfterChange(java.lang.String value) {
		this.afterChange = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.lang.String getReviewReason() {
		return this.reviewReason;
	}

	public void setReviewReason(java.lang.String value) {
		this.reviewReason = value;
	}
	
	public java.lang.Long getReviewUserId() {
		return this.reviewUserId;
	}

	public void setReviewUserId(java.lang.Long value) {
		this.reviewUserId = value;
	}
	
	public java.util.Date getReviewTime() {
		return this.reviewTime;
	}

	public void setReviewTime(java.util.Date value) {
		this.reviewTime = value;
	}

}

