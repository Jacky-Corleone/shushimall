package com.camelot.usercenter.dto.device;

import com.camelot.usercenter.enums.UserEnums.DeviceType;
/**
 * 用户公司父级设备公共类（买家）
 *
 * @author learrings
 */
public class PrintFatherDeviceDTO  {
    
	private java.lang.Long deviceId;//  deviceId
	private java.lang.Integer deviceType; // 设备类型：10-印后设备、20-印前设备、30-印刷设备  
	
	public java.lang.Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(java.lang.Long deviceId) {
		this.deviceId = deviceId;
	}
	protected void setDeviceType(DeviceType deviceType) {
		this.deviceType=deviceType.getCode();
	}
	public java.lang.Integer getDeviceType() {
		return deviceType;
	}
}

