package com.camelot.goodscenter.domain;

/**
 * <p>Description: [SUK实体]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemSku {
	
	private Long skuId;//sku id
	private Long itemId;//商品id
	private Integer itemStatus;//商品状态
	private Long sellerId;//商家id
	private Integer skuStatus;//sku 状态,1:有效;2:无效
	private Integer skuType;//sku 类型 1:主sku,2:非主sku
	private String ad;//广告语,未使用！！
	private String attributes;//销售属性集合：keyId:valueId
	private java.util.Date created;//创建日期
	private java.util.Date modified;//修改日期
	private Long shopId;//店铺
	
	//商品编号
	private String productId;
	
	private String itemName;//商品名称
	private Long cid;//类目ID
	private String skuScope;//SKU评价评分
	
	private Integer hasPrice;//是否显示价格 1：有价格；2：暂无报价
	
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
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
	public Integer getSkuStatus() {
		return skuStatus;
	}
	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
	}
	public Integer getSkuType() {
		return skuType;
	}
	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}
	public String getAd() {
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getModified() {
		return modified;
	}
	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	public String getSkuScope() {
		return skuScope;
	}
	public void setSkuScope(String skuScope) {
		this.skuScope = skuScope;
	}
	public Integer getHasPrice() {
		return hasPrice;
	}
	public void setHasPrice(Integer hasPrice) {
		this.hasPrice = hasPrice;
	}
	
}
