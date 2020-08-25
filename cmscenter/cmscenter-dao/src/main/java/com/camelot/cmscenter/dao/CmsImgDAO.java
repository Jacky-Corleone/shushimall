package com.camelot.cmscenter.dao;

import com.camelot.cmscenter.domain.CmsImageInfo;
import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:楼层数据交互接口]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsImgDAO extends CmsDAO<CmsImageInfo> {
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:  根据修改 ]</p>
	 * Created on 2015年1月22日
	 * @param cmsImageDto
	 * @return
	 * @author:[mengbo]
	 */
	public void modifyCmsImage(@Param("entity") CmsImageDto cmsImageDto);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询图片的列表  ]</p>
	 * Created on 2015年1月23日
	 * @param cmsImageDto
	 * @param page
	 * @return
	 * @author:[mengbo]
	 */
	public List<CmsImageDto> queryDTOList(@Param("entity") CmsImageDto cmsImageDto, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:根据id查询楼层列表的详情]</p>
	 * Created on 2015年1月23日
	 * @param idDTO
	 * @return
	 * @author:[chenx]
	 */
	public CmsImageDto queryDTOById(@Param("idDTO") String idDTO);
	
	public Long queryCount(@Param("entity") CmsImageDto cmsImageDto);
}