package com.camelot.maketcenter.service;

import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [集采活动订单]</p>
 * Created on 2015年12月17日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CentralPurchasingRefOrderExportService {
	/**
	 * 
	 * <p>Discription:[新增订单]</p>
	 * Created on 2015年12月17日
	 * @param centralPurchasingRefOrderDTO
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<Boolean> addCentralPurchasingRefOrder(CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO);
	/**
	 * 
	 * <p>Discription:[查询订单]</p>
	 * Created on 2015年12月17日
	 * @param centralPurchasingRefOrderDTO
	 * @param page
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> queryCentralPurchasingRefOrder(
			CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO, Pager page);
}
