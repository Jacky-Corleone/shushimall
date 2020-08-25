package com.camelot.usercenter.dto.indto;

import java.io.Serializable;

import com.camelot.usercenter.dto.UserDTO;

/**
 * 
 * <p>Description: [添加店铺子账户入参]</p>
 * Created on 2015-3-16
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class StoreUserResourceInDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	private String username;//用户名
	
	private String password;//密码
	
	private String nickName;//姓名

	private Long parentId;//父级账号ID
	private Long shopId;//店铺ID
	
	private Integer[] resourceIds;//资源id组
	
	private UserDTO userDTO;
	
	

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer[] getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(Integer[] resourceIds) {
		this.resourceIds = resourceIds;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	
	
}
