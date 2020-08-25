package com.camelot.payment.dto.citic.auxiliary;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  主体账户余额回传对象(辅)
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("row")
public class MainBalance implements Serializable {
	private static final long serialVersionUID = -725014414036726390L;
	private String status;//<!--账户状态 char(7)-->
	private String statusText;// <!--账户状态信息 varchar(254)-->
	private String accountNo;//<!--账号1 char(19)-->
	private String accountName;//<!--账户名称 varchar(60)-->
	private String currencyID;//<!--币种 char(2)-->
	private String openBankName;//<!--开户行名称 varchar(62)-->
	private String lastTranDate;//<!--最近交易日 char(8)-->
	private String usableBalance;//<!--可用账户余额 decimal(15,2)-->
	private String balance;//<!-账号余额--decimal(15,2)--->
	private String forzenAmt;//<!--冻结（或看管）金额decimal(15,2)-->
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}
	public String getOpenBankName() {
		return openBankName;
	}
	public void setOpenBankName(String openBankName) {
		this.openBankName = openBankName;
	}
	public String getLastTranDate() {
		return lastTranDate;
	}
	public void setLastTranDate(String lastTranDate) {
		this.lastTranDate = lastTranDate;
	}
	public String getUsableBalance() {
		return usableBalance;
	}
	public void setUsableBalance(String usableBalance) {
		this.usableBalance = usableBalance;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getForzenAmt() {
		return forzenAmt;
	}
	public void setForzenAmt(String forzenAmt) {
		this.forzenAmt = forzenAmt;
	}
}
