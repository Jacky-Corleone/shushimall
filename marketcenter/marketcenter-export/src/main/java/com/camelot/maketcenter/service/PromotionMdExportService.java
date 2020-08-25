package com.camelot.maketcenter.service;

import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [活动直降]</p>
 * Created on 2015-4-15
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface PromotionMdExportService {

	/**
	 * 
	 * <p>Discription:[查询直接活动]</p>
	 * Created on 2015-4-15
	 * @param promotionMdDTO 
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<PromotionMarkdown>>  queryPromotionMdList(PromotionMarkdown promotionMarkdown,Pager page);

	/**
	 * 删除限时折扣
	 * @param promotionInfoId
	 * @return
	 */
	public ExecuteResult<String> deleteMdByPromotionId(Long promotionInfoId);

	/**
	 * 删除限时折扣
	 * @param promotionMarkdown
	 * @return
	 */
	public ExecuteResult<String> addMdByPromotionId(PromotionMarkdown promotionMarkdown);
	
	/**
	 * <p>Discription:[查看当前店铺的下架操作的商品是否所有商品存在促销活动]</p>
	 * @param shopId 店铺ID
	 * @param itemIds 商品ids
	 * @return
	 * @autor: WHW
	 */
	public boolean queryActivityCheck(Long shopId,Long[] itemIds);
}
