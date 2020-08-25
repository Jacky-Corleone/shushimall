package com.camelot.tradecenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author
 * 
 */
public class TradeOrders implements Serializable {

	private static final long serialVersionUID = 1l;
	private java.lang.Long id;// 主键
	private String orderId;// 订单id
	private String parentOrderId;// 是否为子订单(0未拆单，-1为父订单，其它为父订单ID)
	private java.lang.Long sellerId;// 卖家ID
	private java.lang.Long buyerId;// 买家ID
	private java.lang.Long shopId;// 店铺ID
	private String name;// 用户名
	private java.lang.Long provinceId;// 省
	private java.lang.Long cityId;// 市
	private java.lang.Long countyId;// 县
	private String detailAddress;// 详细地址
	private String fullAddress;// 全地址
	private String mobile;// 移动电话
	private String phone;// 固定电话
	private String email;// 邮件
	private java.util.Date orderTime;// 下订单时间
	private java.util.Date orderFinishTime;// 订单完成时间
	private BigDecimal totalPrice;// 优惠前金额
	private BigDecimal totalDiscount;// 总优惠金额
	private BigDecimal freight;// 运费
	private BigDecimal paymentPrice;// 实际支付金额
	private java.lang.Integer state;// 订单状态
	private java.lang.Integer yn;// 取消订单(1:否，2:是)
	private String orderJfsKey;// jfs读取key
	private java.lang.Integer invoice;// 是否有发票(1:无，2:有)
	private String invoiceTitle;// 发票抬头
	private String logisticsNo;// 物流编号
	private String logisticsCompany;// 物流公司
	private String logisticsRemark;// 备注
	private String memo;// 备注
	private java.lang.Integer paymentMethod;// 支付方式（1：个人支付；2：企业支付；）
	private java.lang.Integer paymentType;// 支付类型(1:网银,2:中信,3:线下，4，支付宝，5：微信)
	private java.util.Date paymentTime;// 付款时间
	private Integer shipmentType;// 配送方式
	private java.lang.Integer deleted;// 订单删除(1:无,2:是)
	private java.util.Date deleteTime;// 订单删除时间
	private java.lang.Integer locked;// 订单锁定(1:无,2:是)
	private java.util.Date lockTime;// 锁定时间
	private java.lang.Integer refund;// 退款(1:无,2:是)
	private java.util.Date refundTime;// 退款时间
	private java.lang.Integer delay;// 延期收货(1:无,2:是)
	private java.util.Date delayOverTime;// 延期收货结束时间
	private java.lang.Integer evaluate;// 买家订单评价(1:无,2:是)
	private java.lang.Integer sellerEvaluate;// 卖家订单评价(1:无,2:是)
	private java.lang.Integer afterService;// 申请售后服务(1:无,2:是,3:完成)
	private java.util.Date createTime;// 创建时间
	private java.util.Date updateTime;// 订单更新时间
	private java.util.Date cancelTime;// 订单取消时间
	private java.util.Date changePaymentPriceTime;// 修改实际支付金额时间
	private java.util.Date deliverTime;// 订单发货时间
	private java.util.Date confirmReceiptTime;// 确认收货时间

	private Integer paid;// 订单是否支付标记 1未支付 2已支付
	private Integer payPeriod;// 支付周期(天)

	private String promoCode;// 优惠码

	private java.lang.Integer orderType; // 订单类型 0：询价订单 1：协议订单 2：正常
	private Integer platformId;// 平台id，null为科印，2为绿印
	
	private BigDecimal discountAmount;//折扣金额  vip卡打折
	
	private Integer integral;//订单中使用的积分
	
	private BigDecimal exchangeRate;// 一个积分兑换的钱数
	
	private BigDecimal integralDiscount;// 积分总兑换金额
	
	private Integer centralPurchasing; // 是否为集采订单（1：是 2：否）
	
	private Integer isService; // 是否是服务订单（1是 2否）

	private Long creatorId;//订单创建人的userId；

	public java.lang.Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(java.lang.Integer orderType) {
		this.orderType = orderType;
	}

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	public java.lang.Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(java.lang.Long sellerId) {
		this.sellerId = sellerId;
	}

	public java.lang.Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(java.lang.Long buyerId) {
		this.buyerId = buyerId;
	}

