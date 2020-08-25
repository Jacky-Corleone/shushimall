package com.camelot.shop.domain;

import java.io.Serializable;

public class Area implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	private java.lang.String provinceCode;// 省份代码
	private java.lang.String provinceName;// 省份名字
	private java.lang.String cityName;// 市的名字
	private java.lang.String cityCode;// 市的代码
	private java.lang.String districtName;// 区的名字
	private java.lang.String districtCode;// 区的代码
	public java.lang.String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public java.lang.String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(java.lang.String provinceName) {
		this.provinceName = provinceName;
	}
	public java.lang.String getCityName() {
		return cityName;
	}
	public void setCityName(java.lang.String cityName) {
		this.cityName = cityName;
	}
	public java.lang.String getCityCode() {
		return cityCode;
	}
	public void setCityCode(java.lang.String cityCode) {
		this.cityCode = cityCode;
	}
	public java.lang.String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(java.lang.String districtName) {
		this.districtName = districtName;
	}
	public java.lang.String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(java.lang.String districtCode) {
		this.districtCode = districtCode;
	}
	
	
	

}
