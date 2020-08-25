package com.camelot.usercenter.domain;

import java.io.Serializable;
/**
 *  公司设备信息原生类(买家)
 * 
 * @author - learrings
 * @createDate - 2015-3-7
 */
public class UserCompanyDevice  implements Serializable {
    
	private static final long serialVersionUID = -1817847955820679530L;
	private java.lang.Long deviceId;//  deviceId
	private java.lang.String postpressEquipmentNumber;//  印后设备-设备编号
	private java.lang.String postpressBrand;//  印后设备-品牌
	private java.lang.String postpressSeparate;//  印后设备-分类
	private java.lang.String postpressStandard;//  印后设备-规格
	private java.lang.String postpressDescribe;//  印后设备-描述
	private java.lang.String prepressEquipmentNumber;//  印前设备-设备编号
	private java.lang.String prepressBrand;//  印前设备-品牌
	private java.lang.String prepressSeparate;//  印前设备-分类
	private java.lang.String prepressStandard;//  印前设备-规格
	private java.lang.String prepressDescribe;//  印前设备-描述
	private java.lang.String printingEquipmentNumber;//  印刷设备-设备编号
	private java.lang.String printingBrand;//  印刷设备-品牌
	private java.lang.String printingSeparate;//  印刷设备-分类
	private java.lang.String printingColorGroupNumber;//  印刷设备-色组数量
	private java.lang.String printingYear;//  印刷设备-设备生产年份
	private java.lang.String printingOrigin;//  印刷设备-来源
	private java.lang.String printingOtherConfiguration;//  印刷设备-其他配置
	private java.lang.String printingDescribe;//  印刷设备-描述
	private java.lang.String printingBreadth;//  印刷设备-幅面
	private java.lang.Integer deviceType;//  设备类型：10-印后设备、20-印前设备、30-印刷设备
	private java.lang.Long companyinfoId;//  买家公司信息id
	
	
	public java.lang.Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(java.lang.Long value) {
		this.deviceId = value;
	}
	
	public java.lang.String getPostpressEquipmentNumber() {
		return this.postpressEquipmentNumber;
	}

	public void setPostpressEquipmentNumber(java.lang.String value) {
		this.postpressEquipmentNumber = value;
	}
	
	public java.lang.String getPostpressBrand() {
		return this.postpressBrand;
	}

	public void setPostpressBrand(java.lang.String value) {
		this.postpressBrand = value;
	}
	
	public java.lang.String getPostpressSeparate() {
		return this.postpressSeparate;
	}

	public void setPostpressSeparate(java.lang.String value) {
		this.postpressSeparate = value;
	}
	
	public java.lang.String getPostpressStandard() {
		return this.postpressStandard;
	}

	public void setPostpressStandard(java.lang.String value) {
		this.postpressStandard = value;
	}
	
	public java.lang.String getPostpressDescribe() {
		return this.postpressDescribe;
	}

	public void setPostpressDescribe(java.lang.String value) {
		this.postpressDescribe = value;
	}
	
	public java.lang.String getPrepressEquipmentNumber() {
		return this.prepressEquipmentNumber;
	}

	public void setPrepressEquipmentNumber(java.lang.String value) {
		this.prepressEquipmentNumber = value;
	}
	
	public java.lang.String getPrepressBrand() {
		return this.prepressBrand;
	}

	public void setPrepressBrand(java.lang.String value) {
		this.prepressBrand = value;
	}
	
	public java.lang.String getPrepressSeparate() {
		return this.prepressSeparate;
	}

	public void setPrepressSeparate(java.lang.String value) {
		this.prepressSeparate = value;
	}
	
	public java.lang.String getPrepressStandard() {
		return this.prepressStandard;
	}

	public void setPrepressStandard(java.lang.String value) {
		this.prepressStandard = value;
	}
	
	public java.lang.String getPrepressDescribe() {
		return this.prepressDescribe;
	}

	public void setPrepressDescribe(java.lang.String value) {
		this.prepressDescribe = value;
	}
	
	public java.lang.String getPrintingEquipmentNumber() {
		return this.printingEquipmentNumber;
	}

	public void setPrintingEquipmentNumber(java.lang.String value) {
		this.printingEquipmentNumber = value;
	}
	
	public java.lang.String getPrintingBrand() {
		return this.printingBrand;
	}

	public void setPrintingBrand(java.lang.String value) {
		this.printingBrand = value;
	}
	
	public java.lang.String getPrintingSeparate() {
		return this.printingSeparate;
	}

	public void setPrintingSeparate(java.lang.String value) {
		this.printingSeparate = value;
	}
	
	public java.lang.String getPrintingColorGroupNumber() {
		return this.printingColorGroupNumber;
	}

	public void setPrintingColorGroupNumber(java.lang.String value) {
		this.printingColorGroupNumber = value;
	}
	
	public java.lang.String getPrintingYear() {
		return this.printingYear;
	}

	public void setPrintingYear(java.lang.String value) {
		this.printingYear = value;
	}
	
	public java.lang.String getPrintingOrigin() {
		return this.printingOrigin;
	}

	public void setPrintingOrigin(java.lang.String value) {
		this.printingOrigin = value;
	}
	
	public java.lang.String getPrintingOtherConfiguration() {
		return this.printingOtherConfiguration;
	}

	public void setPrintingOtherConfiguration(java.lang.String value) {
		this.printingOtherConfiguration = value;
	}
	
	public java.lang.String getPrintingDescribe() {
		return this.printingDescribe;
	}

	public void setPrintingDescribe(java.lang.String value) {
		this.printingDescribe = value;
	}
	
	public java.lang.String getPrintingBreadth() {
		return this.printingBreadth;
	}

	public void setPrintingBreadth(java.lang.String value) {
		this.printingBreadth = value;
	}
	
	public java.lang.Integer getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(java.lang.Integer value) {
		this.deviceType = value;
	}
	
	public java.lang.Long getCompanyinfoId() {
		return this.companyinfoId;
	}

	public void setCompanyinfoId(java.lang.Long value) {
		this.companyinfoId = value;
	}

}

