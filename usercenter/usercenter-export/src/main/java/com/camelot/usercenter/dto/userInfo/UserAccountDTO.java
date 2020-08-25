package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;

import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums;

/**
 *  用户账户
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-4
 */
public class UserAccountDTO implements Serializable {

	private static final long serialVersionUID = 2111392290704691660L;
	private java.lang.String bankAccountName;//  银行开户名
	private java.lang.String bankAccount;//  公司银行账号
	private java.lang.String bankName;//  开户行支行名称
	private java.lang.String bankBranchJointLine;//  开户行支行联行号
	private java.lang.String bankBranchIsLocated;//  开户行支行所在地
	private java.lang.String bankAccountPermitsPicSrc;//  银行开户许可证电子版图片地址
	private java.lang.Integer bankAccountStatus;//  银行账户审核状态 0驳回/1待确认/2确认
	private java.lang.String bankAccountStatusLabel;//  银行账户审核状态 0驳回/1待确认/2确认
	private Integer isCiticBank; //是否中信本银行  1 是  0 不是 
	
	private String auditRemark; //审核备注 (驳回原因)
	
	
	public Integer getIsCiticBank() {
		return isCiticBank;
	}
	public void setIsCiticBank(Integer isCiticBank) {
		this.isCiticBank = isCiticBank;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public java.lang.String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(java.lang.String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public java.lang.String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(java.lang.String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public java.lang.String getBankName() {
		return bankName;
	}
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	public java.lang.String getBankBranchJointLine() {
		return bankBranchJointLine;
	}
	public void setBankBranchJointLine(java.lang.String bankBranchJointLine) {
		this.bankBranchJointLine = bankBranchJointLine;
	}
	public java.lang.String getBankBranchIsLocated() {
		return bankBranchIsLocated;
	}
	public void setBankBranchIsLocated(java.lang.String bankBranchIsLocated) {
		this.bankBranchIsLocated = bankBranchIsLocated;
	}
	public java.lang.String getBankAccountPermitsPicSrc() {
		return bankAccountPermitsPicSrc;
	}
	public void setBankAccountPermitsPicSrc(
			java.lang.String bankAccountPermitsPicSrc) {
		this.bankAccountPermitsPicSrc = bankAccountPermitsPicSrc;
	}
	public java.lang.Integer getBankAccountStatus() {
		return bankAccountStatus;
	}
	public String getBankAccountStatusLabel() {
		return bankAccountStatusLabel;
	}
	public void setBankAccountStatus(java.lang.Integer bankAccountStatus) {

        UserEnums.UserAuditStatus comStatus= UserEnums.UserAuditStatus.getEnumByOrdinal(bankAccountStatus);
		if(comStatus!=null){
			this.bankAccountStatusLabel=comStatus.getLabel().toString();
		}
		this.bankAccountStatus = bankAccountStatus;
	}
}
