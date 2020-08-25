package com.camelot.usercenter.dto;

import java.io.Serializable;

public class LoginResDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * token值
	 */
	private String token;

	/**
	 * 昵称
	 */
	private String nickname;

	private Long uid;

	/**
	 * 用户状态
	 */
	private int ustatus;

	/**
	 * 平台ID
	 */
	private Integer platformId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public int getUstatus() {
		return ustatus;
	}

	public void setUstatus(int ustatus) {
		this.ustatus = ustatus;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

}
