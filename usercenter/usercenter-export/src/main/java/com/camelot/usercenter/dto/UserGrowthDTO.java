package com.camelot.usercenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>Description: [成长值增长的记录表]</p>
 * Created on 2015-12-8
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserGrowthDTO implements Serializable{
	
	/**
	 * <p>Discription:[字段功能描述]</p>
	 */
	private static final long serialVersionUID = 1L;
	public int id;   //id
	public int userId;   //用户id
	public String type;   //成长值的成长类型
	public BigDecimal growthValue; //成长数值
	public Date createTime; //创建时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getGrowthValue() {
		return growthValue;
	}
	public void setGrowthValue(BigDecimal growthValue) {
		this.growthValue = growthValue;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

}
