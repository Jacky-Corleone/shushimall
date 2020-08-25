package com.camelot.payment.service.alipay.util.mobile;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 *  附属账户预签约回传对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("direct_trade_create_res")
public class CopyOfAffiliatedAddDto implements Serializable {

	private static final long serialVersionUID = 7829600600839984724L;
	private String status;// 状态
	private String statusText;// 状态文本
	private String subAccNo;// <!--附属账号 char(19)-->
	private String subAccNm;//<!--附属账户名称 varchar(100)-->
	private Integer AccountType;
	
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
	public Integer getAccountType() {
		return AccountType;
	}
	public void setAccountType(Integer accountType) {
		AccountType = accountType;
	}
	
	
}
