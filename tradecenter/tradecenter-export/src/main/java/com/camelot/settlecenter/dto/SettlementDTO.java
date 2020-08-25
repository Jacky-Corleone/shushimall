package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
/**
 * 
 * <p>Description: [结算表]</p>
 * Created on 2015-3-10
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SettlementDTO  implements Serializable {
    
	private static final long serialVersionUID = 5223488302743072688L;
	private java.lang.Long id;//  主键
	private java.lang.Long sellerId;//  卖家id
	private java.lang.Long shopId;//  店铺ID
	private java.lang.String companyName;// 公司名称
	private java.lang.String shopName;//店铺名称
	private java.util.Date beginDate;//  本期结算开始日期
	private java.util.Date endDate;//  本期结算截止日期
	private BigDecimal platformIncome;//  平台总收入
	private BigDecimal platformExpenditure;//  平台总支出
	private BigDecimal sellerIncome;//  卖家总收入
	private BigDecimal sellerExpenditure;//  卖家总支出
	private BigDecimal orderTotalMoney;//  订单总金额
	private BigDecimal commissionTotalMoney;//  佣金总金额
	private BigDecimal settlementTotalMoney;//  结算总金额
	private BigDecimal refundTotalMoney;//  退款总金额
	private BigDecimal platformNeedPay;//  平台需要支付给卖家的金额
	private BigDecimal platformHavePaid;//  平台已付金额
	private java.lang.String paymentId;//  支付流水号[01.网银在线账户-->卖家冻结账户/02.卖家冻结账户-->卖家收款账户/03.卖家冻结账户-->佣金账户]
	private java.lang.String platformAccount;//  平台网银在线结算账户（网银在线收单后结算到此账户）
	private java.lang.String platformCommissionAccount;//  平台佣金账号(平台收取佣金的账户)
	private java.lang.String sellerCashAccount;//  卖家收款账号
	private java.lang.String sellerFrozenAccount;//  卖家冻结账号
	
	private Integer commissionStatus;//  佣金结算状态： 1->待结算 2->已结算
	private Integer status;//  状态：状态：0-->待出账 1->待结算 2->已结算
	private String statusLabel;//  状态枚举字段：1->待结算 2->已结算 
	
	private java.util.Date billDate;//  出账日期
	private java.util.Date settlementDate;//  应结算日期
	private java.util.Date havePaidDate;//  已结算日期
	private java.util.Date created;//  创建时间
	private java.util.Date modified;//  更新时间
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(java.lang.Long sellerId) {
		this.sellerId = sellerId;
	}
	public java.lang.Long getShopId() {
		return shopId;
	}
	public void setShopId(java.lang.Long shopId) {
		this.shopId = shopId;
	}
	public java.lang.String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(java.lang.String companyName) {
		this.companyName = companyName;
	}
	public java.lang.String getShopName() {
		return shopName;
	}
	public void setShopName(java.lang.String shopName) {
		this.shopName = shopName;
	}
	public java.util.Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getPlatformIncome() {
		return platformIncome;
	}
	public void setPlatformIncome(BigDecimal platformIncome) {
		this.platformIncome = platformIncome;
	}
	public BigDecimal getPlatformExpenditure() {
		return platformExpenditure;
	}
	public void setPlatformExpenditure(BigDecimal platformExpenditure) {
		this.platformExpenditure = platformExpenditure;
	}
	public BigDecimal getSellerIncome() {
		return sellerIncome;
	}
	public void setSellerIncome(BigDecimal sellerIncome) {
		this.sellerIncome = sellerIncome;
	}
	public BigDecimal getSellerExpenditure() {
		return sellerExpenditure;
	}
	public void setSellerExpenditure(BigDecimal sellerExpenditure) {
		this.sellerExpenditure = sellerExpenditure;
	}
	public BigDecimal getOrderTotalMoney() {
		return orderTotalMoney;
	}
	public void setOrderTotalMoney(BigDecimal orderTotalMoney) {
		this.orderTotalMoney = orderTotalMoney;
	}
	public BigDecimal getCommissionTotalMoney() {
		return commissionTotalMoney;
	}
	public void setCommissionTotalMoney(BigDecimal commissionTotalMoney) {
		this.commissionTotalMoney = commissionTotalMoney;
	}
	public BigDecimal getSettlementTotalMoney() {
		return settlementTotalMoney;
	}
	public void setSettlementTotalMoney(BigDecimal settlementTotalMoney) {
		this.settlementTotalMoney = settlementTotalMoney;
	}
	public BigDecimal getRefundTotalMoney() {
		return refundTotalMoney;
	}
	public void setRefundTotalMoney(BigDecimal refundTotalMoney) {
		this.refundTotalMoney = refundTotalMoney;
	}
	public BigDecimal getPlatformNeedPay() {
		return platformNeedPay;
	}
	public void setPlatformNeedPay(BigDecimal platformNeedPay) {
		this.platformNeedPay = platformNeedPay;
	}
	public BigDecimal getPlatformHavePaid() {
		return platformHavePaid;
	}
	public void setPlatformHavePaid(BigDecimal platformHavePaid) {
		this.platformHavePaid = platformHavePaid;
	}
	public java.lang.String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(java.lang.String paymentId) {
		this.paymentId = paymentId;
	}
	public java.lang.String getPlatformAccount() {
		return platformAccount;
	}
	public void setPlatformAccount(java.lang.String platformAccount) {
		this.platformAccount = platformAccount;
	}
	public java.lang.String getPlatformCommissionAccount() {
		return platformCommissionAccount;
	}
	public void setPlatformCommissionAccount(
			java.lang.String platformCommissionAccount) {
		this.platformCommissionAccount = platformCommissionAccount;
	}
	public java.lang.String getSellerCashAccount() {
		return sellerCashAccount;
	}
	public void setSellerCashAccount(java.lang.String sellerCashAccount) {
		this.sellerCashAccount = sellerCashAccount;
	}
	public java.lang.String getSellerFrozenAccount() {
		return sellerFrozenAccount;
	}
	public void setSellerFrozenAccount(java.lang.String sellerFrozenAccount) {
		this.sellerFrozenAccount = sellerFrozenAccount;
	}
	public Integer getCommissionStatus() {
		return commissionStatus;
	}
	public void setCommissionStatus(Integer commissionStatus) {
		this.commissionStatus = commissionStatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		SettleStatusEnum settleStatusEnum=SettleStatusEnum.getEnumBycode(status);
		if(settleStatusEnum!=null){
			this.statusLabel=settleStatusEnum.getLabel();
		}
		this.status = status;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public java.util.Date getBillDate() {
		return billDate;
	}
	public void setBillDate(java.util.Date billDate) {
		this.billDate = billDate;
	}
	public java.util.Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(java.util.Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	public java.util.Date getHavePaidDate() {
		return havePaidDate;
	}
	public void setHavePaidDate(java.util.Date havePaidDate) {
		this.havePaidDate = havePaidDate;
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

