package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * Created on 2015年3月13日
 * 
 * @author <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemFavouriteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 卖家ID
	 */
	private Integer sellerId;
	/**
	 * 店铺ID
	 */
	private Integer shopId;
	/**
	 * 商品ID
	 */
	private Integer itemId;

	/**
	 * 商品名称
	 */
	private String itemName;

	/**
	 * SKU_ID
	 */
	private Integer skuId;

	/**
	 * SKU销售属性串
	 */
	private String skuAttributeStr;

	/**
	 * 属性值
	 */
	private List<ItemAttr> attributes = new ArrayList<ItemAttr>();

	/**
	 * 创建时间
	 */
	private Date createdDate;

	private Integer favouriteCount;// 收藏数量

	private List<Long> userIdList;

	public Integer getFavouriteCount() {
		return favouriteCount;
	}

	public void setFavouriteCount(Integer favouriteCount) {
		this.favouriteCount = favouriteCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public String getSkuAttributeStr() {
		return skuAttributeStr;
	}

	public void setSkuAttributeStr(String skuAttributeStr) {
		this.skuAttributeStr = skuAttributeStr;
	}

	public List<ItemAttr> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ItemAttr> attributes) {
		this.attributes = attributes;
	}

}
