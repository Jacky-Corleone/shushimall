package com.camelot.goodscenter.service;

import java.math.BigDecimal;
import java.util.List;

import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.dto.SellPrice;

public interface ItemPriceService {

	/**
	 * 
	 * <p>Discription:[获取大客户价格,需求未定，逻辑未实现]</p>
	 * Created on 2015-3-7
	 * @param param
	 * @return
	 * @author:wangcs
	 */
	public BigDecimal getB2BPrice(PriceQueryParam param) throws Exception;
	
	/**
	 * 
	 * <p>Discription:[查询用于显示的SKU的区域阶梯价格]</p>
	 * Created on 2015-3-7
	 * @return
	 * @throws Exception
	 * @author:wangcs
	 */
	public BigDecimal getSkuAreaPrice(PriceQueryParam param) throws Exception;
	
	/**
	 * 
	 * <p>Discription:[查询用于显示的商品的区域阶梯价格]</p>
	 * Created on 2015-3-7
	 * @return
	 * @throws Exception
	 * @author:wangcs
	 */
	public BigDecimal getItemAreaPrice(PriceQueryParam param) throws Exception;
	
	/**
	 * 
	 * <p>Discription:[查询用于显示的用户询价]</p>
	 * Created on 2015-3-7
	 * @return
	 * @throws Exception
	 * @author:wangcs
	 */
	public BigDecimal getInquiryPrice(PriceQueryParam param) throws Exception;

	/**
	 * 
	 * <p>Discription:[根据skuid查询查询SKU阶梯价格]</p>
	 * Created on 2015-3-7
	 * @param skuId
	 * @return
	 * @author:wangcs
	 */
	public List<SellPrice> querySkuSellPrices(Long skuId) throws Exception;
	
	/**
	 * 
	 * <p>Discription:[
	 * 获取SKU显示价格
	 * param.setAreaCode(skuDTO.getAreaCode());
		param.setItemId(skuDTO.getItemId());
		param.setQty(skuDTO.getQty());
		param.setShopId(skuDTO.getShopId());
		param.setSkuId(skuDTO.getSkuId());
	 * ]</p>
	 * Created on 2015-3-26
	 * @param param
	 * @return
	 * @throws Exception
	 * @author:wangcs
	 */
	public BigDecimal getSkuShowPrice(PriceQueryParam param) throws Exception;
}
