package com.camelot.usercenter.dto.audit;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *  
 *  @author 
 *  @createDate 
 **/
public class UserModifyInfoDTO implements Serializable {

	private static final long serialVersionUID = 2411042107299670558L;
	private java.lang.Long id;//  id
	private java.lang.Integer userType;//  用户类型
	private java.lang.String applicantName;//  申请人名称(商家名称)
	private java.lang.Long applicantUserid;//  申请人id(商家编号)
	private Integer applyStatus;//  申请所处状态 0, 新建(待审核); 1, 审核通过  2, 审核不通过;
	private java.util.Date createTime;//  申请创建时间
	private java.lang.String modifyTable;//  修改的表名
	private java.util.Date modifyTime;//  修改时间 也是审核时间
	private String remark; //审核备注
	private String modifyType; //商家修改类型 文本
	private java.lang.String deletedFlag;//  删除标记  1 删除 0 未删除

	public java.lang.String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(java.lang.String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.Integer getUserType() {
		return this.userType;
	}

	public void setUserType(java.lang.Integer value) {
		this.userType = value;
	}

	public java.lang.Long getApplicantUserid() {
		return this.applicantUserid;
	}

	public void setApplicantUserid(java.lang.Long value) {
		this.applicantUserid = value;
	}

	public Integer getApplyStatus() {
		return this.applyStatus;
	}

	public void setApplyStatus(Integer value) {
		this.applyStatus = value;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

	public java.lang.String getModifyTable() {
		return this.modifyTable;
	}

	public void setModifyTable(java.lang.String value) {
		this.modifyTable = value;
	}

	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(java.util.Date value) {
		this.modifyTime = value;
	}

	public java.lang.String getApplicantName() {
		return this.applicantName;
	}

	public void setApplicantName(java.lang.String value) {
		this.applicantName = value;
	}
}
