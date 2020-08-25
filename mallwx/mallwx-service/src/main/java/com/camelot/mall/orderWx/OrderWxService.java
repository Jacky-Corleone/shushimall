package com.camelot.mall.orderWx;

import java.math.BigDecimal;
import java.util.Map;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

public interface OrderWxService {

	void changeRegion(String ctoken, String uid, String regionId);

	/**
	 * 获取购物车信息
	 * @param ctoken
	 * @param uid
	 * @param b
	 * @return
	 */
	ShopCartDTO getMyCart(String ctoken, String uid, boolean carding, Map<Long,Integer> freightDeliveryType,String promoCode,String couponId,Integer paymentMethod,Integer platformId);

	/**
	 * 订单提交
	 * @param ctoken
	 * @param uid
	 * @param dto
	 * @param contractNo 
	 * @param orderType 
	 * @return
	 */
	Map<String, Object> subimtOrder(String ctoken, String uid,TradeOrdersDTO dto,  String contractNo,String needApprove, Map<Long,Integer> freightDeliveryType,String promoCode,String couponId);

	/**
	 * 获取协议或是询价中的放在购物车中的商品
	 * @param uid
	 * @return
	 */
	String getRedisCar(String uid);
	/**
	 * 设置协议或是询价中的放在购物车中的商品
	 * @param uid
	 * @return
	 */
	void setRedisCar(String uid,String jsonProducts);

	/**
	 * 删除协议询价订单的购物车
	 * @param uid
	 */
	void delRedisCart(String uid);
	
	/**
	 * 计算店铺运费
	 * @param shop
	 * @param shopDeliveryType
	 * @return
	 */
	ExecuteResult<BigDecimal> calcShopFreight(ShopOutPriceDTO shop, Map<Long, Integer> shopDeliveryType);


	ShopCartDTO getMyOrder(String uid, Map<Long, Integer> freightDeliveryType, String contractNo);

	




}
