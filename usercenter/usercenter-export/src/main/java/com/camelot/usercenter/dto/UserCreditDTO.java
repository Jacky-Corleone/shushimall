package com.camelot.usercenter.dto;

import java.io.Serializable;

import com.camelot.openplatform.common.DataGrid;
/**
 * 
 * <p>Description: [用户积分]</p>
 * Created on 2015-4-14
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserCreditDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  ID
		private java.lang.Long userId;//  用户ID
		private java.lang.Long credit;//  积分
		private java.util.Date modified;//  修改时间
		private java.util.Date created;//  创建时间
		private Integer status;//  0：无效 1：有效
		
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.Long getCredit() {
		return this.credit;
	}

	public void setCredit(java.lang.Long value) {
		this.credit = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

}

