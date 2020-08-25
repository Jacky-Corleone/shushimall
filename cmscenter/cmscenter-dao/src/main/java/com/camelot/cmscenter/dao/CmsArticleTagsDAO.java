package com.camelot.cmscenter.dao;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * <p>Description: [提供前后台查询DAO]</p>
 * Created on 20160223
 * @author  <a href="mailto: xxx@camelotchina.com">lj</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsArticleTagsDAO extends CmsDAO<CmsArticleTagsDAO> {
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询列表  ]</p>
	 * Created on 20160223
	 * @param 
	 * @param page
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsDto> queryArtTagList(@Param("entity") CmsArticleTagsDto cmsArticleTagsDto, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:根据id查询文章的详情]</p>
	 * Created on 20160223
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public CmsArticleTagsDto queryById(@Param("id") String id);
	
	public Long queryCount(@Param("entity") CmsArticleTagsDto CmsArticleTagsDto);
}