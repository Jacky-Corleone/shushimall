package com.camelot.mall.bank;

import java.math.BigDecimal;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月18日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class BankOrder {
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 订单名称
	 */
	private String subject;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 支付币种
	 */
	private String curType;
	
	/**
	 * 收款账号
	 */
	private String toAccount;
	
	/**
	 * 卖家名称
	 */
	private String seller;
	
	/**
	 * 对外订单号
	 */
	private String outTradeNo;
	
	/**
	 * 买家帐号
	 */
	private String fromCount;
	
	/**
	 * 支付密码
	 */
	private String payPassword;
	
	private BigDecimal totalFee;
	
	private Long buyer;
	
	private BigDecimal balance;

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getFromCount() {
		return fromCount;
	}

	public void setFromCount(String fromCount) {
		this.fromCount = fromCount;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public Long getBuyer() {
		return buyer;
	}

	public void setBuyer(Long buyer) {
		this.buyer = buyer;
	}

}
