package com.camelot.goodscenter.dto;

import java.io.Serializable;

public class ItemSalesVolumeDTO implements Serializable {

	private static final long serialVersionUID = -3397135710195469737L;

	private Long sellerId;
	private Long shopId;
	private Long itemId;
	private Long skuId;
	private long salesVolume;//销量
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
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
	public long getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(long salesVolume) {
		this.salesVolume = salesVolume;
	}
	
	
}
