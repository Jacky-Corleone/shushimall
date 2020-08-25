package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [库存]</p>
 * Created on 2015年3月18日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TradeInventoryDTO implements Serializable{

	private static final long serialVersionUID = 2644780491164814397L;
    
	private Long shopId; //店铺id
	private Long skuId;  //sku号
	private Long sellerId;//商家id
	private Integer totalInventory;//实际库存量
	private Integer occupieInventory;//占用库存量
	private Date created;//创建时间
	private Date modified;//更新时间
	private short state;//库存状态,1有货，2无货，3可预订
	private short yn;//逻辑删除，1删除，2未删除
	private String createUser;//创建用户
	private String updateUser;//更新用户
	
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
	public Integer getOccupieInventory() {
		return occupieInventory;
	}
	public void setOccupieInventory(Integer occupieInventory) {
		this.occupieInventory = occupieInventory;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public short getState() {
		return state;
	}
	public void setState(short state) {
		this.state = state;
	}
	public short getYn() {
		return yn;
	}
	public void setYn(short yn) {
		this.yn = yn;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	
}
