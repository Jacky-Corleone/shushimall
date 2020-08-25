package com.camelot.usercenter.dto.device;

import java.io.Serializable;

import com.camelot.usercenter.enums.UserEnums.DeviceType;
/**
 * 用户公司设备 - 印前（买家）
 *
 * @author learrings
 */
public class PrePressDeviceDTO extends PrintFatherDeviceDTO implements Serializable {
    
	private static final long serialVersionUID = -8060364319865327841L;
	private java.lang.String prepressEquipmentNumber;//  印前设备-设备编号
	private java.lang.String prepressBrand;//  印前设备-品牌
	private java.lang.String prepressSeparate;//  印前设备-分类
	private java.lang.String prepressStandard;//  印前设备-规格
	private java.lang.String prepressDescribe;//  印前设备-描述
	private java.lang.Long companyinfoId;//  买家公司信息id
	
	public PrePressDeviceDTO(){
		super.setDeviceType(DeviceType.PREPRESS);
	}
	
	public java.lang.String getPrepressEquipmentNumber() {
		return prepressEquipmentNumber;
	}
	public void setPrepressEquipmentNumber(java.lang.String prepressEquipmentNumber) {
		this.prepressEquipmentNumber = prepressEquipmentNumber;
	}
	public java.lang.String getPrepressBrand() {
		return prepressBrand;
	}
	public void setPrepressBrand(java.lang.String prepressBrand) {
		this.prepressBrand = prepressBrand;
	}
	public java.lang.String getPrepressSeparate() {
		return prepressSeparate;
	}
	public void setPrepressSeparate(java.lang.String prepressSeparate) {
		this.prepressSeparate = prepressSeparate;
	}
	public java.lang.String getPrepressStandard() {
		return prepressStandard;
	}
	public void setPrepressStandard(java.lang.String prepressStandard) {
		this.prepressStandard = prepressStandard;
	}
	public java.lang.String getPrepressDescribe() {
		return prepressDescribe;
	}
	public void setPrepressDescribe(java.lang.String prepressDescribe) {
		this.prepressDescribe = prepressDescribe;
	}
	public java.lang.Long getCompanyinfoId() {
		return companyinfoId;
	}

	public void setCompanyinfoId(java.lang.Long companyinfoId) {
		this.companyinfoId = companyinfoId;
	}
}

