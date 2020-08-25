package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RecommendItemDTO implements Serializable {

	private static final long serialVersionUID = 4295266239081851712L;

	private Long itemId;//商品ID
	private String itemName;//商品名称
	private BigDecimal itemPrice;//商品价格
	private String itemPicUrl;//商品图片路径
	private Long skuId;//SKU ID
	private String skuPicUrl;//SKU商品
	private BigDecimal skuPrice;//SKU价格
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemPicUrl() {
		return itemPicUrl;
	}
	public void setItemPicUrl(String itemPicUrl) {
		this.itemPicUrl = itemPicUrl;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getSkuPicUrl() {
		return skuPicUrl;
	}
	public void setSkuPicUrl(String skuPicUrl) {
		this.skuPicUrl = skuPicUrl;
	}
	public BigDecimal getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}
	
}
