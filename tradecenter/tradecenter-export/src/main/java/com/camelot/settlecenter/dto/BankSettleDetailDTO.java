package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author
 * 
 */
public class BankSettleDetailDTO  implements Serializable {

	private static final long serialVersionUID = -6889225139764739085L;
	private java.lang.Long id;//  id
	
	private java.lang.String outTradeNo;//  对外交易编号
	private BigDecimal orderAmount;//  订单金额
	private BigDecimal factorage=new BigDecimal("0.00");//  手续费
	private java.lang.Integer orderStatus=2;//  订单状态 1.失败，2成功
	private java.lang.Integer liquidateStatus=2;//  清算状态 1.未清算 2.已清算
	private java.util.Date completedTime;//  交易完成时间
	private java.util.Date callBankTime;//  银行返回时间
	private java.lang.String bankType;//  导入银行 CB[网银在线] ，AP[支付宝] ，WX[微信]
	
	private java.lang.String dealFlag;//  处理标记 1 未处理 空 已处理
	private java.lang.String remark1;//  备注1
	private java.lang.String remark2;//  备注2
	private java.lang.String createId;//  导入用户ID
	private java.util.Date created;//  创建时间
	
	private java.util.Date comletedTimeStart;//交易完成时间开始，用于条件查询
	private java.util.Date comletedTimeEnd;//交易完成时间，用于条件查询
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(java.lang.String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public BigDecimal getOrderAmount() {
		return this.orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getFactorage() {
		return this.factorage;
	}
	public void setFactorage(BigDecimal factorage) {
		this.factorage = factorage;
	}
	public java.lang.Integer getOrderStatus() {
		return this.orderStatus;
	}
	public void setOrderStatus(java.lang.Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public java.lang.Integer getLiquidateStatus() {
		return this.liquidateStatus;
	}
	public void setLiquidateStatus(java.lang.Integer liquidateStatus) {
		this.liquidateStatus = liquidateStatus;
	}
	public java.util.Date getCompletedTime() {
		return this.completedTime;
	}
	public void setCompletedTime(java.util.Date completedTime) {
		this.completedTime = completedTime;
	}
	public java.util.Date getCallBankTime() {
		return this.callBankTime;
	}
	public void setCallBankTime(java.util.Date callBankTime) {
		this.callBankTime = callBankTime;
	}
	public java.lang.String getBankType() {
		return this.bankType;
	}
	public void setBankType(java.lang.String bankType) {
		this.bankType = bankType;
	}
	public java.lang.String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(java.lang.String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public java.lang.String getRemark1() {
		return this.remark1;
	}
	public void setRemark1(java.lang.String remark1) {
		this.remark1 = remark1;
	}
	public java.lang.String getRemark2() {
		return this.remark2;
	}
	public void setRemark2(java.lang.String remark2) {
		this.remark2 = remark2;
	}
	public java.lang.String getCreateId() {
		return this.createId;
	}
	public void setCreateId(java.lang.String createId) {
		this.createId = createId;
	}
	public java.util.Date getCreated() {
		return this.created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getComletedTimeStart() {
		return comletedTimeStart;
	}
	public void setComletedTimeStart(java.util.Date comletedTimeStart) {
		this.comletedTimeStart = comletedTimeStart;
	}
	public java.util.Date getComletedTimeEnd() {
		return comletedTimeEnd;
	}
	public void setComletedTimeEnd(java.util.Date comletedTimeEnd) {
		this.comletedTimeEnd = comletedTimeEnd;
	}

}

