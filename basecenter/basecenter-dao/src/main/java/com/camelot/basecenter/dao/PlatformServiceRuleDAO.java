package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/** 
 * <p>Description: [服务规则dao]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface PlatformServiceRuleDAO extends BaseDAO<PlatformServiceRuleDTO> {
    /**
     * <p>Discription:[根据规则id数组查询]</p>
     * Created on 2015年3月19日
     * @param ruleIds
     * @return
     * @author:[杨芳]
     */
	public List<PlatformServiceRuleDTO> queryByIds(@Param("ruleIds")Long[] ruleIds);
	
	public void deleteRuleByid(PlatformServiceRuleDTO platformServiceRuleDTO);
}
