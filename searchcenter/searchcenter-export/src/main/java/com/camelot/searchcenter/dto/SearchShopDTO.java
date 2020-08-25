package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.List;

public class SearchShopDTO implements Serializable {

	private static final long serialVersionUID = -6484619094488936688L;

	private Long shopId;//店铺ID
	private String shopName;//店铺名称
	private String shopLogoUrl;//店铺LOGO图片
	private String sellerName;//用户名称
	private String serviceScore;//服务评价
	private List<SearchItemSku> recommendItems;
	private String shopUrl;//店铺域名
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopLogoUrl() {
		return shopLogoUrl;
	}
	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getServiceScore() {
		return serviceScore;
	}
	public void setServiceScore(String serviceScore) {
		this.serviceScore = serviceScore;
	}
	public List<SearchItemSku> getRecommendItems() {
		return recommendItems;
	}
	public void setRecommendItems(List<SearchItemSku> recommendItems) {
		this.recommendItems = recommendItems;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	
	
}
