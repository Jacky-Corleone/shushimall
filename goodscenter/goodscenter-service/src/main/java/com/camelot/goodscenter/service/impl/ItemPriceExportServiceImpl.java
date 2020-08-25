package com.camelot.goodscenter.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemPriceExportService;
import com.camelot.goodscenter.service.ItemPriceService;

@Service("itemPriceExportService")
public class ItemPriceExportServiceImpl implements ItemPriceExportService {

	private static final Logger logger = LoggerFactory.getLogger(ItemPriceExportServiceImpl.class);
	
	@Resource
	private ItemPriceService itemPriceService;
	
	@Override
	public BigDecimal getSkuShowPrice(ItemShopCartDTO skuDTO) {
		PriceQueryParam param = new PriceQueryParam();
		param.setAreaCode(skuDTO.getAreaCode());
		param.setItemId(skuDTO.getItemId());
		param.setQty(skuDTO.getQty()==null? 1:skuDTO.getQty());
		param.setShopId(skuDTO.getShopId());
		param.setSkuId(skuDTO.getSkuId());
		param.setBuyerId(skuDTO.getBuyerId());
		param.setSellerId(skuDTO.getSellerId());
		BigDecimal price = null;
		try {
			price = itemPriceService.getSkuShowPrice(param);
		} catch (Exception e) {
			logger.error("执行方法【getSkuShowPrice】报错：{}",e);
			throw new RuntimeException(e);
		}
		return price;
	}

}
