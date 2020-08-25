package com.camelot.cmscenter.dao;

import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListJoinDto;
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
public interface CmsArticleTagsListDAO extends CmsDAO<CmsArticleTagsListDAO> {
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询列表  ]</p>
	 * Created on 20160223
	 * @param 
	 * @param page
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListDto> queryArtTagList(@Param("entity") CmsArticleTagsListDto cmsArticleTagsListDto, @Param("page") Pager page);
	/**
	 *
	 * <p>Discription:[方法功能中文描述:查询列表  ]</p>
	 * Created on 20160223
	 * @param
	 * @param page
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListJoinDto> queryArtTagjoinList(@Param("entity") CmsArticleTagsListDto cmsArticleTagsListDto, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[按照文章id查询]</p>
	 * Created on 20160223
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListDto> queryByArtId(@Param("id") String id);
	/**
	 *
	 * <p>Discription:[按照图片id查询]</p>
	 * Created on 20160223
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListDto> queryByImgId(@Param("id") String id);
	/**
	 *
	 * <p>Discription:[按照主键id查询]</p>
	 * Created on 20160223
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public CmsArticleTagsListDto queryById(@Param("id") String id);

	public Long queryCount(@Param("entity") CmsArticleTagsListDto cmsArticleTagsListDto);

	public void delete(@Param("id") String id);
	public void deleteByImg(@Param("id") String id);

	public void modifyCmsArtTagsList(@Param("entity") CmsArticleTagsListDto cmsArticleTagsListDto);
	public void insertTagList(@Param("entity") CmsArticleTagsListDto cmsArticleTagsListDto);
}