package com.camelot.pricecenter.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;

/**
 * 
 * <p>Description: [优惠券]</p>
 * Created on 2015年12月15日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCartCouponService {
	
	/**
	 * 
	 * <p>Discription:[校验优惠券是否可用]</p>
	 * Created on 2015年12月29日
	 * @param uid 用户ID（必须）
	 * @param couponId 优惠券编号（必须）
	 * @param paymentMethod 企业支付或个人支付（1个人 2企业；非必须）
	 * @param shopCartDTO 购物车
	 * @param platformId 1:印刷家 2:绿印 3:小管家
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ShopCartDTO> validateCoupon(Long uid, String couponId, Integer paymentMethod, ShopCartDTO shopCartDTO, Integer platformId);

	/**
	 * 
	 * <p>Discription:[优惠券计算]</p>
	 * Created on 2015年12月17日
	 * @param shopCartDTO
	 * @param uid
	 * @param couponId 优惠券编号
	 * @param paymentMethod 个人支付或企业支付（1个人 2企业；非必须）
	 * @param platformId 1:印刷家 2:绿印 3:小管家
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ShopCartDTO> calcCoupon(ShopCartDTO shopCartDTO, Long uid, String couponId, Integer paymentMethod, Integer platformId);
	
}
