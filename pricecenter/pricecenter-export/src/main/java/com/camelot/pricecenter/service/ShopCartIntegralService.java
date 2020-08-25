package com.camelot.pricecenter.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;

/**
 * 
 * <p>Description: [积分]</p>
 * Created on 2015年12月15日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCartIntegralService {
	
	/**
	 * 
	 * <p>Description: [校验用户使用的积分是否正确]</p>
	 * Created on 2015年12月12日
	 * @param uid
	 * @param integral 使用的积分
	 * @param platformId 1或null:印刷家 2:绿印 3:小管家
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ShopCartDTO> validateIntegral(ShopCartDTO shopCartDTO, Long uid, Integer integral, Integer platformId);

	/**
	 * 
	 * <p>Description: [积分优惠计算]</p>
	 * Created on 2015年12月11日
	 * @param shopCartDTO
	 * @param platformId 1或null:印刷家平台,2:绿印平台,3:小管家
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ShopCartDTO> calcIntegral(ShopCartDTO shopCartDTO, Long uid, Integer platformId);
	
}
