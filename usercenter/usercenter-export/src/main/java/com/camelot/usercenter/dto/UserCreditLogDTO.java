package com.camelot.usercenter.dto;

import java.io.Serializable;
/**
 * 
 * <p>Description: [积分查询明细]</p>
 * Created on 2015-4-14
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserCreditLogDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  ID
		private java.lang.Long creditId;//  积分ID
		private java.lang.Integer sorceType;//  积分渠道 0：平台系统 1：订单 
		private java.lang.Long credit;//  变化积分，分正负
		private java.lang.String description;//  积分变化描述
		private java.util.Date created;//  创建时间
	
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getCreditId() {
		return this.creditId;
	}

	public void setCreditId(java.lang.Long value) {
		this.creditId = value;
	}
	
	public java.lang.Integer getSorceType() {
		return this.sorceType;
	}

	public void setSorceType(java.lang.Integer value) {
		this.sorceType = value;
	}
	
	public java.lang.Long getCredit() {
		return this.credit;
	}

	public void setCredit(java.lang.Long value) {
		this.credit = value;
	}
	
	public java.lang.String getDescription() {
		return this.description;
	}

	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

}

