package com.camelot.usercenter.dto;

import java.io.Serializable;
/**
 * 用户公司信息（买家）
 * 
 * @author learrings
 */
public class UserCompanyDTO  implements Serializable {
	private static final long serialVersionUID = 7143945013244719619L;
	private java.lang.Long id;//  id
	private java.lang.Long userId;//  用户id
	private java.util.Date createTime;//  创建时间
	private java.lang.String serviceType;//  业务类型
	private java.lang.String companyScale;//  公司规模
	private java.lang.String deliveryAddress;//  收货地址
	private java.lang.String invoiceInformation;//  发票信息
	private java.lang.String status;//   用户公司信息状态  暂存 状态为3  已提交 为4 
	
	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.lang.String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(java.lang.String value) {
		this.serviceType = value;
	}
	
	public java.lang.String getCompanyScale() {
		return this.companyScale;
	}

	public void setCompanyScale(java.lang.String value) {
		this.companyScale = value;
	}
	
	public java.lang.String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(java.lang.String value) {
		this.deliveryAddress = value;
	}
	
	public java.lang.String getInvoiceInformation() {
		return this.invoiceInformation;
	}

	public void setInvoiceInformation(java.lang.String value) {
		this.invoiceInformation = value;
	}

}

