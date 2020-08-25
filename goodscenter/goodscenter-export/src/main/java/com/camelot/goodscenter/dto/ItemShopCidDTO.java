package com.camelot.goodscenter.dto;

import java.io.Serializable;

public class ItemShopCidDTO implements Serializable {

	private static final long serialVersionUID = 6476620323388301064L;

	private Long itemId;//商品ID
	private Long shopCid;//店铺分类ID
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getShopCid() {
		return shopCid;
	}
	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}
	
	
}
