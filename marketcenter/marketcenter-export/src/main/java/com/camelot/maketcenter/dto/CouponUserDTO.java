package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: [优惠券用户关联表]</p>
 * Created on 2015年12月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CouponUserDTO implements Serializable {
	private static final long serialVersionUID = 1l;
	private Long id;// 主键
	private String couponId;// 优惠券编号
	private Long userId;// 此优惠券领取者ID
	private Integer userCouponType;// 用户领取优惠券的状态	0-未使用，1-已使用，2-已过期 3-未开始 4-已终止
	private Date couponReceiveTime;// 优惠券领取时间
	private Integer deleted;// 是否删除  0-未删除，   1-删除
	
	private String userName;//用户名称
	private String phone;//联系方式
	private String level;//会员等级
	private String companyName;//公司名称
	private String nickName;//昵称
	
	private List<Integer> platformIdList;//平台ids
	
	private List<String> couponIdList;//用户可用优惠劵ID集合

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserCouponType() {
		return userCouponType;
	}

	public void setUserCouponType(Integer userCouponType) {
		this.userCouponType = userCouponType;
	}

	public Date getCouponReceiveTime() {
		return couponReceiveTime;
	}

	public void setCouponReceiveTime(Date couponReceiveTime) {
		this.couponReceiveTime = couponReceiveTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<Integer> getPlatformIdList() {
	    return platformIdList;
	}

	public void setPlatformIdList(List<Integer> platformIdList) {
	    this.platformIdList = platformIdList;
	}

	public List<String> getCouponIdList() {
		return couponIdList;
	}

	public void setCouponIdList(List<String> couponIdList) {
		this.couponIdList = couponIdList;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
