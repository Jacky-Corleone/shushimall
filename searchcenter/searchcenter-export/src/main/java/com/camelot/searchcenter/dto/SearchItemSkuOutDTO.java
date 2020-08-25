package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.camelot.openplatform.common.DataGrid;

public class SearchItemSkuOutDTO implements Serializable {

	private static final long serialVersionUID = 2920206909351800602L;

	private List<SearchItemAttr> attributes = new ArrayList<SearchItemAttr>();//商品非销售属性
	private List<SearchItemAttr> seniorAttributes = new ArrayList<SearchItemAttr>();//商品高级非销售属性
	private List<SearchBrand> brands = new ArrayList<SearchBrand>();//品牌
	private List<SearchCategory> categories = new ArrayList<SearchCategory>();//商品类别
	private DataGrid<SearchItemSku> itemSkus = new DataGrid<SearchItemSku>();
	
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
	public DataGrid<SearchItemSku> getItemSkus() {
		return itemSkus;
	}
	public void setItemSkus(DataGrid<SearchItemSku> itemSkus) {
		this.itemSkus = itemSkus;
	}
	public List<SearchItemAttr> getSeniorAttributes() {
		return seniorAttributes;
	}
	public void setSeniorAttributes(List<SearchItemAttr> seniorAttributes) {
		this.seniorAttributes = seniorAttributes;
	}
}
