package com.camelot.goodscenter.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemPriceDAO;
import com.camelot.goodscenter.dao.ItemSkuInquiryPriceDAO;
import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.service.ItemPriceService;

@Service
public class ItemPriceServiceImpl implements ItemPriceService {

	@Resource
	private ItemPriceDAO itemPriceDAO;
	
	@Resource
	private ItemSkuInquiryPriceDAO itemSkuInquiryPriceDAO;
	
	@Override
	public BigDecimal getSkuAreaPrice(PriceQueryParam param) throws Exception {
		return itemPriceDAO.getSkuAreaPrice(param);
	}

	@Override
	public BigDecimal getItemAreaPrice(PriceQueryParam param) throws Exception {
		return itemPriceDAO.getItemPrice(param);
	}

	@Override
	public BigDecimal getInquiryPrice(PriceQueryParam param) throws Exception {
		ItemSkuInquiryPriceDTO inDTO = new ItemSkuInquiryPriceDTO();
		inDTO.setBuyerId(param.getBuyerId());
		inDTO.setSellerId(param.getSellerId());
		inDTO.setItemId(param.getItemId());
		inDTO.setSkuId(param.getSkuId());
		ItemSkuInquiryPriceDTO  result =itemSkuInquiryPriceDAO.selectByIdsAndNumber(inDTO);
		return result==null? null:result.getInquiryPrice();
	}

	@Override
	public List<SellPrice> querySkuSellPrices(Long skuId) throws Exception{
		return this.itemPriceDAO.querySkuSellPrices(skuId);
	}

	@Override
	public BigDecimal getB2BPrice(PriceQueryParam param) throws Exception{
		//TODO 大客户价格
		return null;
	}

	
	@Override
	public BigDecimal getSkuShowPrice(PriceQueryParam param) throws Exception{
		//大客户价格
		BigDecimal b2bPrice = this.getB2BPrice(param);
		//询价
		BigDecimal inquiryPrice = this.getInquiryPrice(param);
		//SKU地域阶梯价
		BigDecimal skuPrice = this.getSkuAreaPrice(param);
		if(skuPrice==null){
			param.setAreaCode("0");
			skuPrice = this.getSkuAreaPrice(param);
		}
		BigDecimal price = null;
		if(b2bPrice!=null){
			price = b2bPrice;
		}else if(inquiryPrice!=null){
			price = inquiryPrice;
		}else{
			price = skuPrice;
		}
		
		return price;
	}
}
