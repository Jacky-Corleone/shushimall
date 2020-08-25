package com.camelot.maketcenter.dto;

import java.io.Serializable;

public class PromotionInDTO implements Serializable{

	/**
	 * <p>Discription:[活动查询入参]</p>
	 */
	private static final long serialVersionUID = 2047022200380397174L;

	/**
	 * 用户id
	 */
	private Long sellerId;
	/*
	 * 店铺ID
	 */
	private Long shopId;
	/**
	 * 商品ID
	 */
	private Long itemId;
	
	/**
	 * SKUid
	 */
	private Long skuId;
	/**
	 * 地域id
	 */
	private Long areaId;
	/**
	 * 促销ID  活动ID
	 */
	private Long promotionInfoId;
	
	private String activityName;//促销名称
	private Integer onlineState;//  促销在线状态，1：已上线，2：已下线
	private java.util.Date createTimestr;//  创建时间开始
	private java.util.Date createTimeend;//  创建时间结束
	private Integer type;//  促销类型，1：直降，2：满减
	
	private String isEffective;  //不为空时 查询有效期内活动
	private Integer num; //购买数量
	private Integer isAllItem;//活动范围：1，所有商品，2：部分商品
	private Integer userType;//用户类型，1：个人用户；2：企业用户
	private String membershipLevel;//会员等级
	private Integer platformId;//平台id
	
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Integer getOnlineState() {
		return onlineState;
	}
	public void setOnlineState(Integer onlineState) {
		this.onlineState = onlineState;
	}
	public java.util.Date getCreateTimestr() {
		return createTimestr;
	}
	public void setCreateTimestr(java.util.Date createTimestr) {
		this.createTimestr = createTimestr;
	}
	public java.util.Date getCreateTimeend() {
		return createTimeend;
	}
	public void setCreateTimeend(java.util.Date createTimeend) {
		this.createTimeend = createTimeend;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getPromotionInfoId() {
		return promotionInfoId;
	}
	public void setPromotionInfoId(Long promotionInfoId) {
		this.promotionInfoId = promotionInfoId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Integer getIsAllItem() {
		return isAllItem;
	}
	public void setIsAllItem(Integer isAllItem) {
		this.isAllItem = isAllItem;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	public String getMembershipLevel() {
		return membershipLevel;
	}
	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	
}
