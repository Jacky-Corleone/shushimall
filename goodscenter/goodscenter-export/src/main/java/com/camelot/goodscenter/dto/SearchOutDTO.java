package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.camelot.openplatform.common.DataGrid;

/**
 * 
 * <p>Description: [商品搜索出参]</p>
 * Created on 2015-3-6
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SearchOutDTO implements Serializable {
	
	private static final long serialVersionUID = 4865608182872322797L;
	
	private List<ItemAttr> attributes = new ArrayList<ItemAttr>();//商品非高级非销售属性
	private List<ItemAttr> seniorAttributes = new ArrayList<ItemAttr>();//商品高级非销售属性
	private List<ItemBrandDTO> brands = new ArrayList<ItemBrandDTO>();//品牌
	private List<ItemCatCascadeDTO> categories = new ArrayList<ItemCatCascadeDTO>();//商品类别
	private DataGrid<ItemSkuDTO> itemDTOs = new DataGrid<ItemSkuDTO>();//搜索时商品
	
	public List<ItemAttr> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<ItemAttr> attributes) {
		this.attributes = attributes;
	}
	public List<ItemBrandDTO> getBrands() {
		return brands;
	}
	public void setBrands(List<ItemBrandDTO> brands) {
		this.brands = brands;
	}
	public List<ItemCatCascadeDTO> getCategories() {
		return categories;
	}
	public void setCategories(List<ItemCatCascadeDTO> categories) {
		this.categories = categories;
	}
	public DataGrid<ItemSkuDTO> getItemDTOs() {
		return itemDTOs;
	}
	public void setItemDTOs(DataGrid<ItemSkuDTO> itemDTOs) {
		this.itemDTOs = itemDTOs;
	}
	public List<ItemAttr> getSeniorAttributes() {
		return seniorAttributes;
	}
	public void setSeniorAttributes(List<ItemAttr> seniorAttributes) {
		this.seniorAttributes = seniorAttributes;
	}
}
