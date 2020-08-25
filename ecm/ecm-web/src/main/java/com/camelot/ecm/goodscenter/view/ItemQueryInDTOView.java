package com.camelot.ecm.goodscenter.view;

import com.camelot.goodscenter.dto.ItemQueryInDTO;

public class ItemQueryInDTOView {
	private ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
	private Integer platformId1;
	private Integer platformId2;
	private Integer cid;
	private String shopName;
	public ItemQueryInDTO getItemQueryInDTO() {
		return itemQueryInDTO;
	}

	public void setItemQueryInDTO(ItemQueryInDTO itemQueryInDTO) {
		this.itemQueryInDTO = itemQueryInDTO;
	}

	public Integer getPlatformId1() {
		return platformId1;
	}

	public void setPlatformId1(Integer platformId1) {
		this.platformId1 = platformId1;
	}

	public Integer getPlatformId2() {
		return platformId2;
	}

	public void setPlatformId2(Integer platformId2) {
		this.platformId2 = platformId2;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
}
