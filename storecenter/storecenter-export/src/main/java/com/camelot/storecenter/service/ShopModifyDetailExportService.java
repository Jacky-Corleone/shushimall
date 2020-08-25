package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;

/**
 * 店铺信息修改详情
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015-3-13
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopModifyDetailExportService {

	/**
	 * 
	 * <p>Discription:[店铺信息修改详情]</p>
	 * Created on 2015-3-13
	 * @param shopModifyDetailDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopModifyDetailDTO>> queryShopModifyDetail(ShopModifyDetailDTO shopModifyDetailDTO,Pager page);
}
