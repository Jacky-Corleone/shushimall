package com.camelot.sellercenter.logo.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.sellercenter.logo.dto.LogoDTO;
/**
 * 
 * <p>Description: [Logo服务接口]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface LogoExportService {
	/**
	 * 
	 * <p>Discription:[查询科印Logo]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<LogoDTO> getMallLogo();

	/**
	 * 
	 * <p>Description: [根据平台ID查询logo]</p>
	 * Created on 2015年9月1日
	 * @param platformId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<LogoDTO> getMallLogoByPlatformId(Integer platformId);

	/**
	 * 
	 * <p>Discription:[修改科印logo]</p>
	 * Created on 2015年1月27日
	 * @param logoName logo名称
	 * @param picUrl logo地址
	 * @param platformId 平台ID
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<String> modifyMallLogo(String logoName, String picUrl);
	
	/**
	 * 
	 * <p>Description: [根据平台ID修改logo]</p>
	 * Created on 2015年9月1日
	 * @param logoName
	 * @param picUrl
	 * @param platformId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> modifyMallLogoByPlatformId(String logoName, String picUrl, Integer platformId);
	
}
