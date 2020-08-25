package com.camelot.maketcenter.service;

import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [活动信息]</p>
 * Created on 2015-4-15
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface PromotionInfoExportService {

	/**
	 * 
	 * <p>Discription:[查询促销活动信息]</p>
	 * Created on 2015-4-15
	 * @param promotionInfo
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public  ExecuteResult<DataGrid<PromotionInfo>>  queryPromotionInfoList(PromotionInfo promotionInfo,Pager page);

	/**
	 * <p>Discription:[查看当前店铺是否所有商品存在满减与直降活动]</p>
	 * @param shopId 店铺ID
	 * @return
	 * @autor: WHW
	 */
	public boolean queryActivityCheck(Long shopId);
	
}


