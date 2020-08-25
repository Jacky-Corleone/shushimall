package com.camelot.usercenter.dto.contract;

import java.io.Serializable;
/**
 * 用户合同审核记录对象
 * 
 * @author learrings
 */
public class UserContractAuditDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
	private java.lang.Long caId;//  caId
	private java.lang.Long cid;//  合同表主键
	private java.lang.String auditId;//  审核人id
	private java.util.Date auditDate;//  审核时间
	private java.lang.String remark;//  备注
	private Integer status;//  REJECT (0, "已驳回" ), UPLOAD(1, "待上传" ), UNPASS(2, "待审核" ), PASS(3, "已确认" );
	
	public java.lang.Long getCaId() {
		return this.caId;
	}

	public void setCaId(java.lang.Long value) {
		this.caId = value;
	}
	
	public java.lang.Long getCid() {
		return this.cid;
	}

	public void setCid(java.lang.Long value) {
		this.cid = value;
	}
	
	public java.lang.String getAuditId() {
		return this.auditId;
	}

	public void setAuditId(java.lang.String value) {
		this.auditId = value;
	}
	
	public java.util.Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(java.util.Date value) {
		this.auditDate = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}

	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

}

