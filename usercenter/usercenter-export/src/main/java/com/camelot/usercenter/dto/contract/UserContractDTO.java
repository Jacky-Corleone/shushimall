package com.camelot.usercenter.dto.contract;

import java.io.Serializable;

import com.camelot.openplatform.common.DataGrid;
/**
 * 合同基础类
 * 
 * @author learrings
 */
public class UserContractDTO  implements Serializable {
    
	private static final long serialVersionUID = -7150087152059882609L;
	private java.lang.Long id;//  id
	private java.lang.String contractId;//  合同编号
	private java.lang.Long shopId;//  店铺编号
	private Integer contractType;//  合同类型（1：商家入驻，2：续签，3、调整类目）
	private java.util.Date contractStarttime;//  合同生效开始时间
	private java.util.Date contractEndtime;//  合同生效结束时间
	private java.lang.Long creatorId;//  合同创建人
	private java.lang.String contractJssAddr;//  合同存储地址
	private Integer contractStatus;// 合同状态  (0, "已驳回"), (1, "待上传"), (2, "待审核"),(3, "已确认")
	private java.lang.String contractRemark;//  合同备注
	private java.util.Date createdTime;//  创建时间
	private java.util.Date modified;//  修改时间
	private String auditRemark; //审核备注
	private DataGrid<UserContractAuditDTO> dataGridContractAudit;// 合同审核历史记录信息组
	private java.lang.String companyName;//  公司名称
	private String createdBegin;//  创建时间开始时间
	private String createdEnd;//  创建时间结束时间
	
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.String getContractId() {
		return this.contractId;
	}

	public void setContractId(java.lang.String value) {
		this.contractId = value;
	}
	
	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}
	
	public Integer getContractType() {
		return this.contractType;
	}

	public void setContractType(Integer value) {
		this.contractType = value;
	}
	
	public java.util.Date getContractStarttime() {
		return this.contractStarttime;
	}

	public void setContractStarttime(java.util.Date value) {
		this.contractStarttime = value;
	}
	
	public java.util.Date getContractEndtime() {
		return this.contractEndtime;
	}

	public void setContractEndtime(java.util.Date value) {
		this.contractEndtime = value;
	}
	
	public java.lang.Long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(java.lang.Long value) {
		this.creatorId = value;
	}
	
	public java.lang.String getContractJssAddr() {
		return this.contractJssAddr;
	}

	public void setContractJssAddr(java.lang.String value) {
		this.contractJssAddr = value;
	}
	
	public Integer getContractStatus() {
		return this.contractStatus;
	}

	public void setContractStatus(Integer value) {
		this.contractStatus = value;
	}
	
	public java.lang.String getContractRemark() {
		return this.contractRemark;
	}

	public void setContractRemark(java.lang.String value) {
		this.contractRemark = value;
	}
	
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(java.util.Date value) {
		this.createdTime = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

	public DataGrid<UserContractAuditDTO> getDataGridContractAudit() {
		return dataGridContractAudit;
	}

	public void setDataGridContractAudit(
			DataGrid<UserContractAuditDTO> dataGridContractAudit) {
		this.dataGridContractAudit = dataGridContractAudit;
	}

	public java.lang.String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(java.lang.String companyName) {
		this.companyName = companyName;
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

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

}

