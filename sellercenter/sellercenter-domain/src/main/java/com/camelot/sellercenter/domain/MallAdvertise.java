package com.camelot.sellercenter.domain;

import java.util.Date;

/**
 * 
 * <p>Description: MallAdvertise domain类</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: guomaomao@camelotchina.com">郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class MallAdvertise implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;//
	private Integer sortNum;//序号
	private Integer adType;//广告类型  1，轮播下广告位  2，登录   3 头部广告  4 类目广告
	private String adTitle;//广告标题
	private String adUrl;//广告指向链接
	private String adSrc;//图片链接
	private Date startTime;//定时发布时间
	private Date endTime;//定时结束时间
	private Date created;//创建时间
	private Date modified;//修改时间
	private Double platformId;//平台ID
	private Integer status;//是否可用 1 发布  0 未发布
	private Integer timeFlag;//时间标记 1 创建时间  2 发布时间 3结束时间
	private Integer close; //是否可关闭 1 可关闭 2 不可关闭
	private Long cid; //类目id （一级类目）
	private Integer advType;//频道类型 1：首页 2：类目 3 地区  4积分商城
	private Integer integral;// 积分
	private Long skuId;// skuId
	private Integer themeId;//主题ID
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

	// setter and getter
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public Integer getSortNum(){
		return sortNum;
	}
	
	public void setSortNum(Integer sortNum){
		this.sortNum = sortNum;
	}
	public Integer getAdType(){
		return adType;
	}
	
	public void setAdType(Integer adType){
		this.adType = adType;
	}
	public String getAdTitle(){
		return adTitle;
	}
	
	public void setAdTitle(String adTitle){
		this.adTitle = adTitle;
	}
	public String getAdUrl(){
		return adUrl;
	}
	
	public void setAdUrl(String adUrl){
		this.adUrl = adUrl;
	}
	public String getAdSrc(){
		return adSrc;
	}
	
	public void setAdSrc(String adSrc){
		this.adSrc = adSrc;
	}
	public Date getStartTime(){
		return startTime;
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	public Date getCreated(){
		return created;
	}
	
	public void setCreated(Date created){
		this.created = created;
	}
	public Date getModified(){
		return modified;
	}
	
	public void setModified(Date modified){
		this.modified = modified;
	}
	public Double getPlatformId(){
		return platformId;
	}
	
	public void setPlatformId(Double platformId){
		this.platformId = platformId;
	}
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(Integer timeFlag) {
		this.timeFlag = timeFlag;
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
	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
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
