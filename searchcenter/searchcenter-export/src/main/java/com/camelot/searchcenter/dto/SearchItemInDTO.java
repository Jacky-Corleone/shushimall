package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.camelot.openplatform.common.Pager;

public class SearchItemInDTO implements Serializable {

	private static final long serialVersionUID = 871356129957380540L;
	
	private Pager<SearchItem> pager = new Pager<SearchItem>();//分页对象
	private List<Long> attrIds = new ArrayList<Long>();//属性ID组
	private List<Long> brandIds = new ArrayList<Long>();//品牌ID组
	private Long cid;//商品类目ID
	private String keyword;//搜索关键字
	public List<Long> getAttrIds() {
		return attrIds;
	}
	public void setAttrIds(List<Long> attrIds) {
		this.attrIds = attrIds;
	}
	public List<Long> getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(List<Long> brandIds) {
		this.brandIds = brandIds;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Pager<SearchItem> getPager() {
		return pager;
	}
	public void setPager(Pager<SearchItem> pager) {
		this.pager = pager;
	}
	
	
}
