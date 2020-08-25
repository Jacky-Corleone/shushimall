package com.camelot.usercenter.dto.fieldIdentification;

import java.io.Serializable;
import java.util.Date;

/**
 * 卖家实地认证审核DTO
 * 董其超
 * 2015年7月17日
 */
public class FieldIdentificationAuditDTO implements Serializable {

    private static final long serialVersionUID = -4467302356848015688L;

    private Long id;//主键
    private Long userId; //卖家ID
	private Long extendId; //卖家扩展ID
	private String companyName; //公司名
	private Integer auditStatus; //实地认证审核状态：0待受理，1待审核，2审核通过，3审核驳回, 4已受理
	private String auditorId;//审核人ID
	private String auditRemark;//审核备注
	private Date createDate;//创建时间
	private String createDateBegin; //创建时间开始
    private String createDateEnd; //创建时间结束
	private Date updateDate;//修改时间
	private Integer deleteFlag;//逻辑删除标记：0未逻辑删除，1已逻辑删除

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getExtendId() {
		return extendId;
	}
	public void setExtendId(Long extendId) {
		this.extendId = extendId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateDateBegin() {
		return createDateBegin;
	}
	public void setCreateDateBegin(String createDateBegin) {
		this.createDateBegin = createDateBegin;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
