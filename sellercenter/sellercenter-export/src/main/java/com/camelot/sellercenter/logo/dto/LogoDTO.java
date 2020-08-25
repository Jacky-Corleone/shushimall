package com.camelot.sellercenter.logo.dto;

import java.io.Serializable;

/**
 * 
 * <p>
 * Description: [logoDTO]
 * </p>
 * Created on 2015年1月27日
 * 
 * @author <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class LogoDTO implements Serializable {
	private static final long serialVersionUID = 2990627565835351070L;
	private String logoName;
	private String picUrl;
	/**
	 * 平台ID null为科印 2绿印
	 */
	private Integer platformId;

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 * 
	 * @return String logoName.
	 */
	public String getLogoName() {
		return logoName;
	}

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 * 
	 * @param logoName
	 *            The logoName to set.
	 */
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 * 
	 * @return String picUrl.
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 * 
	 * @param picUrl
	 *            The picUrl to set.
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

}
