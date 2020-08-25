package com.camelot.activity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/** 
 * <p>Description: [活动记录信息]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ActivityRecordDTO implements Serializable{

	private static final long serialVersionUID = -4994801549808777767L;
	//主键id
	private Long id;
	//订单id
	private String orderId;
	//店铺id
	private Long shopId;
	//活动类型:1:平台直降  2:平台满减 3:平台优惠卷  4:平台积分  5:店铺直降 6:店铺满减 7:店铺优惠卷,8:店铺改价优惠活动'
	private Integer type;
	//活动优惠金额
	private BigDecimal discountAmount;
	//活动id
	private String promotionId;
	//类型集合入参用
	private List<Integer> promotionTypes=new ArrayList<Integer>();
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public List<Integer> getPromotionTypes() {
		return promotionTypes;
	}
	public void setPromotionTypes(List<Integer> promotionTypes) {
		this.promotionTypes = promotionTypes;
	}
	
}
