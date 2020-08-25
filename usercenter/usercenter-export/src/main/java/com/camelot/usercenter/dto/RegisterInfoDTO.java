package com.camelot.usercenter.dto;

import com.camelot.usercenter.enums.UserEnums;

import java.io.Serializable;

/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015-3-17
 *
 * @author yuht
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class RegisterInfoDTO implements Serializable {
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
     * 密码
     */
    private String loginpwd;

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
    //昵称
    private String nickName;

    /**
     * 父级账号ID
     */
    private Long parentId;
    /**
     * 店铺ID
     */
    private Long shopId;

    private String linkMan; //联系人
    private UserEnums.DepartMent department;//所属部门
    private String linkPhoneNum; //固定电话
    
    private Integer platformId;
    
    private Integer status;
    private Integer paymentStatus;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }


    public UserEnums.DepartMent getDepartment() {
        return department;
    }

    public void setDepartment(UserEnums.DepartMent department) {
        this.department = department;
    }

    public String getLinkPhoneNum() {
        return linkPhoneNum;
    }

    public void setLinkPhoneNum(String linkPhoneNum) {
        this.linkPhoneNum = linkPhoneNum;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
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

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
