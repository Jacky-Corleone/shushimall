package com.camelot.usercenter.dto.device;

import java.io.Serializable;
import java.util.List;

/**
 * 用户公司设备主类（买家）
 *
 * @author learrings
 */
public class UserComDeviceDTO  implements Serializable {
    
	private static final long serialVersionUID = -5379809664690105797L;
	private java.lang.Long userId;//  用户id
	private List<PrePressDeviceDTO> listPrePress; // 印前设备
	private List<PrintingDeviceDTO> listPrinting; // 印刷设备
	private List<PostPressDeviceDTO> listPostPress; // 印后设备
	
	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}

	public List<PrePressDeviceDTO> getListPrePress() {
		return listPrePress;
	}

	public void setListPrePress(List<PrePressDeviceDTO> listPrePress) {
		this.listPrePress = listPrePress;
	}

	public List<PrintingDeviceDTO> getListPrinting() {
		return listPrinting;
	}

	public void setListPrinting(List<PrintingDeviceDTO> listPrinting) {
		this.listPrinting = listPrinting;
	}

	public List<PostPressDeviceDTO> getListPostPress() {
		return listPostPress;
	}

	public void setListPostPress(List<PostPressDeviceDTO> listPostPress) {
		this.listPostPress = listPostPress;
	}

}

