package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [TDK设置服务]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface BaseTDKConfigService {
	
	/**
	 * 保存tdk设置
	 * @param baseTDKConfigDTO
	 * BaseTDKConfigService@return
	 */
	public ExecuteResult<BaseTDKConfigDTO> saveBaseTDKConfig(BaseTDKConfigDTO baseTDKConfigDTO);
	
	/**
	 * 查询tdk设置
	 * @param baseTDKConfigDTO
	 * @return
	 */
	public ExecuteResult<BaseTDKConfigDTO> queryBaseTDKConfig(BaseTDKConfigDTO baseTDKConfigDTO);
	
	/**
	 * 编辑tdk设置
	 * @param baseTDKConfigDTO
	 * @return
	 */
	public ExecuteResult<BaseTDKConfigDTO> modifyBaseTDKConfig(BaseTDKConfigDTO baseTDKConfigDTO);
	
}
