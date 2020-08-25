package com.camelot.pricecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>Description: [运费模版对应的优惠信息]</p>
 * Created on 2015年11月25日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class DeliveryTypePreferentialWayDTO implements Serializable {

	private static final long serialVersionUID = 4731211961825561689L;

	private Long id;// 优惠方式id，主键

	private Long templateId;// 运费模板id

	private Integer deliveryType;// 优惠方式，1件数，2重量，3体积 4金额

	private Integer strategy;// 策略，1满减，2包邮

	private BigDecimal full;// 满多少件/重量/体积

	private BigDecimal reduce;// 减多少钱（元）

	private Long shopId;// 店铺id

	private Long sellerId;// 卖家id

	private Date createTime;// 优惠方式创建时间

	private Date updateTime;// 优惠方式修改时间

	private String delState;// 是否删除（假删）1，未删，2已删

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getStrategy() {
		return strategy;
	}

	public void setStrategy(Integer strategy) {
		this.strategy = strategy;
	}

	public BigDecimal getFull() {
		return full;
	}

	public void setFull(BigDecimal full) {
		this.full = full;
	}

	public BigDecimal getReduce() {
		return reduce;
	}

	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
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

	public String getDelState() {
		return delState;
	}

	public void setDelState(String delState) {
		this.delState = delState;
	}

}
