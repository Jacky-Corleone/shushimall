package com.camelot.sellercenter.mallBanner.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月27日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallBannerInDTO implements Serializable {

	private static final long serialVersionUID = 3110761272079286567L;
	
	private Integer id;//
	private String bannerUrl;//图片url
	private String bannerLink;//跳转链接
	private String title;//轮播名称
	private Integer sortNumber;//排序序号
	private String timeFlag;//时间标记
	private Date startTimeShow;//页面展示时间
	private Date endTimeShow;//页面展示时间
	private String startTime;//开始展示时间
	private String endTime;//结束时间
	private String status; //轮播状态  轮播添加.即时发布时，直接添加为上架轮播 定时发布时，添加的轮播是非上架状态 上架状态 为1 下架状态为0
	
	private Integer bannerType;//1、首页；2、类目:3、地区：4、积分商城
    private Integer themeId; //频道id
    private Integer integral;// 积分
	private Long skuId;// skuId
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
		return "MallBannerInDTO [id=" + id + ", bannerUrl=" + bannerUrl + ", bannerLink=" + bannerLink + ", title="
				+ title + ", sortNumber=" + sortNumber + ", timeFlag=" + timeFlag + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}

	public Date getStartTimeShow() {
		return startTimeShow;
	}

	public void setStartTimeShow(Date startTimeShow) {
		this.startTimeShow = startTimeShow;
	}

	public Date getEndTimeShow() {
		return endTimeShow;
	}

	public void setEndTimeShow(Date endTimeShow) {
		this.endTimeShow = endTimeShow;
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
