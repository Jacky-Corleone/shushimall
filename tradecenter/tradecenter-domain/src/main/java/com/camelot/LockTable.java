package com.camelot;

import java.io.Serializable;
/**
 * 
 * @author
 * 
 */
public class LockTable  implements Serializable {
    
	private static final long serialVersionUID = -1094700640355972181L;
	private java.lang.Long id;//  id
	private java.lang.String tableNm;//  锁表字段
	private java.lang.Integer status;//  状态 1[加锁]
	private java.lang.Integer batch;//  批次
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.String getTableNm() {
		return this.tableNm;
	}

	public void setTableNm(java.lang.String value) {
		this.tableNm = value;
	}
	
	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	
	public java.lang.Integer getBatch() {
		return this.batch;
	}

	public void setBatch(java.lang.Integer value) {
		this.batch = value;
	}

}

