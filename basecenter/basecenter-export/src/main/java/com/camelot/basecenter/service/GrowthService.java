package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015-12-4
 * @author  <a href="mailto: fandongzang98@camelotchina.com">范东藏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface GrowthService {

	/**
	 * 
	 * <p>Discription:[查询成长值配置列表]</p>
	 * Created on 2015-12-4
	 * @param page
	 * @param growthQueryDTO
	 * @return
	 * @author:[范东藏]
	 */
	public DataGrid<GrowthDTO> queryGrowthList(Pager page, GrowthDTO GrowthDTO);
	
	/**
	 * 
	 * <p>Discription:[根据id查询成长值]</p>
	 * Created on 2015-12-4
	 * @param id
	 * @return
	 * @author:[范东藏]
	 */
	public ExecuteResult<String> addGrowth(GrowthDTO growthDTO);
	/**
	 * 
	 * <p>Discription:[修改增长值]</p>
	 * Created on 2015-12-4
	 * @param growthDto
	 * @return
	 * @author:[范东藏]
	 */
	public ExecuteResult<String> updateGrowth(GrowthDTO growthDto);
	
	/**
	 * 
	 * <p>Discription:[根据dto查询类型]</p>
	 * Created on 2015-12-4
	 * @param growthDto
	 * @return
	 * @author:[范东藏]
	 */
	public GrowthDTO getGrowthDTO(GrowthDTO growthDTO);
	
	
}
