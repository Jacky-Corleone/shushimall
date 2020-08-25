package com.camelot.mall.shopcart;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
 * <p>Description: 商品可参加的优惠活动</p>
 * Created on 2015年3月7日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class Promotion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 239720720234441284L;
	private Long id;//  主键
	private String name;//  活动名称
	private Integer type;//  促销类型，1：直降，2：满减
	private BigDecimal price;//  直降为优惠后的单价；满减为满减金额

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
}
