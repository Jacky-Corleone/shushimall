package com.camelot.centralPurchasing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 集采活动
 * @author 周志军
 *
 */
public class CentralPurchasingRefOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	//主键
    private Long orderActivitiesDetailId;
	//集采详情ID
    private Long activitesDetailsId;
	//订单ID
    private String orderId;
	//商品ID
    private Long itemId;
	//skuID
    private Long skuId;
	//购买数量
    private Integer purchaseNum;
	//购买价格
    private BigDecimal purchasePrice;
	//插入时间
    private Date insertTime;
	//插入人
    private Long insertBy;
	//更新时间
    private Date updateTime;
	//更新人
    private Long updateBy;
	public Long getOrderActivitiesDetailId() {
		return orderActivitiesDetailId;
	}
	public void setOrderActivitiesDetailId(Long orderActivitiesDetailId) {
		this.orderActivitiesDetailId = orderActivitiesDetailId;
	}
	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}
	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public Integer getPurchaseNum() {
		return purchaseNum;
	}
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
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
}
