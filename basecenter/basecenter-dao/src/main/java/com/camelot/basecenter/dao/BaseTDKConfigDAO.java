package com.camelot.basecenter.dao;

import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [tdk设置DAO]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface BaseTDKConfigDAO extends BaseDAO<BaseTDKConfigDTO> {
	 
	
	/**
	 * 查询tdk设置
	 * @param baseTDKConfigDTO
	 * @return
	 */
	java.util.List<BaseTDKConfigDTO> queryBaseTDKConfigList();
	
}
