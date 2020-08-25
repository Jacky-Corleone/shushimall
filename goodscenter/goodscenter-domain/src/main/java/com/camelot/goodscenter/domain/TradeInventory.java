package com.camelot.goodscenter.domain;

/**
 * <p>Description: [库存]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class TradeInventory {

	private Long id;//
	private Long shopId;//店铺ID
	private Long skuId;//sku号
	private Long sellerId;//商户id
	private Integer totalInventory;//实际库存量
	private Integer occupiedInventory;//占用库存量
	private java.util.Date created;//创建时间
	private java.util.Date modified;//更新时间
	private String state;//库存状态,1有货，2无货，3可预订
	private String yn;//逻辑删除，1删除，2未删除
	private Long createUser;//创建用户
	private Long updateUser;//更新用户
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getTotalInventory() {
		return totalInventory;
	}
	public void setTotalInventory(Integer totalInventory) {
		this.totalInventory = totalInventory;
	}
	public Integer getOccupiedInventory() {
		return occupiedInventory;
	}
	public void setOccupiedInventory(Integer occupiedInventory) {
		this.occupiedInventory = occupiedInventory;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getYn() {
		return yn;
	}
	public void setYn(String yn) {
		this.yn = yn;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
}
