package com.camelot.goodscenter.dto.indto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * <p>Description: [二手商品搜索入参]</p>
 * Created on 2015-5-6
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemOldSeachInDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String key;//关键字
	private Long cid;  //类目ID 可为任意级别
	private BigDecimal price;//固定价格条件
	private BigDecimal pricemin;//区间价格条件  最小值
	private BigDecimal pricemax;//区间价格条件  最最大值
	private java.lang.Integer recency;//  新旧程度 全新10   非全新9
	private java.lang.String provinceCode;//  省编码
	private java.lang.String cityCode;//  市编码
	private java.lang.String districtCode;//  区编码
	private Integer orderType;//1 价格正序 2 价格倒序 3商品发布时间正序  4 商品发布时间倒序
	private List<Long> cids;//三级类目组 （入参不用传）
	
	
	
	
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Long> getCids() {
		return cids;
	}
	public void setCids(List<Long> cids) {
		this.cids = cids;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getPricemin() {
		return pricemin;
	}
	public void setPricemin(BigDecimal pricemin) {
		this.pricemin = pricemin;
	}
	public BigDecimal getPricemax() {
		return pricemax;
	}
	public void setPricemax(BigDecimal pricemax) {
		this.pricemax = pricemax;
	}
	public java.lang.Integer getRecency() {
		return recency;
	}
	public void setRecency(java.lang.Integer recency) {
		this.recency = recency;
	}
	public java.lang.String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public java.lang.String getCityCode() {
		return cityCode;
	}
	public void setCityCode(java.lang.String cityCode) {
		this.cityCode = cityCode;
	}
	public java.lang.String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(java.lang.String districtCode) {
		this.districtCode = districtCode;
	}
	
	
	

}
