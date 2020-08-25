package com.camelot.centralPurchasing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 集采详情
 * @author 周志军
 *
 */
public class CentralPurchasingActivitesDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	//主键
    private Long activitesDetailsId;
	//集采活动ID
    private Long centralPurchasingId;
	//skuID
    private Long skuId;
    //itemID
    private Long itemId;
    //类目ID 三级类目
    private Long cid;
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
	//插入时间
    private Date insertTime;
	//插入人
    private Long insertBy;
	//更新时间
    private Date updateTime;
	//更新人
    private Long updateBy;
	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}
	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}
	public Long getCentralPurchasingId() {
		return centralPurchasingId;
	}
	public void setCentralPurchasingId(Long centralPurchasingId) {
		this.centralPurchasingId = centralPurchasingId;
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
	public void setOriginalPriceTime(BigDecimal originalPrice) {
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
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

}
