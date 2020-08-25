package com.camelot.pricecenter.service;

import java.util.List;
import java.util.Map;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ProductOutPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.usercenter.dto.UserDTO;

public interface ShopCartPromotionService {

	/**
	 * 
	 * <p>Description: [计算单个商品营销活动]</p>
	 * Created on 2015年11月24日
	 * @param productInPriceDTOs
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ProductOutPriceDTO> calcPromotion(ProductInPriceDTO productInPriceDTO,UserDTO user);
	
	/**
	 * 
	 * <p>Description: [计算集合内商品营销活动]</p>
	 * Created on 2015年11月24日
	 * @param productInPriceDTOs
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<ProductOutPriceDTO>> calcAllPromotion(List<ProductInPriceDTO> productInPriceDTOs,UserDTO user);
	
	/**
	 * 
	 * <p>Discription:[跳转订单核对页获取店铺所有商品]</p>
	 * Created on 2015-12-4
	 * @param allProducts
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<Map<Long, ShopOutPriceDTO>> getShopByProducts(List<ProductInPriceDTO> allProducts,UserDTO user);
}
