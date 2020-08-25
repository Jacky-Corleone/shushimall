package com.camelot.tradecenter.dto;

import java.io.Serializable;

public class SalesVolumeDTO implements Serializable{

	private static final long serialVersionUID = -5326479214927874317L;

	private Long sellerId;//卖家ID
	private Long buyerId;//买家ID
	private Long shopId;//店铺ID
	private Long itemId;//商品ID
	private Long skuId;//SKU ID
	private Long salesVolume;//销量
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
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Long salesVolume) {
		this.salesVolume = salesVolume;
	}
	
	
}
