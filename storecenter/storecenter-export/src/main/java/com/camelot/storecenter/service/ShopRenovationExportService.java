package com.camelot.storecenter.service;


import java.util.Map;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.dto.combin.ShopTemplatesCombinDTO;


/**
 * 
 * <p>Description: [店铺装修]</p>
 * Created on 2015-4-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopRenovationExportService {
	
	/**
	 * 
	 * <p>Discription:[查询店铺装修内容]</p>
	 * Created on 2015-4-10
	 * @return 
	 * @author:yuht
	 */
	public ExecuteResult<ShopTemplatesCombinDTO> queryShopRenovation(ShopRenovationDTO shopRenovationDTO);
	
	/**
	 * 根据商品ID获取店铺装修的商品内容
	 * @param itemId
	 * @return
	 */
	public DataGrid<ShopRenovationDTO> queryShopRenovationByItemId(Long itemId);
	/**
	 * 
	 * <p>Discription:[查询店铺装修内容]</p>
	 * Created on 2015-4-10
	 * @return 
	 * @author:yuht
	 */
	public ExecuteResult<ShopTemplatesCombinDTO> queryShopRenovationByShopId(Long shopId);
	
	/**
	 * 
	 * <p>Discription:[ 添加店铺装修]</p>
	 * Created on 2015-4-10
	 * @param shopRenovationDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  addShopRenovation(ShopRenovationDTO shopRenovationDTO);
	
	/**
	 * 
	 * <p>Discription:[店铺装修修改]</p>
	 * Created on 2015-4-10
	 * @param shopRenovationDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  modifyShopRenovation(ShopRenovationDTO shopRenovationDTO);
}
