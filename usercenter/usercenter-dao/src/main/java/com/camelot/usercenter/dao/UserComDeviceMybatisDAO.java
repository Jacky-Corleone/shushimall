package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.domain.UserCompanyDevice;
import com.camelot.usercenter.dto.device.UserComDeviceDTO;

/**
 * <p>用户公司设备数据交互接口</p>
 *  
 * @author - learrings
 * @createDate - 2015-3-6
 **/
public interface UserComDeviceMybatisDAO  extends BaseDAO<UserComDeviceDTO>{
	
	/**
	 * 根据用户ID查询指定设备信息
	 * @param uid - 用户ID,可空
	 * @param deviceType - 设备类型，可空
	 * @return
	 */
	public List<UserCompanyDevice> selectListByCondition(@Param("uid")Long uid,@Param("deviceType")Integer deviceType);
	
}