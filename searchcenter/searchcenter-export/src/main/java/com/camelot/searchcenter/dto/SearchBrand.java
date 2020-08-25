package com.camelot.searchcenter.dto;

import java.io.Serializable;

public class SearchBrand implements Serializable {
	
	private static final long serialVersionUID = -3513958333643378693L;

	private Long brandId;//品牌ID
	private String brandName;//品牌
	private String brandLogoUrl;//品牌图片地址
	
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandLogoUrl() {
		return brandLogoUrl;
	}
	public void setBrandLogoUrl(String brandLogoUrl) {
		this.brandLogoUrl = brandLogoUrl;
	}
	
}
