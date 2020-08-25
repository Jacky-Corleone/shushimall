package com.camelot.sellercenter.malladvertise.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: guomaomao@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallAdInDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;//主键ID
	private Integer adType;//广告类型   暂时前台写死：1，轮播下广告位  2，登录   3 头部广告  4 类目广告
	private String title;//广告标题
	private Integer sortNumber;//排序号
	private String adSrc;//图片URL
	private String adURL;//广告指向链接
	private String publishFlag;//发布标记   0：即时发布  1：定时发布
	private Date startTimeShow;//页面展示时间
	private Date endTimeShow;//页面展示时间
	private Date startTime;//开始展示时间
	private Date endTime;//展示结束时间
	private Integer close; //是否可关闭 1 可关闭 2 不可关闭
	private Long cid; //类目id （一级类目）
	private Integer status;//0:未发布，1已发布
	private Integer themeId;  //主题id
    private Integer advType;//主题类型
	private Integer integral;// 积分
	private Long skuId;// skuId
	private String applicableType;//适用户型
	private String titleDescription;//广告语
	private String titleRemarks;//主题备注
	private Integer isClick;// 是否可点击 1 是  2否
	private String priceColour;//价格颜色
	private String adMiddleSrc;//中心图片地址
	private String adBackSrc;//背景图片地址
	private String lrStyle;//左右风格 1左面2右,默认为左
	
	
	public String getPriceColour() {
		return priceColour;
	}
	public void setPriceColour(String priceColour) {
		this.priceColour = priceColour;
	}
	public Integer getClose() {
		return close;
	}
	public void setClose(Integer close) {
		this.close = close;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAdType() {
		return adType;
	}
	public void setAdType(Integer adType) {
		this.adType = adType;
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
	public String getAdSrc() {
		return adSrc;
	}
	public void setAdSrc(String adSrc) {
		this.adSrc = adSrc;
	}
	public String getAdURL() {
		return adURL;
	}
	public void setAdURL(String adURL) {
		this.adURL = adURL;
	}
	public String getPublishFlag() {
		return publishFlag;
	}
	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getThemeId() {
		return themeId;
	}
	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}
	public Integer getAdvType() {
		return advType;
	}
	public void setAdvType(Integer advType) {
		this.advType = advType;
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
	public String getApplicableType() {
		return applicableType;
	}
	public void setApplicableType(String applicableType) {
		this.applicableType = applicableType;
	}
	public String getTitleDescription() {
		return titleDescription;
	}
	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}
	public String getTitleRemarks() {
		return titleRemarks;
	}
	public void setTitleRemarks(String titleRemarks) {
		this.titleRemarks = titleRemarks;
	}
	public Integer getIsClick() {
		return isClick;
	}
	public void setIsClick(Integer isClick) {
		this.isClick = isClick;
	}
	public String getAdMiddleSrc() {
		return adMiddleSrc;
	}
	public void setAdMiddleSrc(String adMiddleSrc) {
		this.adMiddleSrc = adMiddleSrc;
	}
	public String getAdBackSrc() {
		return adBackSrc;
	}
	public void setAdBackSrc(String adBackSrc) {
		this.adBackSrc = adBackSrc;
	}
	public String getLrStyle() {
		return lrStyle;
	}
	public void setLrStyle(String lrStyle) {
		this.lrStyle = lrStyle;
	}
}
