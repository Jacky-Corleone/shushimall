package com.camelot.payment.dto;

import java.io.Serializable;

/**
 * 非登录打印明细查询入参DTO
 * 
 * @author 王东晓
 *
 */
public class CiticTradeInDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String action;//对应请求码
	private String userName; //登录名varchar(30)
	private String mainAccNo;//主体账号varchar(19)
	private String subAccNo; //附属账号 varchar(19)
	private String startDate;//起始日期 char(8) 格式YYYYMMDD
	private String endDate;//终止日期 char(8) 格式YYYYMMDD
	private Integer startRecord;//起始记录号integer
	private Integer pageNumber;//请求记录条数，最大为10 integer 
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMainAccNo() {
		return mainAccNo;
	}
	public void setMainAccNo(String mainAccNo) {
		this.mainAccNo = mainAccNo;
	}
	public String getSubAccNo() {
		return subAccNo;
	}
	public void setSubAccNo(String subAccNo) {
		this.subAccNo = subAccNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(Integer startRecord) {
		this.startRecord = startRecord;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
}
