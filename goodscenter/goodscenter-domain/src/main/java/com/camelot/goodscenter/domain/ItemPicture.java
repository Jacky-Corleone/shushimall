package com.camelot.goodscenter.domain;

/**
 * <p>Description: [商品图片]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemPicture {

	private Long pictureId;//图片id
	private Long itemId;//商品id
	private String pictureUrl;//图片url
	private String sortNumber;//排序号,排序号最小的作为主图，从1开始
	private String pictureStatus;//图片状态:1 有效 2： 无效
	private Long sellerId;//卖家id
	private java.util.Date created;//创建日期
	private java.util.Date modified;//修改日期
	private Long shopId;//店铺id
	
	public Long getPictureId() {
		return pictureId;
	}
	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(String sortNumber) {
		this.sortNumber = sortNumber;
	}
	public String getPictureStatus() {
		return pictureStatus;
	}
	public void setPictureStatus(String pictureStatus) {
		this.pictureStatus = pictureStatus;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getModified() {
		return modified;
	}
	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	

}
