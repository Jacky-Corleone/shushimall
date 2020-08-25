package com.camelot.tradecenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>
 * Description: [交易价格历史记录]
 * </p>
 * Created on 2015-4-14
 * 
 * @author wangcs
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class TradeOrderPriceHistory implements Serializable {

	private static final long serialVersionUID = 3699657068137674634L;

	private Long id;
	private String orderId;
	private Long sellerId;
	private Long buyerId;
	private Long shopId;
	private BigDecimal beforeTotalPrice;// 修改前优惠金额
	private BigDecimal beforeFreight;// 修改前运费
	private BigDecimal beforeTotalDiscount;// 修改前总优惠金额
	private BigDecimal beforePaymentPrice;// 修改前实际支付金额
	private BigDecimal afterTotalPrice;// 修改后总金额
	private BigDecimal afterFreight;// 修改后运费
	private BigDecimal afterTotalDiscount;// 修改后优惠总金额
	private BigDecimal afterPaymentPrice;// 修改实际支付金额
	private Long operationUser;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getBeforeTotalPrice() {
		return beforeTotalPrice;
	}

	public void setBeforeTotalPrice(BigDecimal beforeTotalPrice) {
		this.beforeTotalPrice = beforeTotalPrice;
	}

	public BigDecimal getBeforeFreight() {
		return beforeFreight;
	}

	public void setBeforeFreight(BigDecimal beforeFreight) {
		this.beforeFreight = beforeFreight;
	}

	public BigDecimal getBeforeTotalDiscount() {
		return beforeTotalDiscount;
	}

	public void setBeforeTotalDiscount(BigDecimal beforeTotalDiscount) {
		this.beforeTotalDiscount = beforeTotalDiscount;
	}

	public BigDecimal getBeforePaymentPrice() {
		return beforePaymentPrice;
	}

	public void setBeforePaymentPrice(BigDecimal beforePaymentPrice) {
		this.beforePaymentPrice = beforePaymentPrice;
	}

	public BigDecimal getAfterTotalPrice() {
		return afterTotalPrice;
	}

	public void setAfterTotalPrice(BigDecimal afterTotalPrice) {
		this.afterTotalPrice = afterTotalPrice;
	}

	public BigDecimal getAfterFreight() {
		return afterFreight;
	}

	public void setAfterFreight(BigDecimal afterFreight) {
		this.afterFreight = afterFreight;
	}

	public BigDecimal getAfterTotalDiscount() {
		return afterTotalDiscount;
	}

	public void setAfterTotalDiscount(BigDecimal afterTotalDiscount) {
		this.afterTotalDiscount = afterTotalDiscount;
	}

	public BigDecimal getAfterPaymentPrice() {
		return afterPaymentPrice;
	}

	public void setAfterPaymentPrice(BigDecimal afterPaymentPrice) {
		this.afterPaymentPrice = afterPaymentPrice;
	}

	public Long getOperationUser() {
		return operationUser;
	}

	public void setOperationUser(Long operationUser) {
		this.operationUser = operationUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
