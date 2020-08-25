package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.camelot.common.enums.PayOrderTypeEnum;

/**
 * 交易流程控制VO
 * 
 */
public class OrderInfoPay  implements Serializable{
	
	private static final long serialVersionUID = 8006442418561190816L;
	
	private String outTradeNo;//对外交易号
	private String orderNo;//订单号
	private String orderParentNo;//父级订单号
	private String subject;//订单名称
	private String payBank;//支付渠道
	private BigDecimal orderAmount;//订单金额
	private String seller;//卖家
	private String toAccount;//收款账号
	private String buyer;//买家
	private String fromAccount;//付款账号
	private Integer status;//状态
	private Date completedTime;//交易完成时间
	private Date createdTime;
	
	private String deliveryType;//配送方式
	private String orderDetails;//订单信息
	private String dealFlag;//回传订单处理标记
	
	private PayOrderTypeEnum payOrderType=PayOrderTypeEnum.Parent;// 支付订单类型，默认父级订单
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderParentNo() {
		return orderParentNo;
	}
	public void setOrderParentNo(String orderParentNo) {
		this.orderParentNo = orderParentNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPayBank() {
		return payBank;
	}
	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCompletedTime() {
		return completedTime;
	}
	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public PayOrderTypeEnum getPayOrderType() {
		return payOrderType;
	}
	public void setPayOrderType(PayOrderTypeEnum payOrderType) {
		this.payOrderType = payOrderType;
	}
	
}
