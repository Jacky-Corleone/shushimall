package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContractMatDTO implements Serializable{

	/**
	 * <p>Discription:[商品SKU协议明细]</p>
	 * Created on 2015年6月08日
	 * @author:鲁鹏
	 */
	private static final long serialVersionUID = -6241858618435829940L;
	private Long id;//主键
	private String contractNo;//合同号
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
	private String createBy;//创建人
	private Date createDate;//创建日期
	private String updateBy;//修改人
	private Date updateDate;//修改时间
	private String activeFlag;//有效标记
	private String itemName;
	private String protocolType;//协议类型
	private String number;//协议订单，总数量、
	private Double cost;//协议订单，总价值

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

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

	public String getMatCd() {
		return matCd;
	}

	public void setMatCd(String matCd) {
		this.matCd = matCd;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public String getLable1Cd() {
		return lable1Cd;
	}

	public void setLable1Cd(String lable1Cd) {
		this.lable1Cd = lable1Cd;
	}

	public String getLable1Desc() {
		return lable1Desc;
	}

	public void setLable1Desc(String lable1Desc) {
		this.lable1Desc = lable1Desc;
	}

	public String getLable2Cd() {
		return lable2Cd;
	}

	public void setLable2Cd(String lable2Cd) {
		this.lable2Cd = lable2Cd;
	}

	public String getLable2Desc() {
		return lable2Desc;
	}

	public void setLable2Desc(String lable2Desc) {
		this.lable2Desc = lable2Desc;
	}

	public String getLable3Cd() {
		return lable3Cd;
	}

	public void setLable3Cd(String lable3Cd) {
		this.lable3Cd = lable3Cd;
	}

	public String getLable3Desc() {
		return lable3Desc;
	}

	public void setLable3Desc(String lable3Desc) {
		this.lable3Desc = lable3Desc;
	}

	public String getMatSpec() {
		return matSpec;
	}

	public void setMatSpec(String matSpec) {
		this.matSpec = matSpec;
	}

	public String getMatBrand() {
		return matBrand;
	}

	public void setMatBrand(String matBrand) {
		this.matBrand = matBrand;
	}

	public String getMatDiscount() {
		return matDiscount;
	}

	public void setMatDiscount(String matDiscount) {
		this.matDiscount = matDiscount;
	}

	public Double getMatPrice() {
		return matPrice;
	}

	public void setMatPrice(Double matPrice) {
		this.matPrice = matPrice;
	}

	public String getMatUnit() {
		return matUnit;
	}

	public void setMatUnit(String matUnit) {
		this.matUnit = matUnit;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	
	
	
}
