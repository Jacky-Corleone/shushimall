package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 集采活动
 * @author 周志军
 *
 */
public class QueryCentralPurchasingDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//集采活动主键
    private Long centralPurchasingId;
    //活动编号
    private String activityNum;
    //活动名称
    private String activityName;
    //活动图片
    private String activityImg;
    //图片链接
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
    //in活动状态列表
    private List<Integer> activityStatusList = new ArrayList<Integer>();
    //not in活动状态列表
    private List<Integer> notActivityStatusList = new ArrayList<Integer>();
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
    // 活动详情ID
    private Long activitesDetailsId;
	//itemID
    private Long itemId;
    //itemIDs
    private List<Long> itemIds;
    //skuID
    private Long skuId;
	//集采预估价
    private String estimatePrice;
	//集采价
    private BigDecimal centralPurchasingPrice;
	//原价
    private BigDecimal originalPrice;
	//已报名人数
    private Integer signUpNum;
	//已下单人数
    private Integer placeOrderNum;
	//已付款人数
    private Integer paidNum;
	//每个用户可购买的最大数量
    private Integer perPerchaseMaxNum;
	//发布团购的最大数量
    private Integer releaseGoodsMaxNum;
    
    private Long cid;//类目ID 三级类目
    private List<Long> cids;//三级类目组
    private Integer detailedStatus;// 比activityStatus更加详细的状态
    
    private String itemName;//  商品名称(item)
    private List<ItemAttr> itemAttr;//商品属性（商品信息）
    
    private Integer orderByType;
    
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
	public List<Integer> getActivityStatusList() {
		return activityStatusList;
	}
	public void setActivityStatusList(List<Integer> activityStatusList) {
		this.activityStatusList = activityStatusList;
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
	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}
	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getEstimatePrice() {
		return estimatePrice;
	}
	public void setEstimatePrice(String estimatePrice) {
		this.estimatePrice = estimatePrice;
	}
	public BigDecimal getCentralPurchasingPrice() {
		return centralPurchasingPrice;
	}
	public void setCentralPurchasingPrice(BigDecimal centralPurchasingPrice) {
		this.centralPurchasingPrice = centralPurchasingPrice;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Integer getSignUpNum() {
		return signUpNum;
	}
	public void setSignUpNum(Integer signUpNum) {
		this.signUpNum = signUpNum;
	}
	public Integer getPlaceOrderNum() {
		return placeOrderNum;
	}
	public void setPlaceOrderNum(Integer placeOrderNum) {
		this.placeOrderNum = placeOrderNum;
	}
	public Integer getPaidNum() {
		return paidNum;
	}
	public void setPaidNum(Integer paidNum) {
		this.paidNum = paidNum;
	}
	public Integer getPerPerchaseMaxNum() {
		return perPerchaseMaxNum;
	}
	public void setPerPerchaseMaxNum(Integer perPerchaseMaxNum) {
		this.perPerchaseMaxNum = perPerchaseMaxNum;
	}
	public Integer getReleaseGoodsMaxNum() {
		return releaseGoodsMaxNum;
	}
	public void setReleaseGoodsMaxNum(Integer releaseGoodsMaxNum) {
		this.releaseGoodsMaxNum = releaseGoodsMaxNum;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public List<Long> getCids() {
		return cids;
	}
	public void setCids(List<Long> cids) {
		this.cids = cids;
	}
	public Integer getDetailedStatus() {
		return detailedStatus;
	}
	public void setDetailedStatus(Integer detailedStatus) {
		this.detailedStatus = detailedStatus;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	public List<Integer> getNotActivityStatusList() {
		return notActivityStatusList;
	}
	public void setNotActivityStatusList(List<Integer> notActivityStatusList) {
		this.notActivityStatusList = notActivityStatusList;
	}
	public List<Long> getItemIds() {
		return itemIds;
	}
	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<ItemAttr> getItemAttr() {
		return itemAttr;
	}
	public void setItemAttr(List<ItemAttr> itemAttr) {
		this.itemAttr = itemAttr;
	}
	public Integer getOrderByType() {
		return orderByType;
	}
	public void setOrderByType(Integer orderByType) {
		this.orderByType = orderByType;
	}
	
}
