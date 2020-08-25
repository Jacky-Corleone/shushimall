package com.camelot.usercenter.dto.device;

import java.io.Serializable;

import com.camelot.usercenter.enums.UserEnums.DeviceType;
/**
 * 用户公司设备 - 印刷（买家）
 *
 * @author learrings
 */
public class PrintingDeviceDTO extends PrintFatherDeviceDTO implements Serializable {
    
	private static final long serialVersionUID = 3332249663743594636L;
	private java.lang.String printingEquipmentNumber;//  印刷设备-设备编号
	private java.lang.String printingBrand;//  印刷设备-品牌
	private java.lang.String printingSeparate;//  印刷设备-分类
	private java.lang.String printingColorGroupNumber;//  印刷设备-色组数量
	private java.lang.String printingYear;//  印刷设备-设备生产年份
	private java.lang.String printingOrigin;//  印刷设备-来源
	private java.lang.String printingOtherConfiguration;//  印刷设备-其他配置
	private java.lang.String printingDescribe;//  印刷设备-描述
	private java.lang.String printingBreadth;//  印刷设备-幅面
	private java.lang.Long companyinfoId;//  买家公司信息id
	public PrintingDeviceDTO(){
		super.setDeviceType(DeviceType.PRINTING);
	}
	
	public java.lang.String getPrintingEquipmentNumber() {
		return printingEquipmentNumber;
	}
	public void setPrintingEquipmentNumber(java.lang.String printingEquipmentNumber) {
		this.printingEquipmentNumber = printingEquipmentNumber;
	}
	public java.lang.String getPrintingBrand() {
		return printingBrand;
	}
	public void setPrintingBrand(java.lang.String printingBrand) {
		this.printingBrand = printingBrand;
	}
	public java.lang.String getPrintingSeparate() {
		return printingSeparate;
	}
	public void setPrintingSeparate(java.lang.String printingSeparate) {
		this.printingSeparate = printingSeparate;
	}
	public java.lang.String getPrintingColorGroupNumber() {
		return printingColorGroupNumber;
	}
	public void setPrintingColorGroupNumber(
			java.lang.String printingColorGroupNumber) {
		this.printingColorGroupNumber = printingColorGroupNumber;
	}
	public java.lang.String getPrintingYear() {
		return printingYear;
	}
	public void setPrintingYear(java.lang.String printingYear) {
		this.printingYear = printingYear;
	}
	public java.lang.String getPrintingOrigin() {
		return printingOrigin;
	}
	public void setPrintingOrigin(java.lang.String printingOrigin) {
		this.printingOrigin = printingOrigin;
	}
	public java.lang.String getPrintingOtherConfiguration() {
		return printingOtherConfiguration;
	}
	public void setPrintingOtherConfiguration(
			java.lang.String printingOtherConfiguration) {
		this.printingOtherConfiguration = printingOtherConfiguration;
	}
	public java.lang.String getPrintingDescribe() {
		return printingDescribe;
	}
	public void setPrintingDescribe(java.lang.String printingDescribe) {
		this.printingDescribe = printingDescribe;
	}
	public java.lang.String getPrintingBreadth() {
		return printingBreadth;
	}
	public void setPrintingBreadth(java.lang.String printingBreadth) {
		this.printingBreadth = printingBreadth;
	}
	public java.lang.Long getCompanyinfoId() {
		return companyinfoId;
	}
	public void setCompanyinfoId(java.lang.Long companyinfoId) {
		this.companyinfoId = companyinfoId;
	}

}

