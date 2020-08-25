package com.camelot.mall.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 该dto用于接收店铺类目管理的前台页面数据
 * @author 周乐
 *
 */
public class ShopCategoryPageDTO implements Serializable {
	private static final long serialVersionUID = 650892521148846251L;

	//子类目集合
	private List<ShopCategoryChildDTO> childList = new ArrayList<ShopCategoryChildDTO>();
	//父类目名称
	private List<String> parentName = new ArrayList<String>();
	
	public List<ShopCategoryChildDTO> getChildList() {
		return childList;
	}
	public void setChildList(List<ShopCategoryChildDTO> childList) {
		this.childList = childList;
	}
	public List<String> getParentName() {
		return parentName;
	}
	public void setParentName(List<String> parentName) {
		this.parentName = parentName;
	}
}
