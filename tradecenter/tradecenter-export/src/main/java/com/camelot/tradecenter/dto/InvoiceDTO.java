package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>
 * Description: [增值税发票]
 * </p>
 * Created on 2015年9月10日
 * 
 * @author <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class InvoiceDTO implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;

	private Long id;// 主键
	private Integer invoice;// 发票类型 1不开发票 2普通发票 3增值税发票
	private String invoiceTitle;// 发票抬头
	private String companyName;// 单位名称
	private String taxpayerCode;// 纳税人识别码
	private String registeredAddress;// 注册地址
	private String registeredPhone;// 注册电话
	private String bankName;// 开户银行
	private String bankAccount;// 银行账户
	private String normalContent;// 发票内容：明细
	private String consigneeName;// 收票人姓名
	private String consigneeMobile;// 收票人手机
	private Integer provinceId;// 省
	private Integer cityId;// 市
	private Integer countyId;// 县
	private String fullAddress;// 全地址
	private String detailAddress;// 详细地址
	private Long buyerId;// 买家ID
	private Date createTime;// 创建时间

	private String[] businessLicensePicUrl;// 营业执照
	private String[] taxRegistrationCertificatePicUrl;// 税务登记证
	private String[] generalTaxpayerPicUrl;// 一般纳税人证明

	private List<InvoicePicDTO> invoicePicDTOs;// 增值税发票对应的图片，包含了营业执照、税务登记证、一般纳税人证明

	private String orderId;// 订单编号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaxpayerCode() {
		return taxpayerCode;
	}

	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getRegisteredPhone() {
		return registeredPhone;
	}

	public void setRegisteredPhone(String registeredPhone) {
		this.registeredPhone = registeredPhone;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getNormalContent() {
		return normalContent;
	}

	public void setNormalContent(String normalContent) {
		this.normalContent = normalContent;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String[] getBusinessLicensePicUrl() {
		return businessLicensePicUrl;
	}

	public void setBusinessLicensePicUrl(String[] businessLicensePicUrl) {
		this.businessLicensePicUrl = businessLicensePicUrl;
	}

	public String[] getTaxRegistrationCertificatePicUrl() {
		return taxRegistrationCertificatePicUrl;
	}

	public void setTaxRegistrationCertificatePicUrl(String[] taxRegistrationCertificatePicUrl) {
		this.taxRegistrationCertificatePicUrl = taxRegistrationCertificatePicUrl;
	}

	public String[] getGeneralTaxpayerPicUrl() {
		return generalTaxpayerPicUrl;
	}

	public void setGeneralTaxpayerPicUrl(String[] generalTaxpayerPicUrl) {
		this.generalTaxpayerPicUrl = generalTaxpayerPicUrl;
	}

	public List<InvoicePicDTO> getInvoicePicDTOs() {
		return invoicePicDTOs;
	}

	public void setInvoicePicDTOs(List<InvoicePicDTO> invoicePicDTOs) {
		this.invoicePicDTOs = invoicePicDTOs;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public Integer getInvoice() {
		return invoice;
	}

	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
