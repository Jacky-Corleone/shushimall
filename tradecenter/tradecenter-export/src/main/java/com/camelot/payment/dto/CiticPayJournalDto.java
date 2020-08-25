package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 中信支付记录DTO
 * 
 * @author - learrings
 * 
 */
public class CiticPayJournalDto  implements Serializable {
    
	private static final long serialVersionUID = 6580178765480234695L;
	private java.lang.Long id;//  id
	private String outTradeNo;// 请求支付的对外交易号
	private java.lang.String orderParentTradeNo;//  父级对外交易号
	private java.lang.String payAccNo;//  付款账号
	private java.lang.String tranType;//  转账类型 "BF"：转账；"BG"：解冻；"BH"：解冻支付
	private java.lang.String recvAccNo;//  收款账号
	private java.lang.String recvAccNm;//  收款账户名称
	private BigDecimal tranAmt;//  交易金额
	private java.lang.String memo;//  摘要
	private java.lang.Integer payWay;//  交易方式 0父单交易/1子单交易
	private java.lang.Integer status;//  交易状态 0初始化/1交易失败/2交易成功
	private java.lang.Integer payType;//  交易类型  0支付/1结算
	private java.lang.Integer enableFlag;//  是否有效 0无效/1有效
	private java.lang.String dealFlag;//  处理标记 
	private java.lang.String dealText;//  查询处理结果【备注：0 成功 1 失败 2未知 3审核拒绝 4 用户撤销】
	private java.util.Date created;//  创建时间
	private java.util.Date modified;//  修改时间
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public java.lang.String getOrderParentTradeNo() {
		return orderParentTradeNo;
	}
	public void setOrderParentTradeNo(java.lang.String orderParentTradeNo) {
		this.orderParentTradeNo = orderParentTradeNo;
	}
	public java.lang.String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(java.lang.String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public java.lang.String getTranType() {
		return tranType;
	}
	public void setTranType(java.lang.String tranType) {
		this.tranType = tranType;
	}
	public java.lang.String getRecvAccNo() {
		return recvAccNo;
	}
	public void setRecvAccNo(java.lang.String recvAccNo) {
		this.recvAccNo = recvAccNo;
	}
	public java.lang.String getRecvAccNm() {
		return recvAccNm;
	}
	public void setRecvAccNm(java.lang.String recvAccNm) {
		this.recvAccNm = recvAccNm;
	}
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}
	public java.lang.String getMemo() {
		return memo;
	}
	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}
	public java.lang.Integer getPayWay() {
		return payWay;
	}
	public void setPayWay(java.lang.Integer payWay) {
		this.payWay = payWay;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.Integer getPayType() {
		return payType;
	}
	public void setPayType(java.lang.Integer payType) {
		this.payType = payType;
	}
	public java.lang.Integer getEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(java.lang.Integer enableFlag) {
		this.enableFlag = enableFlag;
	}
	public java.lang.String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(java.lang.String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public java.lang.String getDealText() {
		return dealText;
	}
	public void setDealText(java.lang.String dealText) {
		this.dealText = dealText;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getModified() {
		return modified;
	}
	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}
	
}

