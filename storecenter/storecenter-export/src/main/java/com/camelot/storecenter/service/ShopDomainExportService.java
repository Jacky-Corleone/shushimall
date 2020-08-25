package com.camelot.storecenter.service;

import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>Description: [店铺自定义域名]</p>
 * Created on 2015-3-11
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopDomainExportService {

	/**
	 * 
	 * <p>Discription:[校验店铺域名是否重复]</p>
	 * Created on 2015-3-11
	 * @param shopUrl
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<Boolean>  existShopUrl(String shopUrl,Long shopId);
	
	/**
	 * 
	 * <p>Description: [通过platformId和shopUrl校验店铺域名是否重复]</p>
	 * Created on 2015年9月23日
	 * @param shopUrl
	 * @param platformId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<Boolean> existShopUrlByPlatformId(String shopUrl, Integer platformId,Long shopId);
	
}
