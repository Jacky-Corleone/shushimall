package com.camelot.usercenter.dto;

import java.io.Serializable;

public class RegisterDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Long uid;
	/**
	 * 用户名
	 */
	private String loginname;

	/**
	 * 手机号
	 */
	private String userMobile;

	/**
	 * 邮箱
	 */
	private String userEmail;

	/**
	 * 用户类型
	 */
	private Integer userType;

	/**
	 * 是否是快速注册
	 */
	private Integer quickType;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	private Integer uStatus;

	private Integer deleted; // 是否可用 (冻结 解冻)

	private Integer platformId;// 平台ID

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getQuickType() {
		return quickType;
	}

	public void setQuickType(Integer quickType) {
		this.quickType = quickType;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getuStatus() {
		return uStatus;
	}

	public void setuStatus(Integer uStatus) {
		this.uStatus = uStatus;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

}
