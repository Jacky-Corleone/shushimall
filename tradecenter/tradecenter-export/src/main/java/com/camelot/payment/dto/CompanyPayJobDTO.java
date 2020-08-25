package com.camelot.payment.dto;

import java.io.Serializable;

import com.camelot.common.enums.CiticEnums.AccountType;
/**
 * 企业账户金额不足，充值并支付记录JOB DTO
 * 
 * @author
 * 
 */
public class CompanyPayJobDTO  implements Serializable {
    
	private static final long serialVersionUID = -6910851270851498197L;
	private java.lang.Long id;//  id
	private java.lang.String outTradeNo;//  待执行的对外交易号
	private java.lang.String orderNo;//  待执行的订单号
	private java.lang.Long userId;//  用户ID
	private AccountType accType;//  账户类型 21：买家支付账户 22买家融资账户 31卖家收款账户 32卖家冻结账户
	private java.lang.Integer batch; // 批次
	
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
		return orderNo;
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
	public AccountType getAccType() {
		return accType;
	}
	public void setAccType(AccountType accType) {
		this.accType = accType;
	}
	public java.lang.Integer getBatch() {
		return batch;
	}
	public void setBatch(java.lang.Integer batch) {
		this.batch = batch;
	}
	
}

