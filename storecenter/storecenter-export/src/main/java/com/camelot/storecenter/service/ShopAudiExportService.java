package com.camelot.storecenter.service;


import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.combin.ShopAudiDTO;


public interface ShopAudiExportService  {
	
 
	/**
	 * 
	 * <p>Discription:[店铺审核信息详情查询查询]</p>
	 * Created on 2015-3-7
	 * @param shopAudiinDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<ShopAudiDTO> queryShopAuditInfo(Long shopId); 
	
}
