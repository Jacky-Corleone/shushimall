package com.camelot.usercenter.dao;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.github.pagehelper.Page;

/**
 * <p>Description: [用户成长值记录的DAO]</p>
 * Created on 2015-12-8
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserGrowthDAO extends BaseDAO<UserGrowthDTO>{
	/*
	 * 添加成长值记录
	 */
    public void addUserGrowthInfo(UserGrowthDTO userGrowthDTO);
    
}
