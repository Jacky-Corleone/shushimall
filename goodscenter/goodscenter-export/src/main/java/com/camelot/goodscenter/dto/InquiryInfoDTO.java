package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InquiryInfoDTO implements Serializable {

	private Long id;//
	private String inquiryNo;//询价编号
	private String inquiryName;//询价名称
	private Integer printerId;//印刷厂主键
	private Integer supplierId;//供货方主键
	private Date beginDate;//询价有效期--开始
	private Date endDate;//询价有效期--结束
	private String matCd;//物料号
	private Integer skuId;//商品skuid
	private Integer quantity;//数量
	private String status;//询价状态

	private Date deliveryDate;//交货时间
	private String annex;//附件
	private String remarks;//备注
	private String createBy;//
	private List<Long> createBys;//
	private Date createDate;//创建时间、
	
	private String startTime;//创建开始时间
	private String endTime;//创建结束时间
	
	
	private String updateBy;//
	private Date updateDate;//
	private String activeFlag;//有效标记 0-有效 1-无效
	private String itemName;//商品名称
	private List  printerIdList;//印刷厂主键组
	private List supplierIdList;//供货方主键组
	private List<InquiryMatDTO> inquiryMatDTOs;//多个商品名称
	private String alternate1;
	private String alternate2;
	private String alternate3;
	private String alternate4;
	private String alternate5;
	private List<String> statusList;
	private String tempIds;
	private Date deliveryDate2;//交货时间2

	public String getTempIds() {
		return tempIds;
	}

	public void setTempIds(String tempIds) {
		this.tempIds = tempIds;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public List<InquiryMatDTO> getInquiryMatDTOs() {
		return inquiryMatDTOs;
	}

	public void setInquiryMatDTOs(List<InquiryMatDTO> inquiryMatDTOs) {
		this.inquiryMatDTOs = inquiryMatDTOs;
	}

	public String getAlternate1() {
		return alternate1;
	}

	public void setAlternate1(String alternate1) {
		this.alternate1 = alternate1;
	}

	public String getAlternate2() {
		return alternate2;
	}

	public void setAlternate2(String alternate2) {
		this.alternate2 = alternate2;
	}

	public String getAlternate3() {
		return alternate3;
	}

	public void setAlternate3(String alternate3) {
		this.alternate3 = alternate3;
	}

	public String getAlternate4() {
		return alternate4;
	}

	public void setAlternate4(String alternate4) {
		this.alternate4 = alternate4;
	}

	public String getAlternate5() {
		return alternate5;
	}

	public void setAlternate5(String alternate5) {
		this.alternate5 = alternate5;
	}

	public List getSupplierIdList() {
		return supplierIdList;
	}

	public void setSupplierIdList(List supplierIdList) {
		this.supplierIdList = supplierIdList;
	}

	public List getPrinterIdList() {
		return printerIdList;
	}

	public void setPrinterIdList(List printerIdList) {
		this.printerIdList = printerIdList;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMatCd() {
		return matCd;
	}

	public void setMatCd(String matCd) {
		this.matCd = matCd;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInquiryNo() {
		return inquiryNo;
	}

	public void setInquiryNo(String inquiryNo) {
		this.inquiryNo = inquiryNo;
	}

	public String getInquiryName() {
		return inquiryName;
	}

	public void setInquiryName(String inquiryName) {
		this.inquiryName = inquiryName;
	}

	public Integer getPrinterId() {
		return printerId;
	}

	public void setPrinterId(Integer printerId) {
		this.printerId = printerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	
	

	



	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<Long> getCreateBys() {
		return createBys;
	}

	public void setCreateBys(List<Long> createBys) {
		this.createBys = createBys;
	}

	public Date getDeliveryDate2() {return deliveryDate2;}

	public void setDeliveryDate2(Date deliveryDate2) {this.deliveryDate2 = deliveryDate2;}
}
