package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: 表情服务</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsArtTagsService {
	/**
	 * 
	 * <p>Discription:文章列表</p>
	 * Created on 2016年2月26日
	 * @param page
	 * @param cmsArticleTagsDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsArticleTagsDto> queryCmsArtTagsList(Pager page, CmsArticleTagsDto cmsArticleTagsDto);
	
	/**
	 * 
	 * <p>Discription:文章详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public CmsArticleTagsDto getCmsArtTagsById(String id);
	/**
	 * <p>Discription:tags修改</p>
	 * Created on 2016年2月26日
	 * @return
	 * @author:[mengbo]
	 */
}
