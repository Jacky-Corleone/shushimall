package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InquiryMatDTO implements Serializable {

	private Long id;//
	private String inquiryNo;//合同号
	private String matCd;//物料号
	private String matDesc;//物料描述
	private String lable1Cd;//类别1
	private String lable1Desc;//类别1描述
	private String lable2Cd;//类别2
	private String lable2Desc;//类别2描述
	private String lable3Cd;//类别3
	private String lable3Desc;//类别3描述
	private String matSpec;//物料规格
	private String matBrand;//品牌
	private String matDiscount;//折扣
	private Double matPrice;//价格
	private String matUnit;//单位
	private Integer quantity;//数量
	private Date beginDate;//有效期--开始
	private Date endDate;//有效期--结束
	private Integer printerId;//印刷厂主键
	private Integer supplierId;//供货方主键
	private Integer shopId;//店铺ID
	private String createBy;//创建人
	private Date createDate;//创建日期
	private String updateBy;//修改人
	private Date updateDate;//修改时间
	private String activeFlag;//有效标记
	private String status;//状态
	private String alternate1;
	private String alternate2;
	private String alternate3;
	private String alternate4;
	private String alternate5;
	private Date deliveryDate2;//交货时间2

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
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

	public Long getId() {
		return id;
	}

	public String getInquiryNo(){
		return inquiryNo;
	}
	
	public void setInquiryNo(String inquiryNo){
		this.inquiryNo = inquiryNo;
	}
	public String getMatCd(){
		return matCd;
	}
	
	public void setMatCd(String matCd){
		this.matCd = matCd;
	}
	public String getMatDesc(){
		return matDesc;
	}
	
	public void setMatDesc(String matDesc){
		this.matDesc = matDesc;
	}
	public String getLable1Cd(){
		return lable1Cd;
	}
	
	public void setLable1Cd(String lable1Cd){
		this.lable1Cd = lable1Cd;
	}
	public String getLable1Desc(){
		return lable1Desc;
	}
	
	public void setLable1Desc(String lable1Desc){
		this.lable1Desc = lable1Desc;
	}
	public String getLable2Cd(){
		return lable2Cd;
	}
	
	public void setLable2Cd(String lable2Cd){
		this.lable2Cd = lable2Cd;
	}
	public String getLable2Desc(){
		return lable2Desc;
	}
	
	public void setLable2Desc(String lable2Desc){
		this.lable2Desc = lable2Desc;
	}
	public String getLable3Cd(){
		return lable3Cd;
	}
	
	public void setLable3Cd(String lable3Cd){
		this.lable3Cd = lable3Cd;
	}
	public String getLable3Desc(){
		return lable3Desc;
	}
	
	public void setLable3Desc(String lable3Desc){
		this.lable3Desc = lable3Desc;
	}
	public String getMatSpec(){
		return matSpec;
	}
	
	public void setMatSpec(String matSpec){
		this.matSpec = matSpec;
	}
	public String getMatBrand(){
		return matBrand;
	}
	
	public void setMatBrand(String matBrand){
		this.matBrand = matBrand;
	}
	public String getMatDiscount(){
		return matDiscount;
	}
	
	public void setMatDiscount(String matDiscount){
		this.matDiscount = matDiscount;
	}

	public Double getMatPrice() {
		return matPrice;
	}

	public void setMatPrice(Double matPrice) {
		this.matPrice = matPrice;
	}

	public String getMatUnit(){
		return matUnit;
	}
	
	public void setMatUnit(String matUnit){
		this.matUnit = matUnit;
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

	public void setId(Long id) {
		this.id = id;
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

	public String getActiveFlag(){
		return activeFlag;
	}
	
	public void setActiveFlag(String activeFlag){
		this.activeFlag = activeFlag;
	}

	public Date getDeliveryDate2() {return deliveryDate2;}

	public void setDeliveryDate2(Date deliveryDate2) {this.deliveryDate2 = deliveryDate2;}
}
