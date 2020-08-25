package com.camelot.ecm.settlement.alipay;

import com.camelot.common.ExcelField;

public class AlipaySettlementDetail {
	
	private String no;//序号
	private String billTime;//入账时间
	private String tradeNo;//支付宝交易号
	private String journalNo;//支付宝流水号
	private String outTradeNo;//订单对外交易号
	private String financeType;//财务类型
	private String income;//收入
	private String pay;//支出
	private String accountBalance;//账户余额
	private String factorage;//手续费
	private String payType;//支付渠道
	private String signProduct;//签约产品
	private String payAccountNumber;//对方账号
	private String payAccountName;//对方账户名
	private String bankOrderNo;//银行订单号
	private String itemName;//商品名称
	private String remark;//备注
	private String liquidateStatusString;//清算状态
	private String orderStats;//订单状态
	
	@ExcelField(title = "序号",type=0,sort=10)
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@ExcelField(title = "入账时间",type=0,sort=20)
	public String getBillTime() {
		return billTime;
	}
	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}
	@ExcelField(title = "支付宝交易号",type=0,sort=30)
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	@ExcelField(title = "支付宝流水号",type=0,sort=40)
	public String getJournalNo() {
		return journalNo;
	}
	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}
	@ExcelField(title = "商户订单号",type=0,sort=50)
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	@ExcelField(title = "账务类型",type=0,sort=60)
	public String getFinanceType() {
		return financeType;
	}
	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}
	@ExcelField(title = "收入（+元）",type=0,sort=70)
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	@ExcelField(title = "支出（-元）",type=0,sort=80)
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	@ExcelField(title = "账户余额（元）",type=0,sort=90)
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	@ExcelField(title = "服务费（元）",type=0,sort=100)
	public String getFactorage() {
		return factorage;
	}
	public void setFactorage(String factorage) {
		this.factorage = factorage;
	}
	@ExcelField(title = "支付渠道",type=0,sort=110)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@ExcelField(title = "签约产品",type=0,sort=120)
	public String getSignProduct() {
		return signProduct;
	}
	public void setSignProduct(String signProduct) {
		this.signProduct = signProduct;
	}
	@ExcelField(title = "对方账户",type=0,sort=130)
	public String getPayAccountNumber() {
		return payAccountNumber;
	}
	public void setPayAccountNumber(String payAccountNumber) {
		this.payAccountNumber = payAccountNumber;
	}
	@ExcelField(title = "对方名称",type=0,sort=140)
	public String getPayAccountName() {
		return payAccountName;
	}
	public void setPayAccountName(String payAccountName) {
		this.payAccountName = payAccountName;
	}
	@ExcelField(title = "银行订单号",type=0,sort=150)
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	@ExcelField(title = "商品名称",type=0,sort=160)
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@ExcelField(title = "备注",type=0,sort=170)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ExcelField(title = "清算状态",type=0,sort=180)
	public String getLiquidateStatusString() {
		return liquidateStatusString;
	}
	public void setLiquidateStatusString(String liquidateStatusString) {
		this.liquidateStatusString = liquidateStatusString;
	}
	@ExcelField(title = "订单状态",type=0,sort=190)
	public String getOrderStats() {
		return orderStats;
	}
	public void setOrderStats(String orderStats) {
		this.orderStats = orderStats;
	}
	
}
