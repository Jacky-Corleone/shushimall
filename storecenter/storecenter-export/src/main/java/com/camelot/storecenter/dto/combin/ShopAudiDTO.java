package com.camelot.storecenter.dto.combin;

import java.io.Serializable;
import java.util.List;

import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;

public class ShopAudiDTO implements Serializable{

	/**
	 * <p>Discription:[店铺审核信息DTO]</p>
	 */
	private static final long serialVersionUID = 6811506086406773107L;
	/**
	 * 店铺信息
	 */
	private ShopDTO shopInfo;
	
	/**
	 * 店铺经营类目  列表
	 */
	private List<ShopCategorySellerDTO> scsList;
	/**
	 * 店铺品牌信息 列表
	 */
	private List<ShopBrandDTO> sbList;
	
	
	public ShopDTO getShopInfo() {
		return shopInfo;
	}
	public void setShopInfo(ShopDTO shopInfo) {
		this.shopInfo = shopInfo;
	}
	public List<ShopCategorySellerDTO> getScsList() {
		return scsList;
	}
	public void setScsList(List<ShopCategorySellerDTO> scsList) {
		this.scsList = scsList;
	}
	public List<ShopBrandDTO> getSbList() {
		return sbList;
	}
	public void setSbList(List<ShopBrandDTO> sbList) {
		this.sbList = sbList;
	}
	
	
	
	
}
