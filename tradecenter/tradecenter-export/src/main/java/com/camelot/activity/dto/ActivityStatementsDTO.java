package com.camelot.activity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 
 * <p>Description: [王鹏]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ActivityStatementsDTO implements Serializable{
	private static final long serialVersionUID = -6273882930602536329L;
	//活动结束主键id
	private Long id;
	//订单id
	private String orderId;
	//店铺id
	private Long shopId;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//活动总优惠金额
	private BigDecimal totalDiscountAmount;
	//总退款金额
	private BigDecimal totalRefundAmount;
	//活动结算总金额
	private BigDecimal totalSettleAmount;
	//状态1：有效  2：无效
	private Integer state;
	//店铺名称
	private String shopName;
    //创建开始时间
	private String createdBegin;
    //创建结束时间
	private String createdEnd;
	// 店铺ID
	private List<Long> shopIds;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public BigDecimal getTotalDiscountAmount() {
		return totalDiscountAmount;
	}
	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}
	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
	public java.lang.String getCreatedBegin() {
		return createdBegin;
	}
	public void setCreatedBegin(java.lang.String createdBegin) {
		this.createdBegin = createdBegin;
	}
	public java.lang.String getCreatedEnd() {
		return createdEnd;
	}
	public void setCreatedEnd(java.lang.String createdEnd) {
		this.createdEnd = createdEnd;
	}
	public List<Long> getShopIds() {
		return shopIds;
	}
	public void setShopIds(List<Long> shopIds) {
		this.shopIds = shopIds;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public BigDecimal getTotalSettleAmount() {
		return totalSettleAmount;
	}
	public void setTotalSettleAmount(BigDecimal totalSettleAmount) {
		this.totalSettleAmount = totalSettleAmount;
	}
	
}
