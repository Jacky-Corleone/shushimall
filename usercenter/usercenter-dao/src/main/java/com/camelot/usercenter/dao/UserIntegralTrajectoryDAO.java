package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.domain.UserIntegralTrajectory;

/**
 * 
 * <p>Description: [用户积分]</p>
 * Created on 2015-12-7
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserIntegralTrajectoryDAO  extends BaseDAO<UserIntegralTrajectory>{
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
	public List<UserIntegralTrajectory> selectByType(@Param("entity")UserIntegralTrajectory entity,@Param("pager")Pager pager);
	/**
	 * 
	 * <p>Description: [根据类型查询用户积分信息]</p>
	 * <p>Description: [特殊查询类型为1表示获取积分，类型为2表示使用积分]</p>
	 * Created on 2015-12-8
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @return 
	 */
	public Long selectByTypeCount(@Param("entity") UserIntegralTrajectory entity);
	/**
	 * 
	 * <p>Description: [查询用户总积分]</p>
	 * Created on 2015-12-8
	 * @author 周志军
	 * @param userIntegralTrajectoryDTO
	 * @return 
	 */
	public Long selectTotalIntegral(@Param("userId") Long userId);
}