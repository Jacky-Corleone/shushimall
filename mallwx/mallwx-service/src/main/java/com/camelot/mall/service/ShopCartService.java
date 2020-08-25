package com.camelot.mall.service;

import java.util.List;

import com.camelot.mall.shopcart.Product;
import com.camelot.mall.shopcart.ShopCart;
import com.camelot.openplatform.common.ExecuteResult;
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
	public ExecuteResult<String> add(Product product);

	/**
	 * 
	 * <p>Discription:[购物车商品信息修改]</p>
	 * Created on 2015年3月4日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> edit(Product product);

	/**
	 * 
	 * <p>Discription:[购物车商品移除]</p>
	 * Created on 2015年3月4日
	 * @param products
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> del(Product product);
	
	/**
	 * 
	 * <p>Discription:根据复选框内容进行批量删除购物</p>
	 * Created on 2015年11月13日15:27:57
	 * @param product 缓存商品信息
	 * @param objskuId 复选框选择商品
	 * @return
	 * @author:[訾瀚民]
	 */
	public ExecuteResult<String> delAll(Product product,String objskuId);
	
	
	/**
	 * <p>Discription:[获取购物车对象]</p>
	 * @param ctoken
	 * @param uid
	 * @param view
	 * @return
	 */
	public ShopCart getMyCart(String ctoken, String uid, Boolean carding);
	
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
	 * <p>Discription:[变更购物车商品区域（场景：收货地址变更时）]</p>
	 * Created on 2015年3月11日
	 * @param ctoken
	 * @param uid
	 * @param regionId
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> changeRegion(String ctoken,String uid,String regionId);
	
	
	/**
	 * 
	 * <p>Discription: 创建订单</p>
	 * Created on 2015年3月17日
	 * @param ctoken
	 * @param uid
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> subimtOrder(String ctoken,String uid, TradeOrdersDTO dto);
	
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
	 * <p>Discription: 订单支付校验</p>
	 * Created on 2015年3月17日
	 * @param orderNo
	 * @param type
	 * @param paymentMethod
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public List<Product> AllProducts(String ctoken,String uid);
	
	
	
}