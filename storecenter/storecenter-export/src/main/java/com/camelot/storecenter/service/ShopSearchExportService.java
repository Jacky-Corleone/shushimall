package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;

public interface ShopSearchExportService {

	
	/**
	 * 
	 * <p>Discription:[搜索店铺]</p>
	 * Created on 2015-4-17
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	@SuppressWarnings("rawtypes")
	public ExecuteResult<DataGrid<ShopDTO>> searchShop(ShopDTO inDTO,Pager page);
	
}
