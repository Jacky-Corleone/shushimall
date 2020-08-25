package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;

/**
 * 
 * <p>Description: [用户积分信息]</p>
 * Created on 2015-12-7
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserIntegralTrajectoryExportService {
	
	/**
	 * 
	 * <p>Description: [保存用户积分信息]</p>
	 * Created on 2015-12-7
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @return 
	 */
	public ExecuteResult<UserIntegralTrajectoryDTO> addUserIntegralTrajectoryDTO(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO);
	/**
	 * 
	 * <p>Description: [更新用户积分信息]</p>
	 * Created on 2015-12-7
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @return 
	 */
	public ExecuteResult<Boolean> updateUserIntegralTrajectoryDTO(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO);
	/**
	 * 
	 * <p>Description: [查询用户积分信息]</p>
	 * Created on 2015-12-7
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> queryUserIntegralTrajectoryDTO(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,Pager page);
	/**
	 * 
	 * <p>Description: [根据类型查询用户积分信息]</p>
	 * <p>Description: [特殊查询类型为1表示获取积分，类型为2表示使用积分]</p>
	 * Created on 2015-12-8
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> queryUserIntegralByType(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,Pager page);
	/**
	 * 
	 * <p>Description: [查询用户总积分]</p>
	 * Created on 2015-12-8
	 * @author 周志军
	 * @param userId
	 * @return 
	 */
	public ExecuteResult<Long> queryTotalIntegral(Long userId);
}
