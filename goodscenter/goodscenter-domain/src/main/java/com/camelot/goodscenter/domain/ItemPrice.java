package com.camelot.goodscenter.domain;

import java.math.BigDecimal;

/**
 * <p>Description: [商品价格]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemPrice {

	private Long id;//
	private Long itemId;//商品id
	private BigDecimal sellPrice;//销售价
	private Integer minNum;//最小数量
	private Integer maxNum;//最大数量
	private Integer priceStatus;//状态 1：有效 2： 无效
	private java.util.Date created;//创建时间
	private java.util.Date modified;//修改时间
	private String areaId;//地域id
	private String areaName;//地域名称
	private Long sellerId;//商家id
	private Long platformId;//平台id
	private Long shopId;//店铺id
	private Integer stepIndex;//阶梯编号
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Integer getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(Integer priceStatus) {
		this.priceStatus = priceStatus;
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
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	public Integer getMinNum() {
		return minNum;
	}
	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public Integer getStepIndex() {
		return stepIndex;
	}
	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}
}
