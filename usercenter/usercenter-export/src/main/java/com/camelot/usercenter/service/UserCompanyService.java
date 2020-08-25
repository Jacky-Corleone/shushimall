package com.camelot.usercenter.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.device.UserComDeviceDTO;
import com.camelot.usercenter.enums.UserEnums.DeviceType;
/**
 *  用户公司（买家）对外接口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-6
 */
public interface UserCompanyService {
	
	/**
	 * 保存公司信息
	 * 
	 * @param userCompanyDTO
	 * @return
	 */
	ExecuteResult<UserCompanyDTO> saveUserCompany(UserCompanyDTO userCompanyDTO);
	
	/**
	 * 修改公司信息
	 * 
	 * @param userCompanyDTO
	 * @return
	 */
	ExecuteResult<UserCompanyDTO> modifyUserCompany(UserCompanyDTO userCompanyDTO);
	
	/**
	 * 根据用户ID查询公司信息
	 * 
	 * @param uid - 用户ID
	 * @return
	 */
	ExecuteResult<UserCompanyDTO> findUserCompanyByUId(Long uid);
	
	/**
	 * 添加公司设备，逻辑：先删除当前用户下所有设备后添加
	 * 
	 * @param userCompanyDeviceDTO -  必填项 用户ID(userId)
	 * @return
	 */
	ExecuteResult<UserComDeviceDTO> saveComDevice(UserComDeviceDTO userComDeviceDTO);
	
	/**
	 * 根据用户ID查询公司设备信息列表
	 * 
	 * @param uid - 用户ID
	 * @param deviceType - 设备类型，可空
	 * @return
	 */
	ExecuteResult<UserComDeviceDTO> findComDeviceListByUId(Long uid,DeviceType deviceType);
	/**
	 * 
	 * <p>Discription:[公司信息 提交操作 逻辑为更改公司信息 状态为 4(已提交)]</p>
	 * Created on 2015-3-11
	 * @param userCompanyDTO
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<Integer> modifyUserCompanyStatus(UserCompanyDTO userCompanyDTO);

}
