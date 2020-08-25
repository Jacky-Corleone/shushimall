package com.camelot.pricecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;

/**
 * 
 * <p>Description: [运费计算服务接口]</p>
 * Created on 2015年11月24日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCartFreightService {

	/**
	 * 
	 * <p>Description: [计算使用同一运费模版的一组商品的每种运送方式的运费，如果没有运送方式，就采用默认的运送方式：快递]</p>
	 * Created on 2015年11月25日
	 * @param productInPriceDTOs
	 * @param isPreferental 是否计算运费优惠  true：是  false：否
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<DeliveryTypeFreightDTO>> calcEveryDeliveryTypeFreight(List<ProductInPriceDTO> productInPriceDTOs, boolean isPreferental);
	
}
