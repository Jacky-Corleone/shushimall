package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.camelot.activity.dto.ActivityRecordDTO;

/**
 * 
 * @author
 * 
 */
public class TradeOrdersDTO implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;
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
	private BigDecimal totalPrice;// 优惠前金额
	private BigDecimal totalDiscount;// 总优惠金额
	private BigDecimal freight;// 运费
	private BigDecimal paymentPrice;// 实际支付金额
	private java.lang.Integer invoice;// 是否有发票(1:无，2:有)
	private String invoiceTitle;// 发票抬头
	private Integer shipmentType;// 配送方式 1 在线支付 2 货到付款
	private String logisticsNo;// 物流编号
	private String logisticsCompany;// 物流公司
	private String logisticsRemark;// 备注
	private String memo;// 备注
	private java.lang.Integer paymentMethod;// 支付方式（1：个人支付；2：企业支付；）
	private java.lang.Integer paymentType;// 付款类型(0:支付宝，1:网银,2:金融通,3:线下支付，4支付宝银行，5微信，100:支付宝手机端，101:网银在线手机端 7,积分付款)
	private java.lang.Integer afterService;// 申请售后服务(1:无,2:是,3:完成)
	private java.util.Date createTime;// 创建时间
	private java.util.Date updateTime;// 订单更新时间
	private java.util.Date cancelTime;// 订单取消时间
	private java.util.Date changePaymentPriceTime;// 修改实际支付金额时间

	private Integer state;// 订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
	private Date paymentTime;// 订单支付时间
	private Integer deleted;// 是否删除 1否2是
	private Date deleteTime;// 订单删除时间
	private Integer locked;// 是否锁定 1否2是
	private Date lockTime;// 订单锁定
	private Integer refund;// 退款 1否2是
	private Date refundTime;// 退款时间
	private Integer delay;// 是否延期收货
	private Date delayOverTime;// 延期收货到期时间

	private Integer evaluate;// 买家订单评价(1:无,2:是)
	private Integer sellerEvaluate;// 卖家订单评价(1:无,2:是)
	private Integer yn;// 取消订单(1:否，2:是)

	private Integer paid;// 订单是否支付标记 1未支付 2已支付
	private Integer payPeriod;// 支付周期(天)

	private String promoCode;// 优惠码

	private Date deliverTime;// 订单发货时间
	private Date confirmReceiptTime;// 确认收货时间

	private List<TradeOrderItemsDTO> items = new ArrayList<TradeOrderItemsDTO>();

	private java.lang.Integer orderType; // 订单类型 0：询价订单 1：协议订单 2：正常

	private String contractNo; // 询价或协议订单编号
	
	private Integer platformId;// 平台ID null为科印，2为绿印

	private Long invoiceId;// 发票ID
	
	private String passKey;// 对订单号的加密结果
	
	private BigDecimal discountAmount;//折扣金额  vip卡打折
	
	// 以下字段为订单审核流程字段，当走审核订单流程时用到

	private boolean notApproved = true;// 订单是否审核，默认不审核

	private Long auditorId;// 审核人id

	private String approveStatus;// 审核状态：0：待审核（已提交状态） 1：审核通过 2：审核驳回

	private String rejectReason;// 驳回原因
	
	private Map<Integer, List<TradeOrderItemsDTO>> groupItems = new HashMap<Integer, List<TradeOrderItemsDTO>>(); // 根据运送方式分组商品
	private Integer integral;//订单使用的积分
	private BigDecimal exchangeRate;// 一个积分兑换的钱数
	private BigDecimal integralDiscount;// 积分总兑换金额
	
	private List<ActivityRecordDTO> activityRecordDTOs=new ArrayList<ActivityRecordDTO>();
	
	private String couponId; // 优惠券编号（如果用户使用了优惠券，那么此值不为空）
	
	private Integer centralPurchasing; // 是否为集采订单（1：是 2：否）
	
	private Integer isService; // 是否是服务订单（1是 2否）
	
	private Integer removeServiceGoodsFlag;//移除服务商品标识
	
	//优惠活动明细表
	private List<TradeOrderItemsDiscountDTO> tradeOrderItemsDiscountDTO=new ArrayList<TradeOrderItemsDiscountDTO>();
	private Long creatorId;//订单创建人的userId

	public java.lang.Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(java.lang.Integer orderType) {
		this.orderType = orderType;
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

	public List<TradeOrderItemsDTO> getItems() {
		return items;
	}

	public void setItems(List<TradeOrderItemsDTO> items) {
		this.items = items;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Date getDelayOverTime() {
		return delayOverTime;
	}

	public void setDelayOverTime(Date delayOverTime) {
		this.delayOverTime = delayOverTime;
	}

	public Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

	public Integer getSellerEvaluate() {
		return sellerEvaluate;
	}

	public void setSellerEvaluate(Integer sellerEvaluate) {
		this.sellerEvaluate = sellerEvaluate;
	}

	public Integer getYn() {
		return yn;
	}

	public void setYn(Integer yn) {
		this.yn = yn;
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

	public Date getConfirmReceiptTime() {
		return confirmReceiptTime;
	}

	public void setConfirmReceiptTime(Date confirmReceiptTime) {
		this.confirmReceiptTime = confirmReceiptTime;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public boolean isNotApproved() {
		return notApproved;
	}

	public void setNotApproved(boolean notApproved) {
		this.notApproved = notApproved;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public String getLogisticsRemark() {
		return logisticsRemark;
	}

	public void setLogisticsRemark(String logisticsRemark) {
		this.logisticsRemark = logisticsRemark;
	}

	public Map<Integer, List<TradeOrderItemsDTO>> getGroupItems() {
		return groupItems;
	}

	public void setGroupItems(Map<Integer, List<TradeOrderItemsDTO>> groupItems) {
		this.groupItems = groupItems;
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

	public List<ActivityRecordDTO> getActivityRecordDTOs() {
		return activityRecordDTOs;
	}

	public void setActivityRecordDTOs(List<ActivityRecordDTO> activityRecordDTOs) {
		this.activityRecordDTOs = activityRecordDTOs;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
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

	public List<TradeOrderItemsDiscountDTO> getTradeOrderItemsDiscountDTO() {
		return tradeOrderItemsDiscountDTO;
	}

	public void setTradeOrderItemsDiscountDTO(
			List<TradeOrderItemsDiscountDTO> tradeOrderItemsDiscountDTO) {
		this.tradeOrderItemsDiscountDTO = tradeOrderItemsDiscountDTO;
	}

	public Integer getIsService() {
		return isService;
	}

	public void setIsService(Integer isService) {
		this.isService = isService;
	}

	public Integer getRemoveServiceGoodsFlag() {
		return removeServiceGoodsFlag;
	}

	public void setRemoveServiceGoodsFlag(Integer removeServiceGoodsFlag) {
		this.removeServiceGoodsFlag = removeServiceGoodsFlag;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
