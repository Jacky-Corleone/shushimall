package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.List;

public class ItemCatCascadeDTO implements Serializable {

	private static final long serialVersionUID = -5134816302264085189L;

	private Long cid;//类目ID
	private String cname;//类目名称
	private List<ItemCatCascadeDTO> childCats;
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public List<ItemCatCascadeDTO> getChildCats() {
		return childCats;
	}
	public void setChildCats(List<ItemCatCascadeDTO> childCats) {
		this.childCats = childCats;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		ItemCatCascadeDTO other = (ItemCatCascadeDTO) obj;
		if (cid == null) {
			if (other.cid != null){
				return false;
			}
		} else if (!cid.equals(other.cid)){
			return false;
		}
		if (cname == null) {
			if (other.cname != null){
				return false;
			}
		} else if (!cname.equals(other.cname)){
			return false;
		}
		return true;
	}
	
	
}
