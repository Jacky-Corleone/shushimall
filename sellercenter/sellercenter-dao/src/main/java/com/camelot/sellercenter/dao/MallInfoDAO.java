package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.MallInfo;
import com.camelot.sellercenter.logo.dto.LogoDTO;
/**
 * 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface MallInfoDAO extends BaseDAO<MallInfo>{
	/**
	 * 
	 * <p>Discription:[查询所有的logo记录]</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[马桂雷]
	 */
	public List<LogoDTO> findAll();
	/**
	 * 
	 * <p>Description: [根据平台ID查询logo记录]</p>
	 * Created on 2015年9月1日
	 * @param platformId 平台ID
	 * @return
	 * @author:[宋文斌]
	 */
	public List<LogoDTO> findByPlatformId(@Param("platformId") Integer platformId);
	/**
	 * 
	 * <p>Discription:[修改科印logo]</p>
	 * Created on 2015年1月26日
	 * @param logoName
	 * @param picUrl
	 * @return
	 * @author:[马桂雷]
	 */
	public Integer updateAll(@Param("logoName")String logoName, @Param("picUrl")String picUrl);
	/**
	 * 
	 * <p>Discription:[根据平台ID修改Logo]</p>
	 * Created on 2015年1月26日
	 * @param logoName
	 * @param picUrl
	 * @param platform_id
	 * @return
	 * @author:[马桂雷]
	 */
	public Integer updateByPlatformId(@Param("logoName")String logoName, @Param("picUrl")String picUrl, @Param("platformId")Integer platformId);
}
