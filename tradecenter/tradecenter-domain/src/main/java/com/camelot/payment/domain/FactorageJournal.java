package com.camelot.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.camelot.openplatform.common.PropertyMapping;
/**
 * 
 * @author
 * 
 */
public class FactorageJournal  implements Serializable {
    
	private static final long serialVersionUID = -1841880440994122626L;
	@PropertyMapping("id")
	private java.lang.Long id;//  主键
	@PropertyMapping("orderNo")
	private java.lang.String orderNo;//  订单编号
	@PropertyMapping("factorage")
	private BigDecimal factorage;//  手续费
	@PropertyMapping("reproNo")
	private java.lang.String reproNo;//  退货单
	private java.lang.Integer status;//  状态
	@PropertyMapping("created")
	private java.util.Date created;//  创建时间
	@PropertyMapping("updated")
	private java.util.Date updated;//  修改时间
	@PropertyMapping("remark1")
	private String remark1;//  备注1
	@PropertyMapping("remark2")
	private String remark2;//  备注2
	
	
	private String createdBegin;//  创建开始时间
	private String createdEnd;//  创建结束时间
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getFactorage() {
		return this.factorage;
	}
	public void setFactorage(BigDecimal factorage) {
		this.factorage = factorage;
	}
	public java.lang.Integer getStatus() {
		return this.status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public java.util.Date getCreated() {
		return this.created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
	public java.util.Date getUpdated() {
		return this.updated;
	}
	public void setUpdated(java.util.Date updated) {
		this.updated = updated;
	}
	public String getCreatedBegin() {
		return createdBegin;
	}
	public void setCreatedBegin(String createdBegin) {
		this.createdBegin = createdBegin;
	}
	public String getCreatedEnd() {
		return createdEnd;
	}
	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = createdEnd;
	}
	public java.lang.String getReproNo() {
		return reproNo;
	}
	public void setReproNo(java.lang.String reproNo) {
		this.reproNo = reproNo;
	}

}

