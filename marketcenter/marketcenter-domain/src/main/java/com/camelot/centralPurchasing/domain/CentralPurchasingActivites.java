package com.camelot.centralPurchasing.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * 集采活动
 * @author 周志军
 *
 */
public class CentralPurchasingActivites implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //主键
    private Long centralPurchasingId;
    //活动编号
    private String activityNum;
    //活动名称
    private String activityName;
    //活动图片
    private String activityImg;
    //图片链接地址
    private String refUrl;
    //活动开始时间
    private Date activityBeginTime;
    //活动报名开始时间
    private Date activitySignUpTime;
    //活动报名结束时间
    private Date activitySignUpEndTime;
    //活动结束时间
    private Date activityEndTime;
    //0、未发布；1、已发布；2、已售罄；3、活动中止；4、已删除
    private Integer activityStatus;
    //1、平台；2、店铺
    private Integer activityType;
    //店铺ID
    private Long shopId;
    //0、科印平台；2、绿印平台
    private Integer platformId;
    //插入时间
    private Date insertTime;
    //插入人
    private Long insertBy;
    //更新时间
    private Date updateTime;
    //更新人
    private Long updateBy;
	public Long getCentralPurchasingId() {
		return centralPurchasingId;
	}
	public void setCentralPurchasingId(Long centralPurchasingId) {
		this.centralPurchasingId = centralPurchasingId;
	}
	public String getActivityNum() {
		return activityNum;
	}
	public void setActivityNum(String activityNum) {
		this.activityNum = activityNum;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityImg() {
		return activityImg;
	}
	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}
	public Date getActivityBeginTime() {
		return activityBeginTime;
	}
	public void setActivityBeginTime(Date activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}
	public Date getActivitySignUpTime() {
		return activitySignUpTime;
	}
	public void setActivitySignUpTime(Date activitySignUpTime) {
		this.activitySignUpTime = activitySignUpTime;
	}
	public Date getActivitySignUpEndTime() {
		return activitySignUpEndTime;
	}
	public void setActivitySignUpEndTime(Date activitySignUpEndTime) {
		this.activitySignUpEndTime = activitySignUpEndTime;
	}
	public Date getActivityEndTime() {
		return activityEndTime;
	}
	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	public Integer getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Long getInsertBy() {
		return insertBy;
	}
	public void setInsertBy(Long insertBy) {
		this.insertBy = insertBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	
}
