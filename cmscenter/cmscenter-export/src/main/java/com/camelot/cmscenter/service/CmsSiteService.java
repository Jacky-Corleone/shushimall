package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsSiteDTO;

/**
 * 
 * <p>Description: 站点服务</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsSiteService {

	/**
	 * 
	 * <p>Discription:栏目详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsSiteDTO getCmsSiteById(String id);
	


}
