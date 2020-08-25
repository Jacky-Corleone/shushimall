package com.camelot.goodscenter.dto;

import java.io.Serializable;

public class ItemAdDTO implements Serializable {

	private static final long serialVersionUID = -24993834664607911L;

	private Long itemId;//商品ID
	private String ad;//广告语
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getAd() {
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	
	
}