	public java.lang.Long getShopId() {
		return shopId;
	}

	public void setShopId(java.lang.Long shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.lang.Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(java.lang.Long provinceId) {
		this.provinceId = provinceId;
	}

	public java.lang.Long getCityId() {
		return cityId;
	}

	public void setCityId(java.lang.Long cityId) {
		this.cityId = cityId;
	}

	public java.lang.Long getCountyId() {
		return countyId;
	}

	public void setCountyId(java.lang.Long countyId) {
		this.countyId = countyId;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public java.util.Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(java.util.Date orderTime) {
		this.orderTime = orderTime;
	}

	public java.util.Date getOrderFinishTime() {
		return orderFinishTime;
	}

	public void setOrderFinishTime(java.util.Date orderFinishTime) {
		this.orderFinishTime = orderFinishTime;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(BigDecimal paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public java.lang.Integer getState() {
		return state;
	}

	public void setState(java.lang.Integer state) {
		this.state = state;
	}

	public java.lang.Integer getYn() {
		return yn;
	}

	public void setYn(java.lang.Integer yn) {
		this.yn = yn;
	}

	public String getOrderJfsKey() {
		return orderJfsKey;
	}

	public void setOrderJfsKey(String orderJfsKey) {
		this.orderJfsKey = orderJfsKey;
	}

	public java.lang.Integer getInvoice() {
		return invoice;
	}

	public void setInvoice(java.lang.Integer invoice) {
		this.invoice = invoice;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public java.lang.Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(java.lang.Integer paymentType) {
		this.paymentType = paymentType;
	}

	public java.util.Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(java.util.Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public java.lang.Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(java.lang.Integer deleted) {
		this.deleted = deleted;
	}

	public java.util.Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(java.util.Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public java.lang.Integer getLocked() {
		return locked;
	}

	public void setLocked(java.lang.Integer locked) {
		this.locked = locked;
	}

	public java.util.Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(java.util.Date lockTime) {
		this.lockTime = lockTime;
	}

	public java.lang.Integer getRefund() {
		return refund;
	}

	public void setRefund(java.lang.Integer refund) {
		this.refund = refund;
	}

	public java.util.Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(java.util.Date refundTime) {
		this.refundTime = refundTime;
	}

	public java.lang.Integer getDelay() {
		return delay;
	}

	public void setDelay(java.lang.Integer delay) {
		this.delay = delay;
	}

	public java.util.Date getDelayOverTime() {
		return delayOverTime;
	}

	public void setDelayOverTime(java.util.Date delayOverTime) {
		this.delayOverTime = delayOverTime;
	}

	public java.lang.Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(java.lang.Integer evaluate) {
		this.evaluate = evaluate;
	}

	public java.lang.Integer getSellerEvaluate() {
		return sellerEvaluate;
	}

	public void setSellerEvaluate(java.lang.Integer sellerEvaluate) {
		this.sellerEvaluate = sellerEvaluate;
	}

	public java.lang.Integer getAfterService() {
		return afterService;
	}

	public void setAfterService(java.lang.Integer afterService) {
		this.afterService = afterService;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.util.Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(java.util.Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public java.util.Date getChangePaymentPriceTime() {
		return changePaymentPriceTime;
	}

	public void setChangePaymentPriceTime(java.util.Date changePaymentPriceTime) {
		this.changePaymentPriceTime = changePaymentPriceTime;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public Integer getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(Integer payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public java.lang.Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(java.lang.Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public java.util.Date getConfirmReceiptTime() {
		return confirmReceiptTime;
	}

	public void setConfirmReceiptTime(java.util.Date confirmReceiptTime) {
		this.confirmReceiptTime = confirmReceiptTime;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getLogisticsRemark() {
		return logisticsRemark;
	}

	public void setLogisticsRemark(String logisticsRemark) {
		this.logisticsRemark = logisticsRemark;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getCentralPurchasing() {
		return centralPurchasing;
	}

	public void setCentralPurchasing(Integer centralPurchasing) {
		this.centralPurchasing = centralPurchasing;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getIntegralDiscount() {
		return integralDiscount;
	}

	public void setIntegralDiscount(BigDecimal integralDiscount) {
		this.integralDiscount = integralDiscount;
	}

	public Integer getIsService() {
		return isService;
	}

	public void setIsService(Integer isService) {
		this.isService = isService;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
