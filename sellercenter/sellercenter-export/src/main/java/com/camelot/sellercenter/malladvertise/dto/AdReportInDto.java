package com.camelot.sellercenter.malladvertise.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>Description: [广告点击量 报表类输入参数dto]</p>
 * Created on 2015-7-23
 * @author  王鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class AdReportInDto implements Serializable{
	
	private static final long serialVersionUID = 3527613047270244097L;
	private java.lang.Long adCount;//  广告链接点击次数
	private String clickDate;//  统计日期
	
	private String mallAdName;//广告名称
	private String mallAdType;//广告类型
	
	private String clickDateBegin;//开始时间
	private String clickDateEnd;// 结束时间
	
	private String dateFormat; //不传默认为yyyyMMdd
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public java.lang.Long getAdCount() {
		return adCount;
	}
	public void setAdCount(java.lang.Long adCount) {
		this.adCount = adCount;
	}
	public String getClickDate() {
		return clickDate;
	}
	public void setClickDate(String clickDate) {
		this.clickDate = clickDate;
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
	public String getClickDateBegin() {
		return clickDateBegin;
	}
	public void setClickDateBegin(String clickDateBegin) {
		this.clickDateBegin = clickDateBegin;
	}
	public String getClickDateEnd() {
		return clickDateEnd;
	}
	public void setClickDateEnd(String clickDateEnd) {
		this.clickDateEnd = clickDateEnd;
	}

}
