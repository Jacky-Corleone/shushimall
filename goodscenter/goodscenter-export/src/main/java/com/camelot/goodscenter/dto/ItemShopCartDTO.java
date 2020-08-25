package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemShopCartDTO implements Serializable{

	private static final long serialVersionUID = 5452911203644476227L;

	private Long shopId;//店铺ID
	private Long itemId;//商品ID
	private Integer itemStatus;//商品状态
	private Long cid;//类目ID
	private Long skuId;//SKU id
	private String skuPicUrl;//SKU图片地址
	private Integer qty;//数量
	private String areaCode;//省市区编码
	private BigDecimal payPrice;//实际支付金额
	private BigDecimal payPriceTotal;//实际支付总金额
	private Long sellerId;//卖家ID
	private Long buyerId;//买家ID
	
	private String itemName;//商品名称
	private BigDecimal skuPrice;//sku价格
	private List<ItemAttr> attrSales;//销售属性
	private String skuScope;//SKU评价评分
	
	private boolean hasPrice;//是否显示价格  true 显示  false  不显示
	
	private List<SkuPictureDTO> skuPics = new ArrayList<SkuPictureDTO>();//SKU图片 
	
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
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}
	public List<ItemAttr> getAttrSales() {
		return attrSales;
	}
	public void setAttrSales(List<ItemAttr> attrSales) {
		this.attrSales = attrSales;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getSkuPicUrl() {
		return skuPicUrl;
	}
	public void setSkuPicUrl(String skuPicUrl) {
		this.skuPicUrl = skuPicUrl;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
	public BigDecimal getPayPriceTotal() {
		return payPriceTotal;
	}
	public void setPayPriceTotal(BigDecimal payPriceTotal) {
		this.payPriceTotal = payPriceTotal;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
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
	public String getSkuScope() {
		return skuScope;
	}
	public void setSkuScope(String skuScope) {
		this.skuScope = skuScope;
	}
	public List<SkuPictureDTO> getSkuPics() {
		return skuPics;
	}
	public void setSkuPics(List<SkuPictureDTO> skuPics) {
		this.skuPics = skuPics;
	}
	public boolean isHasPrice() {
		return hasPrice;
	}
	public void setHasPrice(boolean hasPrice) {
		this.hasPrice = hasPrice;
	}
	
}
