package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListJoinDto;
import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

import java.util.List;

/**
 * 
 * <p>Description: 文章服务</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsArtTagsListService {
	/**
	 * 
	 * <p>Discription:文章列表</p>
	 * Created on 2016年2月26日
	 * @param page
	 * @param cmsArticleTagsListDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsArticleTagsListDto> queryCmsArtTagsListList(Pager page, CmsArticleTagsListDto cmsArticleTagsListDto);
	/**
	 *
	 * <p>Discription:文章列表</p>
	 * Created on 2016年2月26日
	 * @param page
	 * @param cmsArticleTagsListDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsArticleTagsListJoinDto> queryCmsArtTagsListListJion(Pager page, CmsArticleTagsListDto cmsArticleTagsListDto);
	/**
	 * 
	 * <p>Discription:文章详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListDto> getCmsArtTagsListByArtId(String id);
	/**
	 *
	 * <p>Discription:文章详情查询按照图片id]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsArticleTagsListDto> getCmsArtTagsListByImgId(String id);
	/**
	 *
	 * <p>Discription:按照主键详情查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public CmsArticleTagsListDto getCmsArtTagsListById(String id);
	/**
	 * <p>Discription:tags修改</p>
	 * Created on 2016年2月26日
	 * @return
	 * @author:[mengbo]
	 */
	public void modifyArtTagsList(CmsArticleTagsListDto cmsArticleTagsListDto);
	/**
	 * <p>Discription:tags新增</p>
	 * Created on 2016年2月26日
	 * @return
	 * @author:[mengbo]
	 */
	public void insertTagList(CmsArticleTagsListDto cmsArticleTagsListDto);
	/**
	 * <p>Discription:tags删除</p>
	 * Created on 2016年2月26日
	 * @return
	 * @author:[mengbo]
	 */
	public void deleteArtTagsList(String id);
	/**
	 * <p>Discription:tags删除</p>
	 * Created on 2016年2月26日
	 * @return
	 * @author:[mengbo]
	 */
	public void deleteArtTagsListByImgId(String id);


}
