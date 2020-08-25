package com.camelot.mall.sellcenter;

import java.util.Date;

/**
 * 
 * <p>
 * Description: [新建一个工具类，解决拥有权限问题]
 * </p>
 * Created on 2015-3-18
 * 
 * @author <a href="mailto: guochao@camelotchina.com">呙超</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ChildUserListPost {

	private Long userId;// 子用户ID
	private String username;// 用户名
	private Long shopId;// 店铺ID
	private String shopName;// 店铺名称
	private Date updateTime;// 修改时间
	private String resourceIds;// 拥有权限
	private String nickName;//子用户姓名
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}