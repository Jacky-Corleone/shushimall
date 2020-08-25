package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: 专题Service</p>
 * Created on 2016年2月24日
 * @author  lj
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CmsSpecialSubjectService {
	/**
	 * 
	 * <p>Discription:查询分页列表</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:mengbo
	 */
	public ExecuteResult<DataGrid<CmsSpecialSubjectDTO>> queryCmsSpecialSubjectList(Pager<?> page, CmsSpecialSubjectDTO specialSubjectDTO) throws Exception;
	
	/**
	 * 
	 * <p>Discription:查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsSpecialSubjectDTO getCmsSpecialSubjectById(String id)throws Exception;
	
	/**
	 * 
	 * <p>Discription:删除]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public boolean deleteCmsSpecialSubjectById(CmsSpecialSubjectDTO specialSubjectDTO)throws Exception;
	/**
	 * 保存
	 * @param specialSubjectDTO
	 * @return
	 * @throws Exception
	 */
	public boolean saveCmsSpecialSubject(CmsSpecialSubjectDTO specialSubjectDTO)throws Exception;
	/**
	 * 更新
	 * @param specialSubjectDTO
	 * @return
	 * @throws Exception
	 */
	public boolean updateCmsSpecialSubject(CmsSpecialSubjectDTO specialSubjectDTO)throws Exception;
	/**
	 * 发布
	 * @param specialSubjectDTO
	 * @return
	 * @throws Exception
	 */
	public boolean release(CmsSpecialSubjectDTO specialSubjectDTO)throws Exception;

}
