package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author
 * 
 */
public class PromotionMdDTO implements Serializable {

	private static final long serialVersionUID = 1l;
	private Long id;
	private Long promotionInfoId;// 促销ID
	private java.lang.Long skuId;// skuID
	private java.lang.Long itemId;// 商品ID
	private java.lang.String areaId;// 地域 ID
	private java.lang.Long minNum;// 最少订货量
	private java.lang.Long maxNum;// 最大订货量
	private BigDecimal sellPrice;// 销售价
	private BigDecimal promotionPrice;// 促销价
	private java.util.Date createTime;// 创建时间
	private java.util.Date updateTime;// 更新时间
	private BigDecimal discountPercent; // 折扣百分比
	private Integer markdownType; // 优惠计算方式

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPromotionInfoId() {
		return promotionInfoId;
	}

	public void setPromotionInfoId(Long promotionInfoId) {
		this.promotionInfoId = promotionInfoId;
	}

	public java.lang.Long getSkuId() {
		return this.skuId;
	}

	public void setSkuId(java.lang.Long value) {
		this.skuId = value;
	}

	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}

	public java.lang.String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(java.lang.String value) {
		this.areaId = value;
	}

	public java.lang.Long getMinNum() {
		return this.minNum;
	}

	public void setMinNum(java.lang.Long value) {
		this.minNum = value;
	}

	public java.lang.Long getMaxNum() {
		return this.maxNum;
	}

	public void setMaxNum(java.lang.Long value) {
		this.maxNum = value;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(BigDecimal promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}

	public BigDecimal getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(BigDecimal discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Integer getMarkdownType() {
		return markdownType;
	}

	public void setMarkdownType(Integer markdownType) {
		this.markdownType = markdownType;
	}

}
