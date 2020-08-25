package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: 栏目服务</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsCategoryService {
	/**
	 * 
	 * <p>Discription:栏目列表</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsCategoryDTO> queryCmsCategoryList(Pager page, CmsCategoryDTO cmsCategoryDTO);
	
	/**
	 * 
	 * <p>Discription:栏目详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsCategoryDTO getCmsCategoryById(String id);
	


}
