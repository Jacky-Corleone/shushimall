package com.camelot.cmscenter.service;

import com.camelot.cmscenter.dto.CmsExperStoreDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface CmsExperStoreService {
	/**
	 * 增
	 * @param cmsExperStoreDTO
	 * @return
	 */
	public boolean add(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 删
	 * @param cmsExperStoreDTO
	 * @return
	 */
	public boolean delete(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 改
	 * @param cmsExperStoreDTO
	 * @return
	 */
	public boolean update(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 查   根据Id 查询
	 * @return
	 */
	public CmsExperStoreDTO queryById(String id);
	/**
	 * 分页查询
	 * @param cmsExperStoreDTO
	 * @param page
	 * @return
	 */
	public ExecuteResult<DataGrid<CmsExperStoreDTO>>  queryExperStoreList(CmsExperStoreDTO cmsExperStoreDTO,Pager<?> page);
	
}
