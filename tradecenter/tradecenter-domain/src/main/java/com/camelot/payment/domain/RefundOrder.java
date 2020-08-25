package com.camelot.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author
 * 
 */
public class RefundOrder implements Serializable {

	private static final long serialVersionUID = 4622504080222405870L;
	private java.lang.Long id;// id
	private java.lang.String refundNo;// 退款单据号
	private java.lang.String orderNo;// 订单编号
	private java.lang.Long itemId;//商品ID
	private java.lang.Long skuId;//skuId
	private BigDecimal orderPrice;//父订单订单金额，此金额包含运费
	private java.lang.String orderLabel;// 订单名称
	private java.lang.String outTradeNo;// 对外交易号
	private java.lang.String transactionNo;// 支付回传交易号
	private java.lang.Long buyerId;// 买家ID
	private java.lang.String buyerName;// 买家名称
	private java.lang.String buyerPhone;// 买家电话
	private BigDecimal refundAmount;// 退款金额
	private java.lang.String refundReason;// 买家申请退款原因
	private java.util.Date refundTime;// 退款时间
	private java.lang.String refundRemark;// 审核退款备注
	private java.lang.String buyAccountNo;// 买家账户
	private java.lang.String buyAccountName;// 买家账户名称
	private java.lang.String buyBankName;// 买家开户行名称
	private java.lang.String bankBranchJointLine;// 买家开户行联号
	private java.lang.Integer sameBank;// 是否是中信银行
	private java.lang.String orderPayBank;// 订单支付银行
	private java.lang.String reproNo;// 退货单号
	private java.lang.String reproLabel;// 退货单名称
	private java.lang.Integer status;// 状态 1初始化21.卖家退款成功，未退佣金22.退款完成3.退款失败
	private java.util.Date created;// 创建时间
	private java.lang.Long modifiedId;// 修改人
	private java.util.Date modified;// 修改时间
	private Long[] reproNos;

	public Long[] getReproNos() {
		return reproNos;
	}

	public void setReproNos(Long[] reproNos) {
		this.reproNos = reproNos;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getRefundNo() {
		return this.refundNo;
	}

	public void setRefundNo(java.lang.String refundNo) {
		this.refundNo = refundNo;
	}

	public java.lang.String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}

	public java.lang.String getOrderLabel() {
		return this.orderLabel;
	}

	public void setOrderLabel(java.lang.String orderLabel) {
		this.orderLabel = orderLabel;
	}

	public java.lang.String getOutTradeNo() {
		return this.outTradeNo;
	}

	public void setOutTradeNo(java.lang.String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public java.lang.String getTransactionNo() {
		return this.transactionNo;
	}

	public void setTransactionNo(java.lang.String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public java.lang.Long getBuyerId() {
		return this.buyerId;
	}

	public void setBuyerId(java.lang.Long buyerId) {
		this.buyerId = buyerId;
	}

	public java.lang.String getBuyerName() {
		return this.buyerName;
	}

	public void setBuyerName(java.lang.String buyerName) {
		this.buyerName = buyerName;
	}

	public java.lang.String getBuyerPhone() {
		return this.buyerPhone;
	}

	public void setBuyerPhone(java.lang.String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public BigDecimal getRefundAmount() {
		return this.refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public java.lang.String getRefundReason() {
		return this.refundReason;
	}

	public void setRefundReason(java.lang.String refundReason) {
		this.refundReason = refundReason;
	}

	public java.util.Date getRefundTime() {
		return this.refundTime;
	}

	public void setRefundTime(java.util.Date refundTime) {
		this.refundTime = refundTime;
	}

	public java.lang.String getRefundRemark() {
		return this.refundRemark;
	}

	public void setRefundRemark(java.lang.String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public java.lang.String getBuyAccountNo() {
		return this.buyAccountNo;
	}

	public void setBuyAccountNo(java.lang.String buyAccountNo) {
		this.buyAccountNo = buyAccountNo;
	}

	public java.lang.String getBuyAccountName() {
		return this.buyAccountName;
	}

	public void setBuyAccountName(java.lang.String buyAccountName) {
		this.buyAccountName = buyAccountName;
	}

	public java.lang.String getBuyBankName() {
		return this.buyBankName;
	}

	public void setBuyBankName(java.lang.String buyBankName) {
		this.buyBankName = buyBankName;
	}

	public java.lang.String getBankBranchJointLine() {
		return this.bankBranchJointLine;
	}

	public void setBankBranchJointLine(java.lang.String bankBranchJointLine) {
		this.bankBranchJointLine = bankBranchJointLine;
	}

	public java.lang.Integer getSameBank() {
		return this.sameBank;
	}

	public void setSameBank(java.lang.Integer sameBank) {
		this.sameBank = sameBank;
	}

	public java.lang.String getOrderPayBank() {
		return orderPayBank;
	}

	public void setOrderPayBank(java.lang.String orderPayBank) {
		this.orderPayBank = orderPayBank;
	}

	public java.lang.String getReproNo() {
		return this.reproNo;
	}

	public void setReproNo(java.lang.String reproNo) {
		this.reproNo = reproNo;
	}

	public java.lang.String getReproLabel() {
		return this.reproLabel;
	}

	public void setReproLabel(java.lang.String reproLabel) {
		this.reproLabel = reproLabel;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public java.lang.Long getModifiedId() {
		return this.modifiedId;
	}

	public void setModifiedId(java.lang.Long modifiedId) {
		this.modifiedId = modifiedId;
	}

	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}

	public java.lang.Long getItemId() {
		return itemId;
	}

	public void setItemId(java.lang.Long itemId) {
		this.itemId = itemId;
	}

	public java.lang.Long getSkuId() {
		return skuId;
	}

	public void setSkuId(java.lang.Long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

}
