package com.camelot.payment.dto.citic.auxiliary;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账户余额信息回传对象(輔)
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("row")
public class AffiliatedBalance implements Serializable{
	private static final long serialVersionUID = -5936740771093831433L;
	private String subAccNo;//<!--附属账号varchar(19)-->
	private String SUBACCNM;// <!--附属账户名称varchar(60)-->
	private String TZAMT;//<!--透支额度decimal(15,2)-->
	private String XSACVL;//<!--实体账户可用资金decimal(15,2)-->
	private String KYAMT;//<!--可用余额 decimal(15,2)-->
	private String SJAMT;//<!--实际余额decimal(15,2)-->
	private String DJAMT;//<!--冻结金额decimal(15,2)-->
	
	public String getSubAccNo() {
		return subAccNo;
	}
	public void setSubAccNo(String subAccNo) {
		this.subAccNo = subAccNo;
	}
	public String getSUBACCNM() {
		return SUBACCNM;
	}
	public void setSUBACCNM(String sUBACCNM) {
		SUBACCNM = sUBACCNM;
	}
	public String getTZAMT() {
		return TZAMT;
	}
	public void setTZAMT(String tZAMT) {
		TZAMT = tZAMT;
	}
	public String getXSACVL() {
		return XSACVL;
	}
	public void setXSACVL(String xSACVL) {
		XSACVL = xSACVL;
	}
	public String getKYAMT() {
		return KYAMT;
	}
	public void setKYAMT(String kYAMT) {
		KYAMT = kYAMT;
	}
	public String getSJAMT() {
		return SJAMT;
	}
	public void setSJAMT(String sJAMT) {
		SJAMT = sJAMT;
	}
	public String getDJAMT() {
		return DJAMT;
	}
	public void setDJAMT(String dJAMT) {
		DJAMT = dJAMT;
	}
	
}
