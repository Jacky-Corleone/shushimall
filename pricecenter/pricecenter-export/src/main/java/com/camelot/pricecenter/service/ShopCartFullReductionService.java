package com.camelot.pricecenter.service;

import java.math.BigDecimal;
import java.util.Map;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.usercenter.dto.UserDTO;


/**
 * 
 * <p>Description: [计算满减后的支付金额]</p>
 * Created on 2015-12-15
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCartFullReductionService {
	
	/**
	 * 
	 * <p>Discription:[计算满减信息]</p>
	 * Created on 2015-12-15
	 * @param cart 购物车中数据
	 * @param shopPromoId 满减活动id
	 * @param user 用户信息
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<ShopCartDTO> calcFullReduction(ShopCartDTO cart,String shopPromoId,UserDTO user);
	
	/**
	 * 
	 * <p>Discription:[获取最优促销活动id]</p>
	 * Created on 2015-12-15
	 * @param myCart
	 * @return
	 * @author:[王鹏]
	 */
	public Map<String,ShopCartDTO> findShopPromotionId(ShopCartDTO myCart);

}
