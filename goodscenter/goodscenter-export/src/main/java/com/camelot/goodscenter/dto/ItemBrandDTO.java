package com.camelot.goodscenter.dto;

import java.io.Serializable;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * <p>Description: [商品品牌]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemBrandDTO implements Serializable {
	
	private static final long serialVersionUID = 2464215588527124822L;
	
	private Long brandId;//品牌ID
	
	private String brandName;//品牌名称
	
	private String brandLogoUrl;//品牌logo地址
	
	private Integer brandStatus;//品牌状态：1、有效；2、无效
	
	private java.util.Date created;//创建时间
	
	private java.util.Date modified;//修改时间
	
	private String brandKey; //品牌首字母
	
	private Long secondLevCid;//平台二级类目ID
	
	private Long thirdLevCid;//平台三级类目ID
	
	private Long[] brandIds;//批量选定的品牌ID  在添加品牌的时候用到
	
	
	
	public String getBrandKey() {
		return brandKey;
	}
	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}
	public Long getSecondLevCid() {
		return secondLevCid;
	}
	public void setSecondLevCid(Long secondLevCid) {
		this.secondLevCid = secondLevCid;
	}
	public Long getThirdLevCid() {
		return thirdLevCid;
	}
	public void setThirdLevCid(Long thirdLevCid) {
		this.thirdLevCid = thirdLevCid;
	}
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
	public Integer getBrandStatus() {
		return brandStatus;
	}
	public void setBrandStatus(Integer brandStatus) {
		this.brandStatus = brandStatus;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getModified() {
		return modified;
	}
	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}
	public Long[] getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(Long[] brandIds) {
		this.brandIds = brandIds;
	}
	
}
