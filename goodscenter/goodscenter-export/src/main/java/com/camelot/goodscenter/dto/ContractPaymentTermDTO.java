package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;

public class ContractPaymentTermDTO implements Serializable {

	private Integer id;//
	private String contractNo;//
	private Integer paymentDays;
	private Integer paymentType;
	private Integer paymentProportion;
	private String activeFlag;//
	private String createBy;//
	private Date createDate;//
	
// setter and getter
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	public String getContractNo(){
		return contractNo;
	}
	
	public void setContractNo(String contractNo){
		this.contractNo = contractNo;
	}
	public Integer getPaymentDays(){
		return paymentDays;
	}
	
	public void setPaymentDays(Integer paymentDays){
		this.paymentDays = paymentDays;
	}
	public Integer getPaymentType(){
		return paymentType;
	}
	
	public void setPaymentType(Integer paymentType){
		this.paymentType = paymentType;
	}
	public Integer getPaymentProportion(){
		return paymentProportion;
	}
	
	public void setPaymentProportion(Integer paymentProportion){
		this.paymentProportion = paymentProportion;
	}
	public String getActiveFlag(){
		return activeFlag;
	}
	
	public void setActiveFlag(String activeFlag){
		this.activeFlag = activeFlag;
	}
	public String getCreateBy(){
		return createBy;
	}
	
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
