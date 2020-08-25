package com.camelot.mall.dto;

import java.io.Serializable;

public class ShopCategoryChildDTO implements Serializable {
	private static final long serialVersionUID = 711735068725425951L;
	//父级类目id
	private Long parentCid;
	//类目名称
	private String childCategoryName;
	
	public Long getParentCid() {
		return parentCid;
	}
	public void setParentCid(Long parentCid) {
		this.parentCid = parentCid;
	}
	public String getChildCategoryName() {
		return childCategoryName;
	}
	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}
}
