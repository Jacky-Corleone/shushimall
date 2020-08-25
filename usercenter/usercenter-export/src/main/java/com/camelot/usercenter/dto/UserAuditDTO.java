package com.camelot.usercenter.dto;

import java.io.Serializable;
/**
 * 用户审核表
 *
 * @author learrings
 */
public class UserAuditDTO  implements Serializable {
    
	private static final long serialVersionUID = 8111769038508131796L;
	private java.lang.Long id;//  id
	private String auditorId;//  审核人id
	private java.util.Date time;//  审核时间
	private java.lang.String remarks;//  审核备注
	private java.lang.Long contextId;//  审核对象id(用户id)
	private java.lang.Integer result;//  审核结果:0:驳回1:待审核2:通过，
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	
	public java.util.Date getTime() {
		return this.time;
	}

	public void setTime(java.util.Date value) {
		this.time = value;
	}

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	public java.lang.Long getContextId() {
		return this.contextId;
	}

	public void setContextId(java.lang.Long value) {
		this.contextId = value;
	}

	public java.lang.Integer getResult() {
		return this.result;
	}

	public void setResult(java.lang.Integer value) {
		this.result = value;
	}

}

