package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopTemplatesDTO;


/**
 * 
 * <p>Description: [平台级店铺模板]</p>
 * Created on 2015-4-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopTemplatesExportService {
	
	/**
	 * 
	 * <p>Discription:[查询平台店铺模板List]</p>
	 * Created on 2015-4-9
	 * @param shopTemplateIn
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<ShopTemplatesDTO>> createShopTemplatesList(ShopTemplatesDTO shopTemplatesDTO);
	
	/**
	 * 
	 * <p>Discription:[修改店铺模板状态]</p>
	 * Created on 2015-4-10
	 * @param templatesId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopTemplatesStatus(Long templatesId,Long shopId);
	
	/**
	 * 
	 * <p>Discription:[修改店铺模板颜色]</p>
	 * Created on 2015-4-14
	 * @param templatesId
	 * @param color
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopTemplatesColor(Long templatesId,String color);
	
}
