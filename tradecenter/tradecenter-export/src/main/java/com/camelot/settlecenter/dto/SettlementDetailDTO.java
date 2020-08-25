package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;

/**
 * 
 * <p>
 * Description: [结算详情表]
 * </p>
 * Created on 2015-3-10
 * 
 * @author yuht
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SettlementDetailDTO implements Serializable {

	private static final long serialVersionUID = -4107224169864917290L;
	private java.lang.Long id;// 主键
	private java.lang.Long sellerId;// 卖家id
	private java.lang.Long shopId;// 店铺ID
	private java.lang.Long settlementId;// 关联的结算单id
	private java.lang.String paymentId;// [无效]支付流水号
	private java.lang.String orderId;// 订单编号
	private java.lang.Long refundId;// 退款单编号
	private BigDecimal orderPrice;// 订单额（实际支付的金额，有退款的是退款后金额）
	private BigDecimal refundMoney;// 退款金额
	private BigDecimal platformIncome;// 平台收入
	private BigDecimal platformExpenditure;// 平台支出
	private BigDecimal sellerIncome;// 卖家收入
	private BigDecimal sellerExpenditure;// 卖家支出
	private BigDecimal buyerIncome;// 买家收入
	private BigDecimal buyerExpenditure;// 买家支出
	private BigDecimal commission;// 佣金
	private BigDecimal factorage;// 第三方手续费
	private BigDecimal sellerCashAccountIncome;// 卖家提现账号应收
	private java.lang.String platformAccount;// [无效]平台网银在线结算账户（网银在线收单后结算到此账户）
	private java.lang.String platformCommissionAccount;// [无效]平台佣金账号(平台收取佣金的账户)
	private java.lang.String sellerCashAccount;// [无效]卖家收款账号
	private java.lang.String sellerFrozenAccount;// [无效]卖家冻结账号
	private java.lang.String buyerPaymentAccount;// 买家支付账号
	private Integer paymentMethod;// 支付方式：1->网银, 2->中信 3 :线下
	private java.lang.String paymentResult;// 支付结果(101)
	private Integer type;// 类型：1->网银在线结算,2->支付
	private Integer status;// 状态：-1.已冻结 0.待支付 1.确认网银在线已支付 2.已支付待付佣金 3.支付完成
	private String statusLabel;// 状态：-1.已冻结 0.待支付 1.确认网银在线已支付 2.已支付待付佣金 3.支付完成
	private java.lang.String remark;// 备注
	private java.util.Date created;// 创建时间
	private java.util.Date modified;// 更新时间
	private String passKey;// 对订单号的加密结果

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}

	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}

	public java.lang.Long getSettlementId() {
		return this.settlementId;
	}

	public void setSettlementId(java.lang.Long value) {
		this.settlementId = value;
	}

	public java.lang.String getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(java.lang.String value) {
		this.paymentId = value;
	}

	public java.lang.String getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.String orderId) {
		this.orderId = orderId;
	}

	public java.lang.Long getRefundId() {
		return this.refundId;
	}

	public void setRefundId(java.lang.Long value) {
		this.refundId = value;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getPlatformIncome() {
		return this.platformIncome;
	}

	public void setPlatformIncome(BigDecimal value) {
		this.platformIncome = value;
	}

	public BigDecimal getPlatformExpenditure() {
		return this.platformExpenditure;
	}

	public void setPlatformExpenditure(BigDecimal value) {
		this.platformExpenditure = value;
	}

	public BigDecimal getSellerIncome() {
		return this.sellerIncome;
	}

	public void setSellerIncome(BigDecimal value) {
		this.sellerIncome = value;
	}

	public BigDecimal getSellerExpenditure() {
		return this.sellerExpenditure;
	}

	public void setSellerExpenditure(BigDecimal value) {
		this.sellerExpenditure = value;
	}

	public BigDecimal getBuyerIncome() {
		return this.buyerIncome;
	}

	public void setBuyerIncome(BigDecimal value) {
		this.buyerIncome = value;
	}

	public BigDecimal getBuyerExpenditure() {
		return this.buyerExpenditure;
	}

	public void setBuyerExpenditure(BigDecimal value) {
		this.buyerExpenditure = value;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public BigDecimal getFactorage() {
		return factorage;
	}

	public void setFactorage(BigDecimal factorage) {
		this.factorage = factorage;
	}

	public BigDecimal getSellerCashAccountIncome() {
		return this.sellerCashAccountIncome;
	}

	public void setSellerCashAccountIncome(BigDecimal value) {
		this.sellerCashAccountIncome = value;
	}

	public java.lang.String getPlatformAccount() {
		return this.platformAccount;
	}

	public void setPlatformAccount(java.lang.String value) {
		this.platformAccount = value;
	}

	public java.lang.String getPlatformCommissionAccount() {
		return this.platformCommissionAccount;
	}

	public void setPlatformCommissionAccount(java.lang.String value) {
		this.platformCommissionAccount = value;
	}

	public java.lang.String getSellerCashAccount() {
		return this.sellerCashAccount;
	}

	public void setSellerCashAccount(java.lang.String value) {
		this.sellerCashAccount = value;
	}

	public java.lang.String getSellerFrozenAccount() {
		return this.sellerFrozenAccount;
	}

	public void setSellerFrozenAccount(java.lang.String value) {
		this.sellerFrozenAccount = value;
	}

	public java.lang.String getBuyerPaymentAccount() {
		return this.buyerPaymentAccount;
	}

	public void setBuyerPaymentAccount(java.lang.String value) {
		this.buyerPaymentAccount = value;
	}

	public Integer getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(Integer value) {
		this.paymentMethod = value;
	}

	public java.lang.String getPaymentResult() {
		return this.paymentResult;
	}

	public void setPaymentResult(java.lang.String value) {
		this.paymentResult = value;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer value) {
		this.type = value;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		SettleDetailStatusEnum settleDetailStatusEnum = SettleDetailStatusEnum.getEnumBycode(value);
		if (settleDetailStatusEnum != null) {
			this.statusLabel = settleDetailStatusEnum.getLabel();
		}
		this.status = value;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
	
	

}
