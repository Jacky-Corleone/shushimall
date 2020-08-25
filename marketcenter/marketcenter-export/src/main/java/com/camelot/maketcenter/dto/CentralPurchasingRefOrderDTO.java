package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>
 * Description: [集采订单DTO]
 * </p>
 * Created on 2015年12月17日
 * 
 * @author <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CentralPurchasingRefOrderDTO implements Serializable {

	private static final long serialVersionUID = -4994801549808777767L;
	// 主键id
	private Long orderActivitiesDetailId;
	// 集采详情ID
	private Long activitesDetailsId;
	// 订单id
	private String orderId;
	// 商品ID
	private Long itemId;
	// skuId
	private Long skuId;
	// 购买数量
	private Integer purchaseNum;
	// 购买价格
	private BigDecimal purchasePrice;
	// 插入时间
	private Date insertTime;
	// 插入人
	private Long insertBy;
	// 插入时间
	private Date updateTime;
	// 更新人
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
