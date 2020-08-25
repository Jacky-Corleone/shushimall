package com.camelot.sellercenter.malladvertise.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>Description: [广告点击量 报表类输出参数dto]</p>
 * Created on 2015-7-21
 * @author  王鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class AdReportOutDto implements Serializable{
	private static final long serialVersionUID = 3930494349721168365L;
	private java.lang.Long id;//  id
	private java.lang.Long mallAdId;//  广告id
	private java.lang.Long adCount;//  广告链接点击次数
	private String clickDate;//  点击日期
	private String mallAdName;//广告名称
	private String mallAdType;//广告类型
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getMallAdId() {
		return mallAdId;
	}
	public void setMallAdId(java.lang.Long mallAdId) {
		this.mallAdId = mallAdId;
	}
	
	public java.lang.Long getAdCount() {
		return adCount;
	}
	public void setAdCount(java.lang.Long adCount) {
		this.adCount = adCount;
	}
	public String getMallAdName() {
		return mallAdName;
	}
	public void setMallAdName(String mallAdName) {
		this.mallAdName = mallAdName;
	}
	public String getMallAdType() {
		return mallAdType;
	}
	public void setMallAdType(String mallAdType) {
		this.mallAdType = mallAdType;
	}
	public String getClickDate() {
		return clickDate;
	}
	public void setClickDate(String clickDate) {
		this.clickDate = clickDate;
	}
	
	
	
}
