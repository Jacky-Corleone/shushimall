package com.camelot.usercenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分轨迹
 * 
 * @author - 周志军
 * @createDate - 2015-12-7
 */
public class UserIntegralTrajectoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	private Long id;
	// 积分类型:1、金额返还积分；2、评价获取积分；3、订单使用积分；4、积分兑换金额；5、退款返还积分
	private Integer integralType;
	// 订单ID
	private String orderId;
	// 积分数
	private Long integralValue;
	// 使用时间
	private Date usingTime;
	// 用户
	private Long userId;
	// 使用店铺
	private Long shopId;
	// 插入时间
	private Date insertTime;
	// 插入人
	private Long insertBy;
	// 更新时间
	private Date updateTime;
	// 更新人
	private Long updateBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIntegralType() {
		return integralType;
	}

	public void setIntegralType(Integer integralType) {
		this.integralType = integralType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getIntegralValue() {
		return integralValue;
	}

	public void setIntegralValue(Long integralValue) {
		this.integralValue = integralValue;
	}

	public Date getUsingTime() {
		return usingTime;
	}

	public void setUsingTime(Date usingTime) {
		this.usingTime = usingTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
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
