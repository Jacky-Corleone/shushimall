package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: 图片服务</p>
 * Created on 2016年2月22日
 * @author  mengbo
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsImageService {
	/**
	 * 
	 * <p>Discription:图片管理</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:mengbo
	 */
	public DataGrid<CmsImageDto> queryCmsImageList(Pager page, CmsImageDto cmsImageDto);
	
	/**
	 * 
	 * <p>Discription:图片查询]</p>
	 * Created on 2016年1月22日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 */
	public CmsImageDto getCmsImgById(String id);
	


	/**
	 * <p>Discription:图片修改</p>
	 * Created on 2016年2月22日
	 * @return
	 * @author:[mengbo]
	 */
	public void modifyCmsImg(CmsImageDto cmsImageDto);


}
