package com.camelot.storecenter.dto;

import java.io.Serializable;

public class ShopSalesVolumeDTO implements Serializable {

	private static final long serialVersionUID = -3397135710195469737L;

	private Long sellerId;
	private Long shopId;
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
	public long getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(long salesVolume) {
		this.salesVolume = salesVolume;
	}
}
