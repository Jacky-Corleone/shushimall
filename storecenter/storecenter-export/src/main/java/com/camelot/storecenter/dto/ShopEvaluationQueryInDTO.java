package com.camelot.storecenter.dto;

import java.io.Serializable;

public class ShopEvaluationQueryInDTO implements Serializable {

	private static final long serialVersionUID = 6882112042565042431L;
	private java.lang.Long id;// id
	private java.lang.Long userId;// 评价人id
	private java.lang.Long userShopId;// 评价人店铺id
	private java.lang.Long byShopId;// 被评价店铺ID
	private String resource;// 1:默认回复 2:手动回复
	private String orderId; // 订单id
	private java.lang.Integer shopDescriptionScope;// 描述相符评分
	private java.lang.Integer shopServiceScope;// 服务态度评分
	private java.lang.Integer shopArrivalScope;// 到货速度评分

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}

	public java.lang.Long getUserShopId() {
		return userShopId;
	}

	public void setUserShopId(java.lang.Long userShopId) {
		this.userShopId = userShopId;
	}

	public java.lang.Long getByShopId() {
		return byShopId;
	}

	public void setByShopId(java.lang.Long byShopId) {
		this.byShopId = byShopId;
	}

	public java.lang.Integer getShopDescriptionScope() {
		return shopDescriptionScope;
	}

	public void setShopDescriptionScope(java.lang.Integer shopDescriptionScope) {
		this.shopDescriptionScope = shopDescriptionScope;
	}

	public java.lang.Integer getShopServiceScope() {
		return shopServiceScope;
	}

	public void setShopServiceScope(java.lang.Integer shopServiceScope) {
		this.shopServiceScope = shopServiceScope;
	}

	public java.lang.Integer getShopArrivalScope() {
		return shopArrivalScope;
	}

	public void setShopArrivalScope(java.lang.Integer shopArrivalScope) {
		this.shopArrivalScope = shopArrivalScope;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
