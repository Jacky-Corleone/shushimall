package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 
 * <p>Description: [QQ客服DTO]</p>
 * Created on 2016年2月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class QqCustomerDTO implements Serializable {
	private static final long serialVersionUID = -6998853737950772335L;
	
	private Long id; // 主键
	private Integer customerType; // '客服类型：0平台客服，1店铺客服'
	private Long userId; // 卖家ID
	private Long shopId; // 店铺ID
	private String shopName; // 店铺名称
	private String customerQqNumber; // 客服腾讯账号
	private Integer isDefault; // 是否默认客服 0 否 1是
	private Integer customerSortNumber; // '客服编号：从0开始'
	private Long lastOperatorId;// 最后操作人ID
	private Date createDate;// 创建日期
	private Date lastUpdateDate;// 最后修改日期
	private Integer deletedFlag;// '是否逻辑删除标记：0在用，未伪删除；1停用，已伪删除'
	
	private List<Long> userIds ;//卖家ID集合
	private List<Long> shopIds ;//店铺ID集合
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCustomerQqNumber() {
		return customerQqNumber;
	}

	public void setCustomerQqNumber(String customerQqNumber) {
		this.customerQqNumber = customerQqNumber;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getCustomerSortNumber() {
		return customerSortNumber;
	}

	public void setCustomerSortNumber(Integer customerSortNumber) {
		this.customerSortNumber = customerSortNumber;
	}

	public Long getLastOperatorId() {
		return lastOperatorId;
	}

	public void setLastOperatorId(Long lastOperatorId) {
		this.lastOperatorId = lastOperatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Integer getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(Integer deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getShopIds() {
		return shopIds;
	}

	public void setShopIds(List<Long> shopIds) {
		this.shopIds = shopIds;
	}

}
