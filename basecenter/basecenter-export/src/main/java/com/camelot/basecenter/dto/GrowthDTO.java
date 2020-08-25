package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * <p>Description: [成长值]</p>
 * Created on 2015-12-4
 * @author  <a href="mailto: fandongzang98@camelotchina.com">范东藏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class GrowthDTO implements Serializable{

	
	private static final long serialVersionUID = 4259751446193001844L;
	private Long id;//id
	private Integer type;//获取积分类型
	private BigDecimal growthValue;//获取积分成长值或百分比
	private Integer userLevel;//用户等级
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getGrowthValue() {
		return growthValue;
	}
	public void setGrowthValue(BigDecimal growthValue) {
		this.growthValue = growthValue;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	
	
	
}
