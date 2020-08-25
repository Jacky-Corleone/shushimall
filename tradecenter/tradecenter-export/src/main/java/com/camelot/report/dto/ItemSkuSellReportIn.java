package com.camelot.report.dto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [店铺销售分析 报表类   入参dto]</p>
 * Created on 2015-7-17
 * @author  武超强
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

public class ItemSkuSellReportIn implements Serializable {
	private static final long serialVersionUID =1l;
	
	private Long shopId;	//店铺id
	private Long skuId;	//skuid
	private Long itemId; //商品id
	private String startDate;	//开始日期  yyyyMMdd
	private String endDate; //结束日期 	yyyyMMdd
	private Integer dayInterval;	//折线图日期间隔
	private String dateFormat; //日期格式 （默认是 yyyyMMdd ，如不需转其他格式，不用传 ）
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getDayInterval() {
		return dayInterval;
	}
	public void setDayInterval(Integer dayInterval) {
		this.dayInterval = dayInterval;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}
