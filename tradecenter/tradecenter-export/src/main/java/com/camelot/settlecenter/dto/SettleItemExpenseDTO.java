package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * <p>Description: [商品费用表]</p>
 * Created on 2015-4-22
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SettleItemExpenseDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long itemId;//  itemID
		private java.lang.Long shopId;//  店铺ID
		private java.lang.Long sellerId;//  用户ID
		private BigDecimal rebateRate;//  返点率
		private BigDecimal serviceFee;//  技术服务费
		private BigDecimal cashDeposit;//  保证金
		private java.util.Date created;//  创建时间
		private java.util.Date modified;//  更新时间
	
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	
	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}
	
	public BigDecimal getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(BigDecimal rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public BigDecimal getCashDeposit() {
		return cashDeposit;
	}

	public void setCashDeposit(BigDecimal cashDeposit) {
		this.cashDeposit = cashDeposit;
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

