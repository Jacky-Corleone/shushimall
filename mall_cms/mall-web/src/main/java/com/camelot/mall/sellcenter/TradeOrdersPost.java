package com.camelot.mall.sellcenter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>
 * Description: [新建一个工具类，解决图片url问题]
 * </p>
 * Created on 2015-3-13
 * 
 * @author <a href="mailto: guochao@camelotchina.com">呙超</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class TradeOrdersPost implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;
	private java.lang.Long orderId;// 订单id
	private java.lang.Long sellerId;// 卖家ID
	private java.lang.Long buyerId;// 买家ID
	private java.lang.Long shopId;// 店铺ID
	private java.lang.String name;// 收获人
	private BigDecimal paymentPrice;// 实际支付金额
	private java.lang.String logisticsCompany;// 物流公司
	private java.lang.Integer afterService;// 申请售后服务(1:无,2:是,3:完成)
	private Integer state;// 订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
	private java.util.Date createTime;// 创建时间
	private Date paymentTime;// 订单支付时间
	private List<String> imgUrl = new ArrayList<String>(); // 图片url
	
	private java.lang.String itemName;//商品名称
	private java.lang.String shopName;//店铺 名称
	
	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.lang.String getShopName() {
		return shopName;
	}

	public void setShopName(java.lang.String shopName) {
		this.shopName = shopName;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.lang.Long getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.Long orderId) {
		this.orderId = orderId;
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

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public BigDecimal getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(BigDecimal paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public java.lang.String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(java.lang.String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public java.lang.Integer getAfterService() {
		return afterService;
	}

	public void setAfterService(java.lang.Integer afterService) {
		this.afterService = afterService;
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

	public List<String> getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(List<String> imgUrl) {
		this.imgUrl = imgUrl;
	}
}