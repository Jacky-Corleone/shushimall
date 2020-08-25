package com.camelot.goodscenter.service;

import java.math.BigDecimal;

import com.camelot.goodscenter.dto.ItemShopCartDTO;

public interface ItemPriceExportService {

	/**
	 * 
	 * <p>Discription:[查询SKU前台应该显示的价格]</p>
	 * Created on 2015-3-20
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	public BigDecimal getSkuShowPrice(ItemShopCartDTO inDTO); 
}
