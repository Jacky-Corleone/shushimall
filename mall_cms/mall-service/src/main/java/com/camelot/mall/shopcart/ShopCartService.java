package com.camelot.mall.shopcart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopCartService {
	/**
	 * 
	 * <p>Discription:[商品添加购物车]</p>
	 * Created on 2015年3月4日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> add(ProductInPriceDTO product);

	/**
	 * 
	 * <p>Discription:[购物车商品信息修改]</p>
	 * Created on 2015年3月4日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> edit(ProductInPriceDTO product);

	/**
	 * 
	 * <p>Discription:[购物车商品移除]</p>
	 * Created on 2015年3月4日
	 * @param products
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> del(ProductInPriceDTO product);
	
	/**
	 * <p>Discription:[获取购物车对象]</p>
	 * @param ctoken
	 * @param uid
	 * @param view
	 * @param freightDeliveryType 运费模版对应的运送方式，key=运费模版ID，value=运送方式
	 * @return
	 */
	public ShopCartDTO getMyCart(String ctoken, String uid, Boolean carding, Map<Long,Integer> freightDeliveryType,String areaCode);
	
	/**
	 * 
	 * <p>Discription:[选中/取消选中店铺所有商品]</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @param shopid
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> checkShop(String ctoken, String uid, Long shopid, Boolean checked);
	
	/**
	 * <p>Discription:[选中/取消选中所有商品]</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @param shopid
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> checkAll(String ctoken, String uid, Boolean checked);
	
	/**
	 * 
	 * <p>Discription:[变更购物车商品区域（场景：收货地址变更时）]</p>
	 * Created on 2015年3月11日
	 * @param ctoken
	 * @param uid
	 * @param regionId
	 * @param orderType 订单类型 0询价订单 1协议订单 2正常
	 * @param buyType 立即购买还是购物车购买
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> changeRegion(String ctoken, String uid, String regionId, Integer orderType,
			String buyType);
	
	/**
	 * 
	 * <p>Description: [创建订单]</p>
	 * Created on 2015年12月10日
	 * @param products
	 * @param promptly
	 * @param shopPromoId
	 * @param couponId 优惠券编号
	 * @param integral 积分
	 * @param ctoken
	 * @param uid
	 * @param dto
	 * @param freightDeliveryType
	 * @param platformId 平台ID（1:印刷家平台,2:绿印平台,3:小管家）。当买家使用了优惠券或积分时此参数必须。
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> subimtOrder(OrderParameter parameter);
	
	/**
	 * 
	 * <p>Discription: 订单支付校验</p>
	 * Created on 2015年3月17日
	 * @param orderNo
	 * @param type
	 * @param paymentMethod
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<Integer> payOrder(String orderNo,String type,String paymentMethod);

	/**
	 * 
	 * <p>Description: [组装询价订单和协议订单的订单信息]</p>
	 * Created on 2015年11月12日
	 * @param uid
	 * @param freightDeliveryType
	 * @return
	 * @author:[宋文斌]
	 */
	public ShopCartDTO getMyOrder(String uid, Map<Long, Integer> freightDeliveryType);
	
	/**
	 * 
	 * <p>Discription:[获取立即购买对象，暂时没用到，以后做立即购买会用到]</p>
	 * Created on 2015-12-4
	 * @param pros
	 * @param uid
	 * @param carding
	 * @return
	 * @author:[创建者中文名字]
	 */
	public ShopCartDTO getPromptly(List<ProductInPriceDTO> pros,String uid, Boolean carding);
	
	/**
	 * 
	 * <p>Discription:[跳转到订单核对页]</p>
	 * Created on 2015-12-4
	 * @param pros
	 * @param ctoken
	 * @param uid
	 * @param carding
	 * @param promptly
	 * @param couponId 优惠券编号
	 * @param paymentMethod 个人支付或企业支付（1个人 2企业；非必须）
	 * @param integral 积分
	 * @param freightDeliveryType
	 * @param platformId 平台Id（1:印刷家平台,2:绿印平台,3:小管家）
	 * @param promotion 满减活动
	 * @return
	 * @author:[王鹏]
	 */
	public ShopCartDTO getMyCartToOrderView(List<ProductInPriceDTO> pros, String ctoken, String uid, Boolean carding,
			String promptly, String couponId, Integer paymentMethod, Integer integral, Map<Long, Integer> freightDeliveryType,
			Integer platformId,String promotion,String markdown);
	
	/**
	 * 
	 * <p>Description: [获取当前用户购物车商品]</p>
	 * Created on 2015年12月4日
	 * @param ctoken
	 * @param uid
	 * @return
	 * @author:[宋文斌]
	 */
	public List<ProductInPriceDTO> getAllProducts(String ctoken,String uid);
	
}