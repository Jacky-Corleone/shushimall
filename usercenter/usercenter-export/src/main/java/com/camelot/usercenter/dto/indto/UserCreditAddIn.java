package com.camelot.usercenter.dto.indto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [用户积分添加入参]</p>
 * Created on 2015-4-14
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserCreditAddIn implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;  //用户ID
	private Integer sorceType; //积分渠道 0：平台系统 1：订单 
	private Long credit; //变化积分，分正负
	private String description; //积分变化描述
	
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getSorceType() {
		return sorceType;
	}
	public void setSorceType(Integer sorceType) {
		this.sorceType = sorceType;
	}
	public Long getCredit() {
		return credit;
	}
	public void setCredit(Long credit) {
		this.credit = credit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
