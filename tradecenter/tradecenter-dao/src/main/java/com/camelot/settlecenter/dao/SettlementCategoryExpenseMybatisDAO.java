package com.camelot.settlecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface SettlementCategoryExpenseMybatisDAO extends BaseDAO<SettleCatExpenseDTO> {
    /**
     * <p>Discription:[根据类目id或者类目id组查询]</p>
     * Created on 2015年3月19日
     * @param ids
     * @return
     * @author:[杨芳]
     */
	public List<SettleCatExpenseDTO> queryByIds(@Param("ids") Long... ids);
}
