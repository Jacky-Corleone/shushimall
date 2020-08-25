package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Description: [商品状态修改参数]</p>
 * Created on 2015-3-16
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemStatusModifyDTO implements Serializable {

	private static final long serialVersionUID = -629335152632348976L;

	private List<Long> itemIds = new ArrayList<Long>();//商品ID  必填
	private String changeReason;//状态更改原因  非必填
	private Integer itemStatus;//商品状态 必填
	private Long userId;//操作人ID 必填
	private Integer operator;//1商家  2平台
	private Long shopId;//店铺ID
	private boolean createPlatItem;//是否加入平台商品库
	
	public List<Long> getItemIds() {
		return itemIds;
	}
	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public boolean isCreatePlatItem() {
		return createPlatItem;
	}
	public void setCreatePlatItem(boolean createPlatItem) {
		this.createPlatItem = createPlatItem;
	}
	
}
