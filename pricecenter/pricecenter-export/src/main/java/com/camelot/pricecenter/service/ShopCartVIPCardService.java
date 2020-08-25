package com.camelot.pricecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ProductOutPriceDTO;

/**
 * 
 * <p>Description: [VIP卡服务接口]</p>
 * Created on 2015年11月24日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCartVIPCardService {

	/**
	 * 
	 * <p>Description: [计算vip卡优惠活动]</p>
	 * Created on 2015年11月24日
	 * @param productInPriceDTOs
	 * @param promoCode 优惠码
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<ProductOutPriceDTO>> calculateVIPCard(List<ProductInPriceDTO> productInPriceDTOs, String promoCode);
}
