package com.camelot.goodscenter.domain;

import java.io.Serializable;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品评价domain类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluation implements Serializable {

	private static final long serialVersionUID = 2915057628225610266L;
	private java.lang.Long id;//  id
	private java.lang.Long userId;//  评价人
	private java.lang.Long userShopId;//  评价人店铺id
	private java.lang.Long byUserId;//  被评价人
	private java.lang.Long byShopId;//  被评价店铺id
	private java.lang.String orderId;//  订单ID
	private java.lang.Long itemId;//  商品id
	private java.lang.Long skuId;//  商品sku
	private java.lang.Integer skuScope;//  sku评分
	private java.lang.String content;//  评价内容
	private String resource = "2"; //1：默认评价 2:手动评价
	private java.lang.String type;//  1:买家对卖家评价 2:卖家对买家评价  3:售后评价
	private java.util.Date createTime;//  创建时间
	private java.util.Date modifyTime;//  编辑时间
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
	public java.lang.Long getByUserId() {
		return byUserId;
	}
	public void setByUserId(java.lang.Long byUserId) {
		this.byUserId = byUserId;
	}
	public java.lang.Long getByShopId() {
		return byShopId;
	}
	public void setByShopId(java.lang.Long byShopId) {
		this.byShopId = byShopId;
	}
	public java.lang.String getOrderId() {
		return orderId;
	}
	public void setOrderId(java.lang.String orderId) {
		this.orderId = orderId;
	}
	public java.lang.Long getItemId() {
		return itemId;
	}
	public void setItemId(java.lang.Long itemId) {
		this.itemId = itemId;
	}
	public java.lang.Long getSkuId() {
		return skuId;
	}
	public void setSkuId(java.lang.Long skuId) {
		this.skuId = skuId;
	}
	public java.lang.Integer getSkuScope() {
		return skuScope;
	}
	public void setSkuScope(java.lang.Integer skuScope) {
		this.skuScope = skuScope;
	}
	public java.lang.String getContent() {
		return content;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
