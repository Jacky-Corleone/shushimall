package com.camelot.usercenter.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 
 * <p>Description: [用户与平台服务规则关系表DAO]</p>
 * Created on 2015-3-10
 * @author  <a href="mailto: xxx@camelotchina.com">liuqsh</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserMallResourceMybatisDAO  extends BaseDAO<UserMallResourceDTO>{
	
	/**
	 * 
	 * <p>Discription:[根据类型 查询所有父级资源]</p>
	 * Created on 2015-3-16
	 * @param type
	 * @param modularType 
	 * @return
	 * @author:yuht
	 */
	List<UserMallResourceDTO> queryParentResourceList(@Param("type")Integer type,@Param("modularType")Integer modularType);

	/**
	 * 
	 * <p>Discription:[根据用户ID查询出用户拥有的资源]</p>
	 * Created on 2015-3-18
	 * @param uid 用户ID
	 * @return
	 * @author:yuht
	 */
	List<UserMallResourceDTO> selectMallResourceById(@Param("uid")Long uid,@Param("modularType")Long modularType);

}