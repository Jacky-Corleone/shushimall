package com.camelot.usercenter.dto.device;

import java.io.Serializable;

import com.camelot.usercenter.enums.UserEnums.DeviceType;
/**
 * 用户公司设备-印后（买家）
 *
 * @author learrings
 */
public class PostPressDeviceDTO extends PrintFatherDeviceDTO  implements Serializable {
    
	private static final long serialVersionUID = -7401355272543002048L;
	private java.lang.String postpressEquipmentNumber;//  印后设备-设备编号
	private java.lang.String postpressBrand;//  印后设备-品牌
	private java.lang.String postpressSeparate;//  印后设备-分类
	private java.lang.String postpressStandard;//  印后设备-规格
	private java.lang.String postpressDescribe;//  印后设备-描述
	private java.lang.Long companyinfoId;//  买家公司信息id
	public PostPressDeviceDTO(){
		super.setDeviceType(DeviceType.POSTPRESS);
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

	public java.lang.Long getCompanyinfoId() {
		return companyinfoId;
	}

	public void setCompanyinfoId(java.lang.Long companyinfoId) {
		this.companyinfoId = companyinfoId;
	}
	
}

