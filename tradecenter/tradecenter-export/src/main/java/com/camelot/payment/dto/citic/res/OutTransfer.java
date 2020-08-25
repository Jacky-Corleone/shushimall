package com.camelot.payment.dto.citic.res;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  附属账户出/入金对象（发送）
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
public class OutTransfer implements Serializable{

	private static final long serialVersionUID = -2938957050982062616L;
	private String clientID;//<!--客户流水号varchar (20) -->
	private String accountNo;//<!--付款账号varchar(19) -->
	private String recvAccNo;//<!--收款账号varchar(19) -->
	private String recvAccNm;//<!--收款账户名称varchar(60) -->
	private BigDecimal tranAmt;//<!--交易金额decimal(15,2) -->
	private Integer sameBank;//--中信标识char(1) 0：本行 1： 他行--
	
	//--收款账户开户行信息,收款账户若为他行，至少一项不为空--
	private String recvTgfi="";//--收款账户开户行支付联行号varchar(12) --
	private String recvBankNm="";//--收款账户开户行名varchar (60) --
	
	private String memo="";//<!--摘要varchar(22) 可空-->
	private String preFlg="0";//--预约标志（0：非预约1：预约）char(1) --
	private String preDate="";//--预约日期（格式：YYYYMMDD 预约时非空）char(8)--
	private String preTime="";//--预约时间（格式：hhmmss 预约时非空，只限100000、120000、140000、160000四个时间点）char(6)--
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getRecvAccNo() {
		return recvAccNo;
	}
	public void setRecvAccNo(String recvAccNo) {
		this.recvAccNo = recvAccNo;
	}
	public String getRecvAccNm() {
		return recvAccNm;
	}
	public void setRecvAccNm(String recvAccNm) {
		this.recvAccNm = recvAccNm;
	}
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}
	public Integer getSameBank() {
		return sameBank;
	}
	public void setSameBank(Integer sameBank) {
		this.sameBank = sameBank;
	}
	public String getRecvTgfi() {
		return recvTgfi;
	}
	public void setRecvTgfi(String recvTgfi) {
		this.recvTgfi = recvTgfi;
	}
	public String getRecvBankNm() {
		return recvBankNm;
	}
	public void setRecvBankNm(String recvBankNm) {
		this.recvBankNm = recvBankNm;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPreFlg() {
		return preFlg;
	}
	public void setPreFlg(String preFlg) {
		this.preFlg = preFlg;
	}
	public String getPreDate() {
		return preDate;
	}
	public void setPreDate(String preDate) {
		this.preDate = preDate;
	}
	public String getPreTime() {
		return preTime;
	}
	public void setPreTime(String preTime) {
		this.preTime = preTime;
	}
	
}
