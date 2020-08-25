package com.camelot.maketcenter.service;

import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [满减活动]</p>
 * Created on 2015-4-15
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface PromotionFrExportService {

	/**
	 * 
	 * <p>Discription:[查询满减活动列表]</p>
	 * Created on 2015-4-15
	 * @param promotionFrDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<PromotionFullReduction>>  queryPromotionFrList(PromotionFullReduction promotionFullReduction,Pager page);

	/**
	 * 删除满减
	 * @param promotionInfoId
	 * @return
	 */
	public ExecuteResult<String> deleteFrByPromotionId(Long promotionInfoId);

	/**
	 * 删除满减
	 * @param promotionFullReduction
	 * @return
	 */
	public ExecuteResult<String> addFrByPromotionId(PromotionFullReduction promotionFullReduction);
	
	/**
	 * <p>Discription:[查看当前店铺的下架操作的商品是否所有商品存在促销活动]</p>
	 * @param shopId 店铺ID
	 * @param itemIds 商品ids
	 * @return
	 * @autor: WHW
	 */
	public boolean queryActivityCheck(Long shopId,Long[] itemIds);
}
