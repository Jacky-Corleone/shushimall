package com.camelot.cmscenter.service;

import java.util.List;

import com.camelot.cmscenter.dto.CmsSpecialSubjectModelDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface CmsSpecialSubjectModelService {

	/**
	 * 
	 * <p>Discription:查询分页列表</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:mengbo
	 */
	public ExecuteResult<DataGrid<CmsSpecialSubjectModelDTO>>  queryCmsSpecSubModelList(CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO,Pager<?> page) throws Exception;
	
	/**
	 * 
	 * <p>Discription:查询]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public CmsSpecialSubjectModelDTO getCmsSpecialSubjectModel(String id)throws Exception;
	
	/**
	 * 
	 * <p>Discription:删除]</p>
	 * Created on 2016年2月22日
	 * @param id
	 * @return
	 * @author:[lj]
	 */
	public boolean deleteCmsSpecialSubjectModel(String id)throws Exception;
	/**
	 * 保存
	 * @param specialSubjectDTO
	 * @return
	 * @throws Exception
	 */
	public boolean saveCmsSpecialSubjectModel(CmsSpecialSubjectModelDTO specialSubjectModelDTO)throws Exception;
	
	/**
	 * 保存
	 * @param specialSubjectDTO
	 * @return
	 * @throws Exception
	 */
	public boolean updateCmsSpecialSubjectModel(CmsSpecialSubjectModelDTO specialSubjectModelDTO)throws Exception;
	
	/**
	 * 查询所有模版
	 * @return
	 */
	public List<CmsSpecialSubjectModelDTO> queryModelList()throws Exception;
}
