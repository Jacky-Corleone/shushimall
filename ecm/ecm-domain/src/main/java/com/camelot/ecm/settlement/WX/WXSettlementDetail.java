package com.camelot.ecm.settlement.WX;

import com.camelot.common.ExcelField;


public class WXSettlementDetail {
	
	private String billTime;//交易时间
	private String appId;//公众账号ID
	private String mchId;//商户号
	private String speciallyMchId;//特约商户号
	private String deviceInfo;//设备号
	private String tradeNo;//微信订单号
	private String outTradeNo;//商户订单号
	private String usrFlag;//用户标识
	private String tradeType;//交易类型
	private String orderStauts;//交易状态
	private String payBank;//付款银行
	private String feeType;//货币种类
	private String totalAmount;//总金额
	private String redPacketAmount;//企业红包金额
	private String wxRefundNo;//微信退款单号
	private String mchRefundNo;//商户退款单号
	private String refundFeeAmount;//退款金额
	private String redPacketRefundFeeAmount;//退款金额
	private String refundType;//退款类型
	private String refundStatus;//退款类型
	private String itemName;//商品名称
	private String mchData;//商户数据包
	private String factorage;//手续费
	private String rate;//费率
	private String liquidateStatusString;//清算状态
	@ExcelField(title = "交易时间",type=0,sort=10)
	public String getBillTime() {
		return billTime;
	}
	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}
	@ExcelField(title = "公众账号ID",type=0,sort=20)
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@ExcelField(title = "商户号",type=0,sort=30)
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	@ExcelField(title = "特约商户号",type=0,sort=40)
	public String getSpeciallyMchId() {
		return speciallyMchId;
	}
	public void setSpeciallyMchId(String speciallyMchId) {
		this.speciallyMchId = speciallyMchId;
	}
	@ExcelField(title = "设备号",type=0,sort=50)
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	@ExcelField(title = "微信订单号",type=0,sort=60)
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	@ExcelField(title = "商户订单号",type=0,sort=70)
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	@ExcelField(title = "用户标识",type=0,sort=80)
	public String getUsrFlag() {
		return usrFlag;
	}
	public void setUsrFlag(String usrFlag) {
		this.usrFlag = usrFlag;
	}
	@ExcelField(title = "交易类型",type=0,sort=90)
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	@ExcelField(title = "交易状态",type=0,sort=100)
	public String getOrderStauts() {
		return orderStauts;
	}
	public void setOrderStauts(String orderStauts) {
		this.orderStauts = orderStauts;
	}
	@ExcelField(title = "付款银行",type=0,sort=110)
	public String getPayBank() {
		return payBank;
	}
	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}
	@ExcelField(title = "货币种类",type=0,sort=120)
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	@ExcelField(title = "总金额",type=0,sort=130)
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRedPacketAmount() {
		return redPacketAmount;
	}
	@ExcelField(title = "企业红包金额",type=0,sort=140)
	public void setRedPacketAmount(String redPacketAmount) {
		this.redPacketAmount = redPacketAmount;
	}
	@ExcelField(title = "微信退款单号",type=0,sort=150)
	public String getWxRefundNo() {
		return wxRefundNo;
	}
	public void setWxRefundNo(String wxRefundNo) {
		this.wxRefundNo = wxRefundNo;
	}
	@ExcelField(title = "商户退款单号",type=0,sort=160)
	public String getMchRefundNo() {
		return mchRefundNo;
	}
	public void setMchRefundNo(String mchRefundNo) {
		this.mchRefundNo = mchRefundNo;
	}
	@ExcelField(title = "退款金额",type=0,sort=170)
	public String getRefundFeeAmount() {
		return refundFeeAmount;
	}
	public void setRefundFeeAmount(String refundFeeAmount) {
		this.refundFeeAmount = refundFeeAmount;
	}
	@ExcelField(title = "企业红包退款金额",type=0,sort=180)
	public String getRedPacketRefundFeeAmount() {
		return redPacketRefundFeeAmount;
	}
	public void setRedPacketRefundFeeAmount(String redPacketRefundFeeAmount) {
		this.redPacketRefundFeeAmount = redPacketRefundFeeAmount;
	}
	@ExcelField(title = "退款类型",type=0,sort=190)
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	@ExcelField(title = "退款状态",type=0,sort=200)
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	@ExcelField(title = "商品名称",type=0,sort=210)
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@ExcelField(title = "商户数据包",type=0,sort=220)
	public String getMchData() {
		return mchData;
	}
	public void setMchData(String mchData) {
		this.mchData = mchData;
	}
	@ExcelField(title = "手续费",type=0,sort=230)
	public String getFactorage() {
		return factorage;
	}
	public void setFactorage(String factorage) {
		this.factorage = factorage;
	}
	@ExcelField(title = "费率",type=0,sort=240)
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	@ExcelField(title = "清算状态",type=0,sort=250)
	public String getLiquidateStatusString() {
		return liquidateStatusString;
	}
	public void setLiquidateStatusString(String liquidateStatusString) {
		this.liquidateStatusString = liquidateStatusString;
	}
	
}
