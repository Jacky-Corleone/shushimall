package com.camelot.payment.domain;

import java.io.Serializable;

import com.camelot.openplatform.common.PropertyMapping;
/**
 * 中信账户管理记录
 * 
 * @author learrings
 */
public class AccountInfo  implements Serializable {
	private static final long serialVersionUID = -1992124793171275353L;
	private java.lang.Long id;//  id
	@PropertyMapping("subAccNo")
	private java.lang.String subAccNo;//  附属账号
	@PropertyMapping("subAccNm")
	private java.lang.String subAccNm;//  附属账户名称
	@PropertyMapping("bindingAccNo")
	private java.lang.String bindingAccNo;//  绑定账户
	@PropertyMapping("bindingAccNm")
	private java.lang.String bindingAccNm;//  绑定账户名称
	private java.lang.Integer accType;//  账户类型 21：买家支付账户 22买家融资账户 31卖家收款账户 32卖家冻结账户
	@PropertyMapping("bankName")
	private java.lang.String bankName;//  开户行支行名称
	@PropertyMapping("bankBranchJointLine")
	private java.lang.String bankBranchJointLine;//  开户行支行联行号
	private java.lang.String transferType="BF";//  转账类型 BF
	@PropertyMapping("sameBank")
	private java.lang.Integer sameBank;//  中信标识 1本行 0他行
	@PropertyMapping("accStatus")
	private java.lang.Integer accStatus;//  账户状态 0 待生成 1待审核 2审核通过 3审核拒绝 4注销
	@PropertyMapping("accStatusText")
	private java.lang.String accStatusText;//  账号状态文本 
	private java.lang.Integer attemptTime;//  尝试生成次数
	@PropertyMapping("userId")
	private java.lang.Long userId;//  平台用户ID
	private java.lang.String dealFlag;// 处理标记
	private java.lang.Integer createdId;//  创建人
	@PropertyMapping("created")
	private java.util.Date created;//  创建时间
	
	private String createdBegin;//  创建开始时间
	private String createdEnd;//  创建结束时间
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getSubAccNo() {
		return subAccNo;
	}
	public void setSubAccNo(java.lang.String subAccNo) {
		this.subAccNo = subAccNo;
	}
	public java.lang.String getSubAccNm() {
		return subAccNm;
	}
	public void setSubAccNm(java.lang.String subAccNm) {
		this.subAccNm = subAccNm;
	}
	public java.lang.String getBindingAccNo() {
		return bindingAccNo;
	}
	public void setBindingAccNo(java.lang.String bindingAccNo) {
		this.bindingAccNo = bindingAccNo;
	}
	public java.lang.String getBindingAccNm() {
		return bindingAccNm;
	}
	public void setBindingAccNm(java.lang.String bindingAccNm) {
		this.bindingAccNm = bindingAccNm;
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
	public java.lang.Integer getAccType() {
		return accType;
	}
	public void setAccType(java.lang.Integer accType) {
		this.accType = accType;
	}
	public java.lang.String getTransferType() {
		return transferType;
	}
	public void setTransferType(java.lang.String transferType) {
		this.transferType = transferType;
	}
	public java.lang.Integer getSameBank() {
		return sameBank;
	}
	public void setSameBank(java.lang.Integer sameBank) {
		this.sameBank = sameBank;
	}
	public java.lang.Integer getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(java.lang.Integer accStatus) {
		this.accStatus = accStatus;
	}
	public java.lang.String getAccStatusText() {
		return accStatusText;
	}
	public void setAccStatusText(java.lang.String accStatusText) {
		this.accStatusText = accStatusText;
	}
	public java.lang.Integer getAttemptTime() {
		return attemptTime;
	}
	public void setAttemptTime(java.lang.Integer attemptTime) {
		this.attemptTime = attemptTime;
	}
	public java.lang.Long getUserId() {
		return userId;
	}
	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}
	public java.lang.String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(java.lang.String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public java.lang.Integer getCreatedId() {
		return createdId;
	}
	public void setCreatedId(java.lang.Integer createdId) {
		this.createdId = createdId;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
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
}

