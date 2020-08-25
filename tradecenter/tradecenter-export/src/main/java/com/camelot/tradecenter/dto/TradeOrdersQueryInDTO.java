package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author
 * 
 */
public class TradeOrdersQueryInDTO implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;
	private String orderId;// 订单号
	private String parentOrderId;// 父订单号
	private java.lang.Long sellerId;// 卖家账号
	private java.lang.Long buyerId;// 买家账号
	private String name;// 收货人
	private Long shopId;// 店铺ID
	private Integer state;// 订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
	private Integer evaluate;// 买评价状态 1:无,2:是
	private Integer sellerEvaluate;// 卖家评价状态 1:无,2:是
	private Integer cancelFlag;// 1未取消 2 取消
	private Integer locked;// 是否锁定 1否2是
	private Date createStart;// 下单开始时间
	private Date createEnd;// 下单结束时间
	private String itemName;// 商品名
	private String shopName;// 店铺名称
	private Date updateStart;// 更新时间
	private Date updateEnd;// 更新时间
	private Integer shipmentType;// 1 在线支付 2延期支付

	private List<Long> itemIdList = new ArrayList<Long>();// 商品ID组
	private List<Long> shopIdList = new ArrayList<Long>();// 店铺ID组
	private List<Long> sellerIdList = new ArrayList<Long>();// 卖家ID组
	private List<Long> buyerIdList = new ArrayList<Long>();// 买家ID组
	private List<Integer> stateList = new ArrayList<Integer>();// 订单状态组

	private Integer userType;// 1买家 2卖家
	private Integer delay;// 延迟收货

	private Integer refund; // 退款(1:无,2:是)
	private Integer deleted;// 订单删除(1:无,2:是)

	private Integer paid;//是否已经付款，1未付款 2 已付款
	private Integer isPayPeriod;//是否延期支付，1延期支付，2立即支付
	private Integer paymentMethod;// 支付类型,个人支付，企业支付
	private Integer paymentType;// 付款类型，支付宝，网银在线....
	private Date paymentTimeStart;// 支付开始时间
	private Date paymentTimeEnd;// 支付结束时间
	private boolean notApproved = true;// 订单是否审核，默认不审核
	private Long auditorId;// 审核人id
	private String approveStatus;// 0:待审核 1：审核通过 2：审核驳回
	private Integer platformId;//平台id,区分绿印平台和科印平台id,科印此字段为空，绿印此字段为2；
	private Integer integral;//订单使用积分
	private Integer centralPurchasing; // 是否为集采订单（1：是 2：否）
	private Integer removeServiceGoodsFlag;//移除服务商品标识
	private Integer isService; // 是否是服务订单（1是 2否）

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

	public Integer getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(Integer cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public Date getCreateStart() {
		return createStart;
	}

	public void setCreateStart(Date createStart) {
		this.createStart = createStart;
	}

	public Date getCreateEnd() {
		return createEnd;
	}

	public void setCreateEnd(Date createEnd) {
		this.createEnd = createEnd;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public List<Long> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(List<Long> itemIdList) {
		this.itemIdList = itemIdList;
	}

	public List<Long> getShopIdList() {
		return shopIdList;
	}

	public void setShopIdList(List<Long> shopIdList) {
		this.shopIdList = shopIdList;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public List<Long> getSellerIdList() {
		return sellerIdList;
	}

	public void setSellerIdList(List<Long> sellerIdList) {
		this.sellerIdList = sellerIdList;
	}

	public List<Long> getBuyerIdList() {
		return buyerIdList;
	}

	public void setBuyerIdList(List<Long> buyerIdList) {
		this.buyerIdList = buyerIdList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSellerEvaluate() {
		return sellerEvaluate;
	}

	public void setSellerEvaluate(Integer sellerEvaluate) {
		this.sellerEvaluate = sellerEvaluate;
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

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}

	public Date getUpdateStart() {
		return updateStart;
	}

	public void setUpdateStart(Date updateStart) {
		this.updateStart = updateStart;
	}

	public Date getUpdateEnd() {
		return updateEnd;
	}

	public void setUpdateEnd(Date updateEnd) {
		this.updateEnd = updateEnd;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Date getPaymentTimeStart() {
		return paymentTimeStart;
	}

	public void setPaymentTimeStart(Date paymentTimeStart) {
		this.paymentTimeStart = paymentTimeStart;
	}

	public Date getPaymentTimeEnd() {
		return paymentTimeEnd;
	}

	public void setPaymentTimeEnd(Date paymentTimeEnd) {
		this.paymentTimeEnd = paymentTimeEnd;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public boolean isNotApproved() {
		return notApproved;
	}

	public void setNotApproved(boolean notApproved) {
		this.notApproved = notApproved;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getIsPayPeriod() {
		return isPayPeriod;
	}

	public void setIsPayPeriod(Integer isPayPeriod) {
		this.isPayPeriod = isPayPeriod;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
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

	public Integer getRemoveServiceGoodsFlag() {
		return removeServiceGoodsFlag;
	}

	public void setRemoveServiceGoodsFlag(Integer removeServiceGoodsFlag) {
		this.removeServiceGoodsFlag = removeServiceGoodsFlag;
	}

	public Integer getIsService() {
		return isService;
	}

	public void setIsService(Integer isService) {
		this.isService = isService;
	}
	
}
