package com.camelot.mall.orderWx;

import java.io.Serializable;

public class ContractInfo implements Serializable {

	private static final long serialVersionUID = -887396736629435568L;
	
	private Long id;//主键
	private String contractNo;//合同编号
	private String contractName;//合同名称
	private String status;//合同状态
	private Integer supplierId;//供货方主键
	private String supplierName;//供货方主键
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
