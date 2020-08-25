package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellPrice implements Serializable {

	private static final long serialVersionUID = 8669069898218358749L;

	private BigDecimal sellPrice;//销售价
	private Integer minNum;//最小数量
	private Integer maxNum;//最大数量
	private String areaId;//地域id
	private String areaName;//地域名称
	private Integer stepIndex;//阶梯编号
	private BigDecimal maketPirce;//市场价
	private BigDecimal costPrice;//成本价
	
	
	private Long shopId;
	private Long sellerId;
	
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
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getStepIndex() {
		return stepIndex;
	}
	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public BigDecimal getMaketPirce() {
		return maketPirce;
	}
	public void setMaketPirce(BigDecimal maketPirce) {
		this.maketPirce = maketPirce;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	
	

	
}
