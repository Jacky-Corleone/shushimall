package com.camelot.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  支付订单记录表
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-2-27
 */
public class Transations implements Serializable {

	private static final long serialVersionUID = 6117230517352657501L;
	
	private Long id;
	private String outTradeNo;// 对外交易号
	private String orderNo;// 订单号
	private String subject;// 订单名称
	private String orderParentNo;// 父级订单号
	private Integer payOrderType;// 支付订单类型，0[父级]/1[子级]',
	private String payBank;// 支付渠道
	private String payType;// 支付方式		
	private BigDecimal orderAmount;// 订单金额(其中已经包含运费)
	private String seller;// 卖家名称
	private String toAccount;// 收款账号
	private String buyer;// 买家名称
	private String fromAccount;// 付款账号
	private String transactionNo;// 返回的支付渠道交易号
	private BigDecimal realAmount;// 返回的实际金额
	private Integer status;// 支付状态 0 未支付,1 支付中 ,2 支付成功 ,3 支付失败,4 支付异常  
	private String statusText;// 状态描述
	private Boolean enableFlag;// 有效标记
	private Date completedTime;// 交易完成时间
	private Date createdTime;	//创建日期
	private Date updatedTime;		//修改日期
	
	private String bankDiscount;	//推荐支付的折扣
	private String discountAmount;	//折扣金额
	private Date launchPayDate;	//同一银行发起支付日期
	private Integer launchPayTimes;	//同一银行发起支付次数
	private Date callBankResultDate;	//对账银行支付结果日期
	private Integer callBankResultTimes;	//对账银行支付结果次数
	
	private String deliveryType;// 配送方式
	private String orderDetails;// 订单信息
	private String dealFlag;// 回传订单处理标记
	private String payMediumCode;// 支付介质码
	
	private Integer isCredit;//银行卡类型；1信用卡0储蓄卡
	private String rate;	//汇率
	
	private boolean isIgnore =false ; // 是否跳过对银行结果处理
	
	private Integer platformId;//平台ID
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOrderParentNo() {
		return orderParentNo;
	}

	public void setOrderParentNo(String orderParentNo) {
		this.orderParentNo = orderParentNo;
	}

	public Integer getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(Integer payOrderType) {
		this.payOrderType = payOrderType;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
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

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getBankDiscount() {
		return bankDiscount;
	}

	public void setBankDiscount(String bankDiscount) {
		this.bankDiscount = bankDiscount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Date getLaunchPayDate() {
		return launchPayDate;
	}

	public void setLaunchPayDate(Date launchPayDate) {
		this.launchPayDate = launchPayDate;
	}

	public Integer getLaunchPayTimes() {
		return launchPayTimes;
	}

	public void setLaunchPayTimes(Integer launchPayTimes) {
		this.launchPayTimes = launchPayTimes;
	}

	public Date getCallBankResultDate() {
		return callBankResultDate;
	}

	public void setCallBankResultDate(Date callBankResultDate) {
		this.callBankResultDate = callBankResultDate;
	}

	public Integer getCallBankResultTimes() {
		return callBankResultTimes;
	}

	public void setCallBankResultTimes(Integer callBankResultTimes) {
		this.callBankResultTimes = callBankResultTimes;
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

	public Integer getIsCredit() {
		return isCredit;
	}

	public void setIsCredit(Integer isCredit) {
		this.isCredit = isCredit;
	}

	public String getPayMediumCode() {
		return payMediumCode;
	}

	public void setPayMediumCode(String payMediumCode) {
		this.payMediumCode = payMediumCode;
	}

	/** 
	  * 忽略对银行结果的处理（手机支付宝）
	  **/ 
	public boolean isIgnore() {
		return isIgnore;
	}

	public void setIgnore(boolean isIgnore) {
		this.isIgnore = isIgnore;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

}
