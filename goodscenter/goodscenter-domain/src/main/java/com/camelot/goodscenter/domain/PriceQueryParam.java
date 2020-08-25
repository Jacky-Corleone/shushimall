package com.camelot.goodscenter.domain;

import java.io.Serializable;

public class PriceQueryParam implements Serializable {

	private static final long serialVersionUID = 5280084812623166747L;

	private Long skuId;//SKU id
	private String areaCode;//地域编码
	private Integer qty;//数量
	private Long itemId;//商品ID
	private Long sellerId;//卖家ID
	private Long buyerId;//买家ID
	private Long shopId;//商铺ID
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
	
}
