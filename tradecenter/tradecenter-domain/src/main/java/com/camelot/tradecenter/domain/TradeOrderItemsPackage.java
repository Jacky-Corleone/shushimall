package com.camelot.tradecenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>Description: [套装商品订单记录]</p>
 * Created on 2016年2月27日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class TradeOrderItemsPackage implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;
	private Long id;// 主键
	private String orderId;// 订单ID
	private Long orderItemsId;// 订单商品ID
	private Long itemId;// 套装商品Id
	private Long skuId;// 套装商品skuId
	private Long subItemId;// 套装子item_id/服务item_id
	private Long subSkuId;// 套装子sku_id/服务sku_id
	private Integer addSource;// 1：普通商品 2：平台上传 3：套装商品 4：基础服务商品 5：增值服务商品 6：辅助材料商品
	private BigDecimal subSkuPrice;//套装单品/服务的sku价格
	private Integer subNum;//套装单品/服务数量
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	
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

	public Long getOrderItemsId() {
		return orderItemsId;
	}

	public void setOrderItemsId(Long orderItemsId) {
		this.orderItemsId = orderItemsId;
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

	public Long getSubItemId() {
		return subItemId;
	}

	public void setSubItemId(Long subItemId) {
		this.subItemId = subItemId;
	}

	public Long getSubSkuId() {
		return subSkuId;
	}

	public void setSubSkuId(Long subSkuId) {
		this.subSkuId = subSkuId;
	}

	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
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

	public BigDecimal getSubSkuPrice() {
		return subSkuPrice;
	}

	public void setSubSkuPrice(BigDecimal subSkuPrice) {
		this.subSkuPrice = subSkuPrice;
	}

	public Integer getSubNum() {
		return subNum;
	}

	public void setSubNum(Integer subNum) {
		this.subNum = subNum;
	}

}
