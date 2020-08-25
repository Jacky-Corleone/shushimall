package com.camelot.ecm.goodscenter.view;

import com.camelot.goodscenter.dto.indto.ItemOldInDTO;

public class ItemOldInDTOView {

	private ItemOldInDTO itemOldInDTO;
	
	private Integer platformId1;
	
	private Integer platformId2;
	
	private Integer cid;
	
	private String buyerName;

	public ItemOldInDTO getItemOldInDTO() {
		return itemOldInDTO;
	}

	public void setItemOldInDTO(ItemOldInDTO itemOldInDTO) {
		this.itemOldInDTO = itemOldInDTO;
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

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
}
