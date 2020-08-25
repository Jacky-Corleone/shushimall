package com.camelot.usercenter.service.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.util.ErrorUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.domain.UserCompanyDevice;
import com.camelot.usercenter.dto.device.PostPressDeviceDTO;
import com.camelot.usercenter.dto.device.PrePressDeviceDTO;
import com.camelot.usercenter.dto.device.PrintingDeviceDTO;
import com.camelot.usercenter.dto.device.UserComDeviceDTO;
import com.camelot.usercenter.enums.UserEnums.DeviceType;

/**
 *  公司工具类
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-7
 */
public class UserCompanyUtil{
	
	private final static Logger logger = LoggerFactory.getLogger(UserCompanyUtil.class);
	
	/**
	 * 公司设备转换
	 * 
	 * @param listUserCompanyDevice
	 * @return
	 */
	public ExecuteResult<UserComDeviceDTO> buildUserComDeviceDTO(List<UserCompanyDevice> listUserCompanyDevice,Long uid) {
		ExecuteResult<UserComDeviceDTO> result=new ExecuteResult<UserComDeviceDTO>();
		try{
			UserComDeviceDTO userComDeviceDTO=new UserComDeviceDTO();
			userComDeviceDTO.setUserId(uid);
			List<PrePressDeviceDTO> listPrePress= new ArrayList<PrePressDeviceDTO>(); // 印前设备
			List<PrintingDeviceDTO> listPrinting=new ArrayList<PrintingDeviceDTO>(); // 印刷设备
			List<PostPressDeviceDTO> listPostPress=new ArrayList<PostPressDeviceDTO>(); // 印后设备
			for (UserCompanyDevice userCompanyDevice:listUserCompanyDevice) {
				if(DeviceType.PREPRESS.getCode()==userCompanyDevice.getDeviceType()){
					PrePressDeviceDTO prePressDeviceDTO=new PrePressDeviceDTO();
					prePressDeviceDTO.setDeviceId(userCompanyDevice.getDeviceId());
					prePressDeviceDTO.setPrepressEquipmentNumber(userCompanyDevice.getPrepressEquipmentNumber());
					prePressDeviceDTO.setPrepressBrand(userCompanyDevice.getPrepressBrand());
					prePressDeviceDTO.setPrepressSeparate(userCompanyDevice.getPrepressSeparate());
					prePressDeviceDTO.setPrepressStandard(userCompanyDevice.getPrepressStandard());
					prePressDeviceDTO.setPrepressDescribe(userCompanyDevice.getPrepressDescribe());
					prePressDeviceDTO.setCompanyinfoId(userCompanyDevice.getCompanyinfoId());
					listPrePress.add(prePressDeviceDTO);
				}else if(DeviceType.PRINTING.getCode()==userCompanyDevice.getDeviceType()){
					PrintingDeviceDTO printingDeviceDTO=new PrintingDeviceDTO();
					printingDeviceDTO.setDeviceId(userCompanyDevice.getDeviceId());
					printingDeviceDTO.setPrintingEquipmentNumber(userCompanyDevice.getPrintingEquipmentNumber());
					printingDeviceDTO.setPrintingBrand(userCompanyDevice.getPrintingBrand());
					printingDeviceDTO.setPrintingSeparate(userCompanyDevice.getPrintingSeparate());
					printingDeviceDTO.setPrintingColorGroupNumber(userCompanyDevice.getPrintingColorGroupNumber());
					printingDeviceDTO.setPrintingYear(userCompanyDevice.getPrintingYear());
					printingDeviceDTO.setPrintingOrigin(userCompanyDevice.getPrintingOrigin());
					printingDeviceDTO.setPrintingOtherConfiguration(userCompanyDevice.getPrintingOtherConfiguration());
					printingDeviceDTO.setPrintingDescribe(userCompanyDevice.getPrintingDescribe());
					printingDeviceDTO.setPrintingBreadth(userCompanyDevice.getPrintingBreadth());
					printingDeviceDTO.setCompanyinfoId(userCompanyDevice.getCompanyinfoId());
					listPrinting.add(printingDeviceDTO);
				}else if(DeviceType.POSTPRESS.getCode()==userCompanyDevice.getDeviceType()){
					PostPressDeviceDTO postPressDeviceDTO=new PostPressDeviceDTO();
					postPressDeviceDTO.setDeviceId(userCompanyDevice.getDeviceId());
					postPressDeviceDTO.setPostpressEquipmentNumber(userCompanyDevice.getPostpressEquipmentNumber());
					postPressDeviceDTO.setPostpressBrand(userCompanyDevice.getPostpressBrand());
					postPressDeviceDTO.setPostpressSeparate(userCompanyDevice.getPostpressSeparate());
					postPressDeviceDTO.setPostpressStandard(userCompanyDevice.getPostpressStandard());
					postPressDeviceDTO.setPostpressDescribe(userCompanyDevice.getPostpressDescribe());
					postPressDeviceDTO.setCompanyinfoId(userCompanyDevice.getCompanyinfoId());
					listPostPress.add(postPressDeviceDTO);
				}
			}
			userComDeviceDTO.setListPrePress(listPrePress);
			userComDeviceDTO.setListPrinting(listPrinting);
			userComDeviceDTO.setListPostPress(listPostPress);
			result.setResult(userComDeviceDTO);
		}catch (Exception e) {
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("转换对象异常", e.getMessage()));
			logger.info("方法[{}]，出参：[{}]","UserCompanyUtil-buildUserComDeviceDTO",e.getMessage());
		}
		return result;
	}
}
