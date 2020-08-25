package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.camelot.common.enums.CiticEnums.AccountType;
/**
 * 中信账户管理
 * 
 * @author learrings
 */
public class AccountInfoDto  implements Serializable {
	private static final long serialVersionUID = -1992124793171275353L;
	
	private String system;// 调用系统编码
	private String sign;// 签名
	
	private java.lang.Long id;//  id
	private java.lang.String subAccNo;//  附属账号
	private java.lang.String subAccNm;//  附属账户名称
	private java.lang.String bindingAccNo;//  绑定账户
	private java.lang.String bindingAccNm;//  绑定账户名称
	private java.lang.String bankName;//  开户行支行名称
	private java.lang.String bankBranchJointLine;//  开户行支行联行号
	private AccountType accType;//  账户类型 21：买家支付账户 22买家融资账户 31卖家收款账户 32卖家冻结账户
	private java.lang.Integer sameBank;//  中信标识 1本行 0他行
	private java.lang.Integer accStatus;//  账户状态 0 待生成 1待审核 2审核通过 3审核拒绝 4注销
	private java.lang.Long userId;//  平台用户ID
	private java.util.Date created;//  创建时间
	
	private String createdBegin;//  创建开始时间
	private String createdEnd;//  创建结束时间
	private BigDecimal withdrawPrice;//  提现金额
	
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
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
	public AccountType getAccType() {
		return accType;
	}
	public void setAccType(AccountType accType) {
		this.accType = accType;
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
	public java.lang.Long getUserId() {
		return userId;
	}
	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
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
	public BigDecimal getWithdrawPrice() {
		return withdrawPrice;
	}
	public void setWithdrawPrice(BigDecimal withdrawPrice) {
		this.withdrawPrice = withdrawPrice;
	}
}

