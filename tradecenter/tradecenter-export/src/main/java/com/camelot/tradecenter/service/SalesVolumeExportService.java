package com.camelot.tradecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.SalesVolumeDTO;

/**
 * 
 * <p>Description: [销量service]</p>
 * Created on 2015-4-15
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface SalesVolumeExportService {

	/**
	 * 
	 * <p>Discription:[查询SKU的销量]</p>
	 * Created on 2015-4-15
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<List<SalesVolumeDTO>> querySkuSalesVolume(SalesVolumeDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[查询店铺的销量]</p>
	 * Created on 2015-4-15
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<List<SalesVolumeDTO>> queryShopSalesVolume(SalesVolumeDTO inDTO);
}
