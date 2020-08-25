package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserGrowthDTO;

/**
 * 
 * <p>Description: [成长值记录的调用接口]</p>
 * Created on 2015-12-8
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserGrowthService {
	/*
	 * 添加成长值记录
	 */
	public void addUserGrowthInfo(UserGrowthDTO userGrowthDTO);
	
	/*
	 * 根据用户id查询成长值记录
	 */
	public ExecuteResult<DataGrid<UserGrowthDTO>> selectList(long userId,Pager pager);
	
	
}
