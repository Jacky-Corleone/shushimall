package com.camelot.usercenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>Description: [查询子账用户对象]</p>
 * Created on 2015-3-17
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ChildUserDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long userId;//子用户ID
	private String  username;//用户名
	private String nickName;//姓名
	private Long shopId;//店铺ID
	private Date  updateTime;//修改时间
	private List<UserMallResourceDTO> userMallResourceList;//子用户资源列表
	
	
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public List<UserMallResourceDTO> getUserMallResourceList() {
		return userMallResourceList;
	}
	public void setUserMallResourceList(List<UserMallResourceDTO> userMallResourceList) {
		this.userMallResourceList = userMallResourceList;
	}
	
	
	
}
