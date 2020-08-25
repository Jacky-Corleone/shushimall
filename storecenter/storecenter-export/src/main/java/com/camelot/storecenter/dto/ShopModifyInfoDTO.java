package com.camelot.storecenter.dto;

import java.io.Serializable;
/**
 * 
 * @author
 * 
 */
public class ShopModifyInfoDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long shopId;//  店铺id
		private Long[] shopIds; //店铺ID组
		private java.lang.Long applicantUserid;//  申请人id
		private Integer applyStatus;//  申请所处状态 0, 新建(待审核); 1, 审核通过 2, 审核不通过;
		private java.util.Date createTime;//  申请创建时间
	
	
		
		
	public Long[] getShopIds() {
			return shopIds;
		}

		public void setShopIds(Long[] shopIds) {
			this.shopIds = shopIds;
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
	
	public java.lang.Long getApplicantUserid() {
		return this.applicantUserid;
	}

	public void setApplicantUserid(java.lang.Long value) {
		this.applicantUserid = value;
	}
	
	public Integer getApplyStatus() {
		return this.applyStatus;
	}

	public void setApplyStatus(Integer value) {
		this.applyStatus = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

}

