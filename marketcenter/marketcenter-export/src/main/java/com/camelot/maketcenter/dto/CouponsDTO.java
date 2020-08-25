package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: [优惠券]</p>
 * Created on 2015年12月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CouponsDTO implements Serializable {
	private static final long serialVersionUID = 1l;
	private Long id;// 主键
	private String couponId;// 优惠券编号
	private String couponName;// 优惠券名称
	private Integer couponType;// 优惠券类型(1:满减券,2:折扣券,3:现金券)
	private BigDecimal meetPrice;// 优惠券使用需要满足条件
	private BigDecimal couponAmount;// 优惠券面额
	private BigDecimal couponMax;// 折扣券限额
	private Integer couponNum;// 优惠券数量
	private Date couponStartTime;// 优惠券使用开始时间
	private Date couponEndTime;// 优惠券使用结束时间
	private Integer platformId;// 优惠券使用平台(1:印刷家平台,2:绿印平台,3:小管家)
	private Integer couponUsingRange;// 优惠券使用范围(1:平台通用类,2:店铺通用类,3:品类通用类,4:SKU使用类)
	private Integer couponUserType;// 优惠券使用用户类型
	private String couponUserMembershipLevel;// 优惠券使用用户会员等级
	private Integer sendCouponType;// 优惠券发放方式  1-待用户领取 2-直接发放用户
	private Integer state;// 优惠券状态 0-未开始 1-已开始 2-已结束 3-已中止 4-待送审 5-待审核 6- 审核驳回
	private Date updateDate;// 修改时间
	private Integer deleted;// 是否删除  0 否   1 已经删除
	private String createUser;// 创建人
	private Long verifyUser;// 审核人
	private Long shopId;// 店铺id
	private String promulgator; //发布者
	private String couponExplain;// 优惠券说明
	
	private String userIds;//卖家中心所有发放的用户id
	private String skuIds;//选择的skuid
	private List<Long> shopIds;//店铺Id的集合
	private String userName;//创建人名称
	private Integer receivedNumber;//已领取数量
	private String rejectReason;//送审驳回原因
	private Integer costAllocation;//成本分摊  1：平台分摊  2： 店铺分摊
	private List<Integer> stateList;//优惠券状态集合
	private String descr;//使用规则描述
	private String isEffective;//是否有效期  不为空查有效内 为空查所有
	
	private List<Integer> platformIdList;//平台ids
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}
	
	public BigDecimal getCouponMax() {
		return couponMax;
	}

	public void setCouponMax(BigDecimal couponMax) {
		this.couponMax = couponMax;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public BigDecimal getMeetPrice() {
		return meetPrice;
	}

	public void setMeetPrice(BigDecimal meetPrice) {
		this.meetPrice = meetPrice;
	}
	
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Long verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getCouponUsingRange() {
		return couponUsingRange;
	}

	public void setCouponUsingRange(Integer couponUsingRange) {
		this.couponUsingRange = couponUsingRange;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getCouponUserType() {
		return couponUserType;
	}

	public void setCouponUserType(Integer couponUserType) {
		this.couponUserType = couponUserType;
	}

	public String getCouponUserMembershipLevel() {
		return couponUserMembershipLevel;
	}

	public void setCouponUserMembershipLevel(String couponUserMembershipLevel) {
		this.couponUserMembershipLevel = couponUserMembershipLevel;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Integer getSendCouponType() {
		return sendCouponType;
	}

	public void setSendCouponType(Integer sendCouponType) {
		this.sendCouponType = sendCouponType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCouponStartTime() {
		return couponStartTime;
	}

	public void setCouponStartTime(Date couponStartTime) {
		this.couponStartTime = couponStartTime;
	}

	public Date getCouponEndTime() {
		return couponEndTime;
	}

	public void setCouponEndTime(Date couponEndTime) {
		this.couponEndTime = couponEndTime;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	public String getCouponExplain() {
		return couponExplain;
	}

	public void setCouponExplain(String couponExplain) {
		this.couponExplain = couponExplain;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(String skuIds) {
		this.skuIds = skuIds;
	}

	public List<Long> getShopIds() {
		return shopIds;
	}

	public void setShopIds(List<Long> shopIds) {
		this.shopIds = shopIds;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getReceivedNumber() {
		return receivedNumber;
	}

	public void setReceivedNumber(Integer receivedNumber) {
		this.receivedNumber = receivedNumber;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getCostAllocation() {
		return costAllocation;
	}

	public void setCostAllocation(Integer costAllocation) {
		this.costAllocation = costAllocation;
	}

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}

	public String getPromulgator() {
		return promulgator;
	}

	public void setPromulgator(String promulgator) {
		this.promulgator = promulgator;
	}

	public List<Integer> getPlatformIdList() {
	    return platformIdList;
	}

	public void setPlatformIdList(List<Integer> platformIdList) {
	    this.platformIdList = platformIdList;
	}
	
}
