package com.camelot.sellercenter.mallword.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;

/**
 * 商城搜索词设置
 * @author Administrator
 *
 */
public interface MallWordExportService {
	
	/**
	 * 添加商城搜索词
	 * @param dto
	 * @return
	 */
	public ExecuteResult<MallWordDTO> add(MallWordDTO dto);
	
	/**
	 * 删除商城搜索词
	 * @param id
	 * @return
	 */
	public ExecuteResult<String> delete(Long id);
	
	/**
	 * 查询商城搜索词
	 * @param dto
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<MallWordDTO> datagrid(MallWordDTO dto,Pager page);
}
