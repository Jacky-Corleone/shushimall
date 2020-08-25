package com.camelot.mall.shopcart;

import java.math.BigDecimal;

/**
 * 
 * <p>Description: [用于计算使用同一个运费模版的商品的运费]</p>
 * Created on 2015年11月3日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class GroupProduct implements java.io.Serializable {
	private static final long serialVersionUID = -3388749122097546332L;
	private Long shopFreightTemplateId; // 运费模版ID
	private String regionId; // 区域编码
	private BigDecimal totalPrice = BigDecimal.ZERO; // 总价格
	private Integer totalQty = 0;// 总数量
	private BigDecimal totalWeight = BigDecimal.ZERO; // 总重量
	private BigDecimal totalVolume = BigDecimal.ZERO; // 总体积
	private Integer deliveryType; // 运送方式：1快递，2EMS，3平邮
	
	public Long getShopFreightTemplateId() {
		return shopFreightTemplateId;
	}

	public void setShopFreightTemplateId(Long shopFreightTemplateId) {
		this.shopFreightTemplateId = shopFreightTemplateId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}
	
}
