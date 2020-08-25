package com.camelot.cmscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsSiteDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;

/**
 * 
 * <p>Description: [提供前后台查询DAO]</p>
 * Created on 20160223
 * @author  <a href="mailto: xxx@camelotchina.com">lj</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CMSSiteDAO extends CmsDAO<CmsSiteDTO> {
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:根据id查询文章的详情]</p>
	 * Created on 20160223
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsSiteDTO queryById(@Param("id") String id);

}