package com.camelot.ecm.shop;

import java.io.Serializable;

import com.camelot.common.ExcelField;

public class AdReport implements Serializable{
	
	private static final long serialVersionUID = -175876845530120471L;
	private String uno;//序号
	private String adName;//广告名称
	private String clickDate;//日期
	private String adType;//广告类型
	private String adCount;//点击次数
	@ExcelField(title="序号", align=2, sort=5)
	public String getUno() {
		return uno;
	}
	public void setUno(String uno) {
		this.uno = uno;
	}
	@ExcelField(title="广告名称", align=2, sort=50)
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	@ExcelField(title="日期", align=2, sort=10)
	public String getClickDate() {
		return clickDate;
	}
	public void setClickDate(String clickDate) {
		this.clickDate = clickDate;
	}
	@ExcelField(title="广告类型", align=2, sort=80)
	public String getAdType() {
		return adType;
	}
	public void setAdType(String adType) {
		this.adType = adType;
	}
	@ExcelField(title="点击次数", align=2, sort=100)
	public String getAdCount() {
		return adCount;
	}
	public void setAdCount(String adCount) {
		this.adCount = adCount;
	}
	
	
	

}
