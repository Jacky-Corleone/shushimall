package com.camelot.goodscenter.dto;

import java.io.Serializable;

public class CatAttrSellerDTO implements Serializable {

	private static final long serialVersionUID = 1462162923118203796L;

	private Long sellerId;//卖家ID 必填
	private Long shopId;//商家ID 必填
	private Long cid;//平台类目ID 必填
	private Integer attrType;//属性类型:1:销售属性;2:非销售属性 必填
	
	private ItemAttr attr;//卖家商品类目属性  添加时必填
	private ItemAttrValue attrValue;//卖家商品类目属性值  添加时必填
	
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
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getAttrType() {
		return attrType;
	}
	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	public ItemAttr getAttr() {
		return attr;
	}
	public void setAttr(ItemAttr attr) {
		this.attr = attr;
	}
	public ItemAttrValue getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(ItemAttrValue attrValue) {
		this.attrValue = attrValue;
	}
	
}
