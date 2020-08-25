package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.camelot.openplatform.common.DataGrid;

public class SearchItemOutDTO implements Serializable {

	private static final long serialVersionUID = 871356129957380540L;
	
	private DataGrid<SearchItem> items = new DataGrid<SearchItem>();
	private List<SearchItemAttr> attributes = new ArrayList<SearchItemAttr>();//商品非销售属性
	private List<SearchBrand> brands = new ArrayList<SearchBrand>();
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();//商品类别
	
	public DataGrid<SearchItem> getItems() {
		return items;
	}
	public void setItems(DataGrid<SearchItem> items) {
		this.items = items;
	}
	public List<SearchItemAttr> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<SearchItemAttr> attributes) {
		this.attributes = attributes;
	}
	public List<SearchBrand> getBrands() {
		return brands;
	}
	public void setBrands(List<SearchBrand> brands) {
		this.brands = brands;
	}
	public List<SearchCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<SearchCategory> categories) {
		this.categories = categories;
	}

}
