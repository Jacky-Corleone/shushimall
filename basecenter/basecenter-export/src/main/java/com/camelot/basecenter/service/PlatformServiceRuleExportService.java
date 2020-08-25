package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface PlatformServiceRuleExportService {
  
	/**
	 * <p>Discription:[根据服务规则id数组查询]</p>
	 * Created on 2015年3月19日
	 * @param ruleIds
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<List<PlatformServiceRuleDTO>> queryRuleByRuleIds(Long[] ruleIds);
	/**
	 * <p>Discription:[根据服务规则的id查询详情]</p>
	 * Created on 2015年3月18日
	 * @param ruleId
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<PlatformServiceRuleDTO> queryByRuleId(Long ruleId);
	/**
	 * <p>Discription:[平台服务规则的查询]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<DataGrid<PlatformServiceRuleDTO>> queryList(PlatformServiceRuleDTO dto,Pager page);

  /**
   * <p>Discription:[平台服务规则的修改]</p>
   * Created on 2015年3月10日
   * @param platformServiceRuleDTO
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> modifyPlatformServiceRule(PlatformServiceRuleDTO platformServiceRuleDTO);
  
  /**
   * <p>Discription:[添加服务规则]</p>
   * Created on 2015年3月18日
   * @param dto
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> addServiceRule(PlatformServiceRuleDTO dto);
  
  
  /**
   * <p>Discription:[修改服务规则的状态]</p>
   * @param platformServiceRuleDTO   status字段：0：代表被删除
   * @return
   * @author:[王东晓]
   */
  public ExecuteResult<String> modifyPlatformServiceRuleStatus(PlatformServiceRuleDTO platformServiceRuleDTO);
  
}
