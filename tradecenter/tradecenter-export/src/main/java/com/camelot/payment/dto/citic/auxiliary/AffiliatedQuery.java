package com.camelot.payment.dto.citic.auxiliary;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账户签约状态回传对象(辅)
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("row")
public class AffiliatedQuery implements Serializable{
	private static final long serialVersionUID = 3282451293964653238L;
	private Integer status;//<!--状态char(1)，0：正常；1：待审核；2：注销；3：审核拒绝-->
	private String statusText;
	private String subAccNo;//<!--附属账号char(19)-->
	private String subAccNm;//<!--附属账户名称varchar(100)-->
	private String accType;//<!--附属账户类型char(2)，03：一般交易账号；04：保证金帐-->
	private String calInterestFlag;//<!--是否计算利息标示char(1)，0：不计息 1：不分段计息 2：分段计息-->
	private String interestRate;//<!--默认计息利率decimal(9,7)-->
	private String feeType;//<!--手续费收取方式char(1)，空：不收取；1：实时收取；2：月末累计-->
	private String overFlag;// <!--是否允许透支char(1)，0：不允许；1：限额透支；2：全额透支-->
	private String overAmt;//<!--透支额度decimal(15,2)-->
	private String overRate;// <!--透支利率decimal(9,7)-->
	private String autoAssignInterestFlag;//<!--自动分配利息标示char(1)，0：否；1；是-->
	private String autoAssignTranFeeFlag;// <!--自动分配手续费标示char(1)-->
	private String centerNo;//<!--会员签约中心char(6)-->
	private String centerNm;//<!--会员签约中心名称varchar(30)-->
	private String realNameParm;//<!--实名制更换char(1)，0：账户名与账号全不换；1：账户名与账号全换；2：换账户名；3：换账号-->
	private String subAccPrintParm;//<!--附属账户凭证打印更换char(1)， 0：全部显示；1：显示附属账户名和账号；2：显示实体账户名和账号；3：显示附属账户名和实体账号；4：显示实体账户名和附属账号 -->
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getSubAccNo() {
		return subAccNo;
	}
	public void setSubAccNo(String subAccNo) {
		this.subAccNo = subAccNo;
	}
	public String getSubAccNm() {
		return subAccNm;
	}
	public void setSubAccNm(String subAccNm) {
		this.subAccNm = subAccNm;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCalInterestFlag() {
		return calInterestFlag;
	}
	public void setCalInterestFlag(String calInterestFlag) {
		this.calInterestFlag = calInterestFlag;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getOverFlag() {
		return overFlag;
	}
	public void setOverFlag(String overFlag) {
		this.overFlag = overFlag;
	}
	public String getOverAmt() {
		return overAmt;
	}
	public void setOverAmt(String overAmt) {
		this.overAmt = overAmt;
	}
	public String getOverRate() {
		return overRate;
	}
	public void setOverRate(String overRate) {
		this.overRate = overRate;
	}
	public String getAutoAssignInterestFlag() {
		return autoAssignInterestFlag;
	}
	public void setAutoAssignInterestFlag(String autoAssignInterestFlag) {
		this.autoAssignInterestFlag = autoAssignInterestFlag;
	}
	public String getAutoAssignTranFeeFlag() {
		return autoAssignTranFeeFlag;
	}
	public void setAutoAssignTranFeeFlag(String autoAssignTranFeeFlag) {
		this.autoAssignTranFeeFlag = autoAssignTranFeeFlag;
	}
	public String getCenterNo() {
		return centerNo;
	}
	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}
	public String getCenterNm() {
		return centerNm;
	}
	public void setCenterNm(String centerNm) {
		this.centerNm = centerNm;
	}
	public String getRealNameParm() {
		return realNameParm;
	}
	public void setRealNameParm(String realNameParm) {
		this.realNameParm = realNameParm;
	}
	public String getSubAccPrintParm() {
		return subAccPrintParm;
	}
	public void setSubAccPrintParm(String subAccPrintParm) {
		this.subAccPrintParm = subAccPrintParm;
	}

}
