package com.camelot.settlecenter.dto.indto;

import java.io.Serializable;

public class SettlementInfoInDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long cid; //类目ID
	private Long itemId;//商品id
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	
	

}
