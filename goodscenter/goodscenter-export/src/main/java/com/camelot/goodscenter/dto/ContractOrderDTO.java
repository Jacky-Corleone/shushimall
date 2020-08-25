package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ContractOrderDTO implements Serializable {

	private Long id;//
	private String contractNo;//合同编号
	private String orderNo;//订单号
	private String state;
	private String createBy;//
	private Date createDate;//
	private String updateBy;//
	private Date updateDate;//
	private String activeFlag;//有效标记
	private String remark;//
	private List ids;
	private List contractNos;
	private List<String> orderNos;

	public List<String> getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(List<String> orderNos) {
		this.orderNos = orderNos;
	}

	public List getContractNos() {
		return contractNos;
	}

	public void setContractNos(List contractNos) {
		this.contractNos = contractNos;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	// setter and getter

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContractNo(){
		return contractNo;
	}
	
	public void setContractNo(String contractNo){
		this.contractNo = contractNo;
	}
	public String getOrderNo(){
		return orderNo;
	}
	
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
	public String getCreateBy(){
		return createBy;
	}
	
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}

	public String getUpdateBy(){
		return updateBy;
	}
	
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}

	public String getActiveFlag(){
		return activeFlag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setActiveFlag(String activeFlag){
		this.activeFlag = activeFlag;
	}
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
}
