package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.InventoryModifyDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [库存]</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface TradeInventoryExportService {
    
	/**
	 * <p>Discription:[根据skuId查询库存]</p>
	 * Created on 2015年3月18日
	 * @param skuId
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<TradeInventoryDTO> queryTradeInventoryBySkuId(Long skuId);
	/**
	 * <p>Discription:[商品库存列表查询]</p>
	 * Created on 2015年3月13日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<DataGrid<TradeInventoryOutDTO>> queryTradeInventoryList(TradeInventoryInDTO dto,Pager page);

	/**
	 * <p>Discription:[批量修改库存量]</p>
	 * Created on 2015年3月13日
	 * @param ids
	 * @return
	 * @author:[杨芳]
	 */
	 public ExecuteResult<String> modifyInventoryByIds(List<InventoryModifyDTO> dto);
}
