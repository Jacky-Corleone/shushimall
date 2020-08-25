package com.camelot.pricecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** 
 * <p>Description: 商品可参加的优惠活动</p>
 * Created on 2015年3月7日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class PromotionDTO implements Serializable, Comparable<PromotionDTO> {
	private static final long serialVersionUID = -1896172897174859290L;
	private Long id;//  主键
	private String name;//  活动名称
	private Integer type;//  促销类型，1：直降，2：满减
	private BigDecimal price;//  直降为优惠后的单价；满减为满减金额
	private Long shopId;//店铺id
	private BigDecimal meetPrice;//  满足金额（当活动为满减活动时有值）
	private BigDecimal discountPrice;//  优惠金额（当活动为满减活动时有值）
	private List<Long> shopIdList;//平台活动参加的店铺id集合
	private Integer  downType;//直降类型 满减活动为空

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Override
	public int compareTo(PromotionDTO arg0) {
		return this.getPrice().compareTo(arg0.getPrice());
	}

	public BigDecimal getMeetPrice() {
		return meetPrice;
	}

	public void setMeetPrice(BigDecimal meetPrice) {
		this.meetPrice = meetPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public List<Long> getShopIdList() {
		return shopIdList;
	}

	public void setShopIdList(List<Long> shopIdList) {
		this.shopIdList = shopIdList;
	}

	public Integer getDownType() {
		return downType;
	}

	public void setDownType(Integer downType) {
		this.downType = downType;
	}
	
}
