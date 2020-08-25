package com.camelot.payment.domain;

import java.io.Serializable;

import com.camelot.openplatform.common.PropertyMapping;
/**
 * 
 * @author
 * 
 */
public class CompanyPayJob  implements Serializable {
    
	private static final long serialVersionUID = -6910851270851498197L;
	@PropertyMapping("id")
	private java.lang.Long id;//  id
	@PropertyMapping("outTradeNo")
	private java.lang.String outTradeNo;//  待执行的对外交易号
	@PropertyMapping("orderNo")
	private java.lang.String orderNo;//  待执行的订单号
	@PropertyMapping("userId")
	private java.lang.Long userId;//  用户ID
	private java.lang.Integer accType;//  账户类型 21：买家支付账户 22买家融资账户 31卖家收款账户 32卖家冻结账户
	private java.lang.Integer enableFlag;//  有效性
	private java.lang.String dealFlag;//  处理标记
	@PropertyMapping("batch")
	private java.lang.Integer batch; // 批次
	private java.util.Date created;//  创建时间
	private java.util.Date midified;//  修改时间
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}
	
	public java.lang.String getOutTradeNo() {
		return this.outTradeNo;
	}

	public void setOutTradeNo(java.lang.String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}

	public java.lang.Long getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}

	public java.lang.Integer getAccType() {
		return accType;
	}

	public void setAccType(java.lang.Integer accType) {
		this.accType = accType;
	}

	public java.lang.Integer getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(java.lang.Integer enableFlag) {
		this.enableFlag = enableFlag;
	}

	public java.lang.String getDealFlag() {
		return this.dealFlag;
	}

	public void setDealFlag(java.lang.String dealFlag) {
		this.dealFlag = dealFlag;
	}
	
	public java.lang.Integer getBatch() {
		return batch;
	}

	public void setBatch(java.lang.Integer batch) {
		this.batch = batch;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	
	public java.util.Date getMidified() {
		return this.midified;
	}

	public void setMidified(java.util.Date midified) {
		this.midified = midified;
	}

}

