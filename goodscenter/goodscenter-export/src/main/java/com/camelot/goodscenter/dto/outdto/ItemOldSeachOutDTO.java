package com.camelot.goodscenter.dto.outdto;

import java.io.Serializable;
import java.util.List;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.openplatform.common.DataGrid;


public class ItemOldSeachOutDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private DataGrid<ItemOldDTO> itemOldDTOs = new DataGrid<ItemOldDTO>();//搜索时二手商品信息
	private  List<ItemCatCascadeDTO> catList; //类目
	
	
	
	public DataGrid<ItemOldDTO> getItemOldDTOs() {
		return itemOldDTOs;
	}
	public void setItemOldDTOs(DataGrid<ItemOldDTO> itemOldDTOs) {
		this.itemOldDTOs = itemOldDTOs;
	}
	public List<ItemCatCascadeDTO> getCatList() {
		return catList;
	}
	public void setCatList(List<ItemCatCascadeDTO> catList) {
		this.catList = catList;
	}
	
	
	
	
}
