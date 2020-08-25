package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author
 * 
 */
public class PromotionFullReduction  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private Long id; //主键
		private Long promotionInfoId;//活动ID
		private java.lang.Long itemId;//  商品id
		private java.lang.Long skuId;//  sku_id
		private BigDecimal meetPrice;//  满足金额
		private BigDecimal discountPrice;//  优惠金额
		private java.util.Date createTime;//  创建时间
		private java.util.Date updateTime;//  更新时间
		
		private String itemName;//商品名称
	
		
		
		
	
	public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

	public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getPromotionInfoId() {
			return promotionInfoId;
		}

		public void setPromotionInfoId(Long promotionInfoId) {
			this.promotionInfoId = promotionInfoId;
		}

	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}
	
	public java.lang.Long getSkuId() {
		return this.skuId;
	}

	public void setSkuId(java.lang.Long value) {
		this.skuId = value;
	}
	
	public BigDecimal getMeetPrice() {
		return meetPrice;
	}

	public void setMeetPrice(BigDecimal meetPrice) {
		this.meetPrice = meetPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
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
	


}

