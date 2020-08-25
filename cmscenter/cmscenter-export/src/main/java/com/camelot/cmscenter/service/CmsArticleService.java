package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: 文章服务</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsArticleService {
	/**
	 * 
	 * <p>Discription:文章列表</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsArticleDTO> queryCmsArticleList(Pager page, CmsArticleDTO cmsArticleDTO);
	
	/**
	 * 
	 * <p>Discription:文章详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsArticleDTO getCmsArticleById(String id);
	


}
