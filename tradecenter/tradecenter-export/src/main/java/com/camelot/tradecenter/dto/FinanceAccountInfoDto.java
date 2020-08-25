package com.camelot.tradecenter.dto;

import java.io.Serializable;

/**
 * 
 * @author yangsaibei
 * 
 * @version 2015年4月17日
 * 
 */
public class FinanceAccountInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2100452272100745480L;

	/**
	 * 账户id
	 */
	private String accountId;
	/**
	 * 金融云支付帐号
	 */
	private String financialCloudPaymentAccountId;
	/**
	 * 全名
	 */
	private String fullName;

	/**
	 * 所属银行
	 */
	private String ownedBank;

	/**
	 * 银行登录用户名
	 */
	private String bankLoginUsername;
	/**
	 * 前置机地址
	 */
	private String citicFrontEndProcessor;
	/**
	 * 主账户名称
	 */
	private String masterAccountName;
	/**
	 * 主账户帐号
	 */
	private String masterAccountNumber;

	/**
	 * 集群ID
	 */
	private String clusterId;
	/**
	 * 结算账户名称
	 */
	private String settlementAccountName;
	/**
	 * 结算账户帐号
	 */
	private String settlementAccountNumber;
	/**
	 * 印刷家支付宝收款账户名称
	 */
	private String settlementAlipayAccountName;
	/**
	 * 印刷家支付宝收款账户账号
	 */
	private String settlementAlipayAccountNumber;
	/**
	 * 印刷家支付宝退款账户名称
	 */
	private String refundAlipayAccountName;
	/**
	 * 印刷家支付宝退款账户账号
	 */
	private String refundAlipayAccountNumber;
	/**
	 * 印刷家微信收款账户名称
	 */
	private String settlementWXAccountName;
	/**
	 * 印刷家微信收款账户账号
	 */
	private String settlementWXAccountNumber;
	/**
	 * 印刷家微信退款账户名称
	 */
	private String refundWXAccountName;
	/**
	 * 印刷家微信退款账户账号
	 */
	private String refundWXAccountNumber;
	/**
	 * 佣金账户名称
	 */
	private String commissionAccountName;

	/**
	 * 佣金账户帐号
	 */
	private String commissionAccountNumber;
	/**
	 * 代理出金账户名称
	 * 
	 */
	private String actingOutGoldAccountName;
	/**
	 * 代理出金账户帐号
	 */
	private String actingOutGoldAccountNumber;
	/**
	 * 公共计费收费账户名称
	 */
	private String publicChargingFeesAccountName;
	/**
	 * 公共计费收费账户帐号
	 */
	private String publicChargingFeesAccountNumber;
	/**
	 * 公共调账户名称
	 */
	private String publicAccountName;
	/**
	 * 公共调账户帐号
	 */
	private String publicAccountNumber;
	/**
	 * 资金初始化账户名称
	 */
	private String initializationFundsAccountName;
	/**
	 * 资金初始化账户帐号
	 */
	private String initializationFundsAccountNumber;
	/**
	 * 汇总退款账户名称
	 */
	private String refundAccountName;
	/**
	 * 汇总退款账户帐号
	 */
	private String refundAccountNumber;
	/**
	 * 手续费补足账户名称
	 */
	private String poundageFillAccountName;
	/**
	 * 手续费补足账户帐号
	 */
	private String poundageFillAccountNumber;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFinancialCloudPaymentAccountId() {
		return financialCloudPaymentAccountId;
	}

	public void setFinancialCloudPaymentAccountId(String financialCloudPaymentAccountId) {
		this.financialCloudPaymentAccountId = financialCloudPaymentAccountId;
	}

	public String getOwnedBank() {
		return ownedBank;
	}

	public void setOwnedBank(String ownedBank) {
		this.ownedBank = ownedBank;
	}

	public String getActingOutGoldAccountNumber() {
		return actingOutGoldAccountNumber;
	}

	public void setActingOutGoldAccountNumber(String actingOutGoldAccountNumber) {
		this.actingOutGoldAccountNumber = actingOutGoldAccountNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBankLoginUsername() {
		return bankLoginUsername;
	}

	public void setBankLoginUsername(String bankLoginUsername) {
		this.bankLoginUsername = bankLoginUsername;
	}

	public String getCiticFrontEndProcessor() {
		return citicFrontEndProcessor;
	}

	public void setCiticFrontEndProcessor(String citicFrontEndProcessor) {
		this.citicFrontEndProcessor = citicFrontEndProcessor;
	}

	public String getMasterAccountName() {
		return masterAccountName;
	}

	public void setMasterAccountName(String masterAccountName) {
		this.masterAccountName = masterAccountName;
	}

	public String getMasterAccountNumber() {
		return masterAccountNumber;
	}

	public void setMasterAccountNumber(String masterAccountNumber) {
		this.masterAccountNumber = masterAccountNumber;
	}

	public String getSettlementAccountName() {
		return settlementAccountName;
	}

	public void setSettlementAccountName(String settlementAccountName) {
		this.settlementAccountName = settlementAccountName;
	}

	public String getSettlementAccountNumber() {
		return settlementAccountNumber;
	}

	public void setSettlementAccountNumber(String settlementAccountNumber) {
		this.settlementAccountNumber = settlementAccountNumber;
	}

	public String getCommissionAccountName() {
		return commissionAccountName;
	}

	public void setCommissionAccountName(String commissionAccountName) {
		this.commissionAccountName = commissionAccountName;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getCommissionAccountNumber() {
		return commissionAccountNumber;
	}

	public void setCommissionAccountNumber(String commissionAccountNumber) {
		this.commissionAccountNumber = commissionAccountNumber;
	}

	public String getActingOutGoldAccountName() {
		return actingOutGoldAccountName;
	}

	public void setActingOutGoldAccountName(String actingOutGoldAccountName) {
		this.actingOutGoldAccountName = actingOutGoldAccountName;
	}

	public String getActingOutGoldAccountNuber() {
		return actingOutGoldAccountNumber;
	}

	public void setActingOutGoldAccountNuber(String actingOutGoldAccountNuber) {
		this.actingOutGoldAccountNumber = actingOutGoldAccountNuber;
	}

	public String getPublicChargingFeesAccountName() {
		return publicChargingFeesAccountName;
	}

	public void setPublicChargingFeesAccountName(String publicChargingFeesAccountName) {
		this.publicChargingFeesAccountName = publicChargingFeesAccountName;
	}

	public String getPublicChargingFeesAccountNumber() {
		return publicChargingFeesAccountNumber;
	}

	public void setPublicChargingFeesAccountNumber(String publicChargingFeesAccountNumber) {
		this.publicChargingFeesAccountNumber = publicChargingFeesAccountNumber;
	}

	public String getPublicAccountName() {
		return publicAccountName;
	}

	public void setPublicAccountName(String publicAccountName) {
		this.publicAccountName = publicAccountName;
	}

	public String getPublicAccountNumber() {
		return publicAccountNumber;
	}

	public void setPublicAccountNumber(String publicAccountNumber) {
		this.publicAccountNumber = publicAccountNumber;
	}

	public String getInitializationFundsAccountName() {
		return initializationFundsAccountName;
	}

	public void setInitializationFundsAccountName(String initializationFundsAccountName) {
		this.initializationFundsAccountName = initializationFundsAccountName;
	}

	public String getInitializationFundsAccountNumber() {
		return initializationFundsAccountNumber;
	}

	public void setInitializationFundsAccountNumber(String initializationFundsAccountNumber) {
		this.initializationFundsAccountNumber = initializationFundsAccountNumber;
	}

	public String getRefundAccountName() {
		return refundAccountName;
	}

	public void setRefundAccountName(String refundAccountName) {
		this.refundAccountName = refundAccountName;
	}

	public String getRefundAccountNumber() {
		return refundAccountNumber;
	}

	public void setRefundAccountNumber(String refundAccountNumber) {
		this.refundAccountNumber = refundAccountNumber;
	}

	public String getSettlementAlipayAccountName() {
		return settlementAlipayAccountName;
	}

	public void setSettlementAlipayAccountName(String settlementAlipayAccountName) {
		this.settlementAlipayAccountName = settlementAlipayAccountName;
	}

	public String getSettlementAlipayAccountNumber() {
		return settlementAlipayAccountNumber;
	}

	public void setSettlementAlipayAccountNumber(String settlementAlipayAccountNumber) {
		this.settlementAlipayAccountNumber = settlementAlipayAccountNumber;
	}

	public String getSettlementWXAccountName() {
		return settlementWXAccountName;
	}

	public void setSettlementWXAccountName(String settlementWXAccountName) {
		this.settlementWXAccountName = settlementWXAccountName;
	}

	public String getSettlementWXAccountNumber() {
		return settlementWXAccountNumber;
	}

	public void setSettlementWXAccountNumber(String settlementWXAccountNumber) {
		this.settlementWXAccountNumber = settlementWXAccountNumber;
	}

	public String getRefundAlipayAccountName() {
		return refundAlipayAccountName;
	}

	public void setRefundAlipayAccountName(String refundAlipayAccountName) {
		this.refundAlipayAccountName = refundAlipayAccountName;
	}

	public String getRefundAlipayAccountNumber() {
		return refundAlipayAccountNumber;
	}

	public void setRefundAlipayAccountNumber(String refundAlipayAccountNumber) {
		this.refundAlipayAccountNumber = refundAlipayAccountNumber;
	}

	public String getRefundWXAccountName() {
		return refundWXAccountName;
	}

	public void setRefundWXAccountName(String refundWXAccountName) {
		this.refundWXAccountName = refundWXAccountName;
	}

	public String getRefundWXAccountNumber() {
		return refundWXAccountNumber;
	}

	public void setRefundWXAccountNumber(String refundWXAccountNumber) {
		this.refundWXAccountNumber = refundWXAccountNumber;
	}

	public String getPoundageFillAccountName() {
		return poundageFillAccountName;
	}

	public void setPoundageFillAccountName(String poundageFillAccountName) {
		this.poundageFillAccountName = poundageFillAccountName;
	}

	public String getPoundageFillAccountNumber() {
		return poundageFillAccountNumber;
	}

	public void setPoundageFillAccountNumber(String poundageFillAccountNumber) {
		this.poundageFillAccountNumber = poundageFillAccountNumber;
	}

}
