package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;

/**
 * 
 * <p>Description: [店铺评价]</p>
 * Created on 2015-3-11
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopEvaluationService {
	
	/**
	 * 保存店铺评价
	 * @param shopEvaluationDTO
	 * @return
	 */
	public ExecuteResult<ShopEvaluationDTO> addShopEvaluation(ShopEvaluationDTO shopEvaluationDTO);
	
	/**
	 * 店铺评价统计
	 * @param shopEvaluationDTO
	 * @return
	 */
	public ExecuteResult<ShopEvaluationTotalDTO> queryShopEvaluationTotal(ShopEvaluationQueryInDTO shopEvaluationQueryInDTO);
	
	/**
	 * 分页查询店铺评价数据
	 * @param shopEvaluationQueryInDTO
	 * @param page
	 * @return
	 */
	public DataGrid<ShopEvaluationDTO> queryShopEvaluationDTOList(ShopEvaluationQueryInDTO shopEvaluationQueryInDTO,Pager page);
	
	
}
