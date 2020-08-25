package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author
 * 
 */
public class PromotionInfoDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  主键
		private java.lang.Long sellerId;//  商家ID 
		private java.lang.Long shopId;//  店铺ID
		private java.lang.String activityName;//  活动名称
		private Integer type;//  促销类型，1：直降，2：满减
		private java.util.Date startTime;//  促销开始时间
		private java.util.Date endTime;//  促销结束时间
		private Integer onlineState;//  促销在线状态，1：已上线，2：已下线
		private java.lang.String words;//  广告语
		private java.lang.String createUser;//  创建人
		private java.lang.String reviewer;//  审核人
		private java.util.Date createTime;//  创建时间
		private java.util.Date updateTime;//   更新时间
		private Integer userType;//用户类型 
		private Integer isAllItem;//活动范围 1：所有商品；2部分商品
		private String membershipLevel;//会员等级
		private Integer platformId;//平台ID
		private Long itemId;	//商品id（查询用）
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	
	public java.lang.String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(java.lang.String value) {
		this.activityName = value;
	}
	
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer value) {
		this.type = value;
	}
	
	public java.util.Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(java.util.Date value) {
		this.startTime = value;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(java.util.Date value) {
		this.endTime = value;
	}
	
	public Integer getOnlineState() {
		return this.onlineState;
	}

	public void setOnlineState(Integer value) {
		this.onlineState = value;
	}
	
	public java.lang.String getWords() {
		return this.words;
	}

	public void setWords(java.lang.String value) {
		this.words = value;
	}
	
	public java.lang.String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(java.lang.String value) {
		this.createUser = value;
	}
	
	public java.lang.String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(java.lang.String value) {
		this.reviewer = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getIsAllItem() {
		return isAllItem;
	}

	public void setIsAllItem(Integer isAllItem) {
		this.isAllItem = isAllItem;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}

