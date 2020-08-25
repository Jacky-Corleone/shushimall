package com.camelot.tradecenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Description: [订单商品]</p>
 * Created on 2016年2月27日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class TradeOrderItems  implements Serializable {
    
	private static final long serialVersionUID =1l;
	private java.lang.Long id;//  主键
	private java.lang.String orderId;//  订单id
	private java.lang.Long itemId;//  SKU所属商品ID
	private java.lang.Long skuId;//  SKU
	private java.lang.String skuName;//  SKU名称
	private BigDecimal primitivePrice;//  原始价
	private java.lang.Long areaId;//  区域ID
	private java.lang.Integer num;//  数量
	private BigDecimal payPrice;//实际支付价格
	private BigDecimal payPriceTotal;//实际支付总价格
	private BigDecimal promotionDiscount;//  促销优惠金额
	private BigDecimal couponDiscount;//  优惠券优惠金额
	private BigDecimal integralDiscount;//  积分优惠金额
	private java.lang.Long promotionId;//  促销ID
	private Integer promotionType;//  促销类型(1直降，2满减)
	private java.util.Date createTime;//  创建时间
	private java.util.Date updateTime;//  更新时间
	private Long cid;//所属类目ID
	private Long shopFreightTemplateId; // 运费模版ID
	private Integer deliveryType; // 运送方式
	private Integer integral;//订单中的该商品使用的积分
	private String contractNo;//协议订单编号
	private BigDecimal pay ;//总金额
	private Integer number ;//总个数
	private Long activitesDetailsId; // 集采活动详情ID(如果该订单商品来自集采订单，该值不为空)
	private List<TradeOrderItemsPackage> tradeOrderItemsPackages = new ArrayList<TradeOrderItemsPackage>();//套装商品订单记录
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(java.lang.String value) {
		this.orderId = value;
	}
	
	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}
	
	public java.lang.Long getSkuId() {
		return this.skuId;
	}

	public void setSkuId(java.lang.Long value) {
		this.skuId = value;
	}
	
	public java.lang.String getSkuName() {
		return this.skuName;
	}

	public void setSkuName(java.lang.String value) {
		this.skuName = value;
	}
	
	public BigDecimal getPrimitivePrice() {
		return primitivePrice;
	}

	public void setPrimitivePrice(BigDecimal primitivePrice) {
		this.primitivePrice = primitivePrice;
	}

	public java.lang.Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(java.lang.Long value) {
		this.areaId = value;
	}
	
	public java.lang.Integer getNum() {
		return this.num;
	}

	public void setNum(java.lang.Integer value) {
		this.num = value;
	}
	
	
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	public java.lang.Long getPromotionId() {
		return this.promotionId;
	}

	public void setPromotionId(java.lang.Long value) {
		this.promotionId = value;
	}
	
	public Integer getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(Integer promotionType) {
		this.promotionType = promotionType;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public BigDecimal getPayPriceTotal() {
		return payPriceTotal;
	}

	public void setPayPriceTotal(BigDecimal payPriceTotal) {
		this.payPriceTotal = payPriceTotal;
	}

	public Long getShopFreightTemplateId() {
		return shopFreightTemplateId;
	}

	public void setShopFreightTemplateId(Long shopFreightTemplateId) {
		this.shopFreightTemplateId = shopFreightTemplateId;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}


	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public BigDecimal getIntegralDiscount() {
		return integralDiscount;
	}

	public void setIntegralDiscount(BigDecimal integralDiscount) {
		this.integralDiscount = integralDiscount;
	}

	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}

	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}

	public List<TradeOrderItemsPackage> getTradeOrderItemsPackages() {
		return tradeOrderItemsPackages;
	}

	public void setTradeOrderItemsPackages(List<TradeOrderItemsPackage> tradeOrderItemsPackages) {
		this.tradeOrderItemsPackages = tradeOrderItemsPackages;
	}
	
}

