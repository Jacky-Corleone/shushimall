package com.camelot.sellercenter.mallBanner.dto;

import java.io.Serializable;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月27日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallBannerDTO implements Serializable {

	private static final long serialVersionUID = 3110761272079286567L;
	
	private Integer id;//
	private String bannerUrl;//图片url
	private String bannerLink;//跳转链接
	private String title;//轮播名称
	private Integer sortNumber;//排序序号
	private String created;//创建时间
	private String modified;//修改时间
	private String status;//0:不可用 1：可用
	private String timeFlag;//0：创建时间  1：发布时间
	private String startTime;//开始展示时间
	private String endTime;//结束时间

	private Integer bannerType;//1、首页；2、类目:3、地区：4、积分商城
	private Integer themeId;//主题id
	private Integer integral;// 积分
	private Long skuId;// skuId
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getBannerLink() {
		return bannerLink;
	}

	public void setBannerLink(String bannerLink) {
		this.bannerLink = bannerLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(String timeFlag) {
		this.timeFlag = timeFlag;
	}

	public String toString() {
		return "MallBannerDTO [id=" + id + ", bannerUrl=" + bannerUrl + ", bannerLink=" + bannerLink + ", title="
				+ title + ", sortNumber=" + sortNumber + ", created=" + created + ", modified=" + modified
				+ ", status=" + status + ", startTime=" + startTime + ", endTime=" + endTime + ",timeFlag=" + timeFlag + "]";
	}


	public Integer getBannerType() {
		return bannerType;
	}

	public void setBannerType(Integer bannerType) {
		this.bannerType = bannerType;
	}

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}
	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

}
