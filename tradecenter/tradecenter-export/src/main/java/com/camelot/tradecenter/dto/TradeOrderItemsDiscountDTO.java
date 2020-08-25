package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 订单商品优惠 对外暴露的DTO
 *
 * @author 王东晓
 * 
 */
public class TradeOrderItemsDiscountDTO implements Serializable {

	private static final long serialVersionUID = -8575624064024884736L;

	private Long id; // 主键ID
	private Long orderItemsId;// trade_order_items表的ID
	private String orderId; // 订单ID
	private Long skuId; // skuID
	private Long markdownId;// 满减活动ID
	private Integer markdownType;// 满减活动类型；1：平台活动，2：店铺活动
	private BigDecimal markdownDiscount;// 满减活动优惠金额
	private Long fullReductionId;// 直降活动ID
	private Integer fullReductionType;// 直降活动类型；1：平台直降，2：店铺直降
	private BigDecimal fullReductionDiscount;// 直降活动优惠金额
	private String couponId;// 优惠券编号
	private Integer couponType;// 优惠券类型；1：平台优惠券，2：店铺优惠券
	private BigDecimal couponDiscount;// 优惠券优惠金额
	private Integer integral;// 商品使用积分
	private BigDecimal integralDiscount;// 商品使用积分兑换的金额
	private Integer integralType;// 积分类型；1：平台积分，2：店铺积分

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderItemsId() {
		return orderItemsId;
	}

	public void setOrderItemsId(Long orderItemsId) {
		this.orderItemsId = orderItemsId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getMarkdownId() {
		return markdownId;
	}

	public void setMarkdownId(Long markdownId) {
		this.markdownId = markdownId;
	}

	public Integer getMarkdownType() {
		return markdownType;
	}

	public void setMarkdownType(Integer markdownType) {
		this.markdownType = markdownType;
	}

	public BigDecimal getMarkdownDiscount() {
		return markdownDiscount;
	}

	public void setMarkdownDiscount(BigDecimal markdownDiscount) {
		this.markdownDiscount = markdownDiscount;
	}

	public Long getFullReductionId() {
		return fullReductionId;
	}

	public void setFullReductionId(Long fullReductionId) {
		this.fullReductionId = fullReductionId;
	}

	public Integer getFullReductionType() {
		return fullReductionType;
	}

	public void setFullReductionType(Integer fullReductionType) {
		this.fullReductionType = fullReductionType;
	}

	public BigDecimal getFullReductionDiscount() {
		return fullReductionDiscount;
	}

	public void setFullReductionDiscount(BigDecimal fullReductionDiscount) {
		this.fullReductionDiscount = fullReductionDiscount;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public BigDecimal getIntegralDiscount() {
		return integralDiscount;
	}

	public void setIntegralDiscount(BigDecimal integralDiscount) {
		this.integralDiscount = integralDiscount;
	}

	public Integer getIntegralType() {
		return integralType;
	}

	public void setIntegralType(Integer integralType) {
		this.integralType = integralType;
	}
	
	
}
