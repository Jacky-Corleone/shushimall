package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.camelot.openplatform.common.Pager;

public class SearchItemSkuInDTO implements Serializable {

	private static final long serialVersionUID = 871356129957380540L;
	
	private Pager<SearchItemSkuOutDTO> pager = new Pager<SearchItemSkuOutDTO>();//分页对象
	private List<Long> brandIds = new ArrayList<Long>();//品牌ID组
	private Long cid;//商品类目ID
	private String keyword;//搜索关键字
	private String attributes;//[属性ID:属性值ID;...] 此形式字符串
	private String areaCode;//区域编码3
	private Long buyerId;//买家ID
	private int orderSort;//1 时间升序 2时间降序  3评价升序 4评价降序  5销量升序 6销量降序 7销量升序 8销量降序
	private Integer platformId;//平台id 目前 科印此值为null,即不传此值,绿印此值为2
	private List<String> addSources; // 商品来源(套装、单品、服务商品)
	private String houseType; // 房型
	private BigDecimal minPrice; // 价格区间最小值
	private BigDecimal maxPrice; // 价格区间最大值
	private Integer searched;//商品是否可以被搜索到。1或null：可以被搜索;2：不可以被搜索
	
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
	public Pager<SearchItemSkuOutDTO> getPager() {
		return pager;
	}
	public void setPager(Pager<SearchItemSkuOutDTO> pager) {
		this.pager = pager;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public int getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(int orderSort) {
		this.orderSort = orderSort;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public Integer getSearched() {
		return searched;
	}
	public void setSearched(Integer searched) {
		this.searched = searched;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public List<String> getAddSources() {
		return addSources;
	}
	public void setAddSources(List<String> addSources) {
		this.addSources = addSources;
	}
	
}
