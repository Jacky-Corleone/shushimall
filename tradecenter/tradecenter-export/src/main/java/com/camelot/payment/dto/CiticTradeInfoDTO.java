package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 非登录打印，详细信息DTO
 * @author 王东晓
 *
 */
@XStreamAlias("row")
public class CiticTradeInfoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subAccNo;//附属账号 varchar(19) 
	private String tranType;//交易类型 varchar(2) 
	private String tranDate;//交易日期 char(8) 格式YYYYMMDD
	private String tranTime;//交易时间 char(6) 格式HHMMSS
	private String tellerNo;//柜员交易号 varchar(14) 
	private String tranSeqNo;//交易序号 varchar(13)
	private String accountNo;//对方账号 varchar(19)
	private String accountNm;//对方账户名称 varchar(60)
	private String accBnkNm;//对方开户行名称 varchar(60)
	private String loanFlag;//借贷标识 "D":借,"C":贷 varchar(1)
	private BigDecimal tranAmt;//交易金额 decimal(15,2) 
	private BigDecimal accBalAmt;//账户余额 decimal(15,2)
	private BigDecimal pdgAmt;//手续费金额 decimal(15,2)
	private String memo;//摘要 varchar(22) 
	private String verifyCode;//打印校验码 varchar(20)
	public String getSubAccNo() {
		return subAccNo;
	}
	public void setSubAccNo(String subAccNo) {
		this.subAccNo = subAccNo;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public String getTellerNo() {
		return tellerNo;
	}
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}
	public String getTranSeqNo() {
		return tranSeqNo;
	}
	public void setTranSeqNo(String tranSeqNo) {
		this.tranSeqNo = tranSeqNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountNm() {
		return accountNm;
	}
	public void setAccountNm(String accountNm) {
		this.accountNm = accountNm;
	}
	public String getAccBnkNm() {
		return accBnkNm;
	}
	public void setAccBnkNm(String accBnkNm) {
		this.accBnkNm = accBnkNm;
	}
	public String getLoanFlag() {
		return loanFlag;
	}
	public void setLoanFlag(String loanFlag) {
		this.loanFlag = loanFlag;
	}
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}
	public BigDecimal getAccBalAmt() {
		return accBalAmt;
	}
	public void setAccBalAmt(BigDecimal accBalAmt) {
		this.accBalAmt = accBalAmt;
	}
	public BigDecimal getPdgAmt() {
		return pdgAmt;
	}
	public void setPdgAmt(BigDecimal pdgAmt) {
		this.pdgAmt = pdgAmt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
}
