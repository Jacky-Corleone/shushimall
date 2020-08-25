package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.PayTypeEnum;

/**
 * 退款接收参数对象
 * 
 * @Description -
 * @author - zhouzj
 * @createDate - 2015-9-24
 */
public class RefundReqParam implements Serializable {

	private static final long serialVersionUID = -2425498182827442262L;

	private String outTradeNo;// 对外交易号
	private String outRefundNo;// 退款交易号
	private String transactionNo;// 返回的交易订单号
	private String codeNo;// 退款单号

	private String orderNo;// 订单号
	private BigDecimal totalAmount;// 交易金额 单位：0.01元
	private BigDecimal refundAmount;// 退款金额 单位：0.01元
	private String curType;// 币种

	private String batchNum;// 退款笔数，默认为1

	private String desc;//退款描述
	private String refundReason;// 退款原因

	private String payBank;// 支付渠道 支付宝、网银等等
	private String id;//退款操作人id
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getCodeNo() {
		return codeNo;
	}

	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
