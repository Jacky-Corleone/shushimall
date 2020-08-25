package com.camelot.goodscenter.domain;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * <p>Description: [品牌]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemBrand {

	private Long brandId;//品牌ID
	
	private String brandName;//品牌名称
	
	private String brandLogoUrl;//品牌logo地址
	
	private Integer brandStatus;//品牌状态：1、有效；2、无效
	
	private java.util.Date created;//创建时间
	
	private java.util.Date modified;//修改时间
	
	private String brandKey; //品牌首字母
	
// setter and getter
	
	
	
	public String getBrandName(){
		return brandName;
	}
	
	public String getBrandKey() {
		return brandKey;
	}

	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}
	public String getBrandLogoUrl(){
		return brandLogoUrl;
	}
	
	public void setBrandLogoUrl(String brandLogoUrl){
		this.brandLogoUrl = brandLogoUrl;
	}
	public Integer getBrandStatus(){
		return brandStatus;
	}
	
	public void setBrandStatus(Integer brandStatus){
		this.brandStatus = brandStatus;
	}
	public java.util.Date getCreated(){
		return created;
	}
	
	public void setCreated(java.util.Date created){
		this.created = created;
	}
	public java.util.Date getModified(){
		return modified;
	}
	
	public void setModified(java.util.Date modified){
		this.modified = modified;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
}
