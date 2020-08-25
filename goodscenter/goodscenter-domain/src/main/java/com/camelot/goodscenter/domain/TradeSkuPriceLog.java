package com.camelot.goodscenter.domain;

import java.math.BigDecimal;

/**
 * <p>Description: [SUK价格日志]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class TradeSkuPriceLog {

	private Long id;//
	private Long skuId;//sku号
	private Long sellerId;//
	private BigDecimal marketPrice;//市场价
	private BigDecimal costPrice;//成本价
	private BigDecimal sellPrice;//销售价
	private String sellStepped;//销售阶梯
	private String areaName;//区域名称
	private String areaId;//区域id
	private String description;//
	private String createUser;//
	private java.util.Date createTime;//创建时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getSellStepped() {
		return sellStepped;
	}
	public void setSellStepped(String sellStepped) {
		this.sellStepped = sellStepped;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
}
