package com.camelot.tradecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;

/**
 * 
 * <p>Description: [套装商品订单记录]</p>
 * Created on 2016年2月25日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface TradeOrderItemsPackageExportService {
	/**
	 * 
	 * <p>Discription:[新增]</p>
	 * Created on 2016年2月25日
	 * @param dto 套装商品订单记录DTO
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<String> add(TradeOrderItemsPackageDTO dto);
	
	/**
	 * 
	 * <p>Discription:[根据条件分页查询]</p>
	 * Created on 2016年2月25日
	 * @param inDTO 套装商品订单记录DTO
	 * @param pager 分页对象
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> queryTradeOrderItemsPackageDTOs(TradeOrderItemsPackageDTO inDTO, Pager<TradeOrderItemsPackageDTO> pager);
	
	/**
	 * 
	 * <p>Discription:[根据ID查询]</p>
	 * Created on 2016年2月25日
	 * @param id 套装商品订单记录ID
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<TradeOrderItemsPackageDTO> queryById(Long id);

}
