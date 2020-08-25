package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.List;

public class SearchCategory implements Serializable {

	private static final long serialVersionUID = 829573689914798269L;

	private Long categoryId;//类目ID
	private String categoryName;//类目名称
	private List<SearchCategory> childCats;//子类目
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<SearchCategory> getChildCats() {
		return childCats;
	}
	public void setChildCats(List<SearchCategory> childCats) {
		this.childCats = childCats;
	}
	
	
}
