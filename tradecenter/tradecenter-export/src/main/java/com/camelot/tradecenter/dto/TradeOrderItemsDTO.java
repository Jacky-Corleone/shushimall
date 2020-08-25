package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author
 * 
 */
public class TradeOrderItemsDTO implements Serializable {

	private static final long serialVersionUID = -8575624064024884736L;
	private Long orderItemId;//订单详情ID
	private String orderId;//  订单id
	private Long itemId;//  SKU所属品类ID
	private Long cid;//类目ID
	private Long skuId;//  SKU
	private String skuName;//  SKU名称
	private BigDecimal primitivePrice;//  原始价
	private BigDecimal payPrice;//实际支付价格
	private BigDecimal payPriceTotal;//实际支付总价格
	private Long areaId;//  区域ID
	private java.lang.Integer num;//  数量
	private BigDecimal promotionDiscount;//  促销优惠金额
	private BigDecimal couponDiscount;//  优惠券优惠金额
	private BigDecimal integralDiscount;// 积分优惠金额
	private Long promotionId;//  促销ID
	private java.lang.Integer promotionType;//  促销类型(1直降，2满减)
	private java.util.Date createTime;//  创建时间
	private java.util.Date updateTime;//  更新时间
	private Long shopFreightTemplateId; // 运费模版ID
	private Integer deliveryType; // 运送方式
	private String skuString;//
	private Integer integral;//该订单中商品使用的积分
	private String contractNo; // 询价或协议订单编号
	private BigDecimal pay ;//总金额
	private Integer number ;//总个数
	private Long activitesDetailsId; // 集采活动详情ID(如果该订单商品来自集采订单，该值不为空)
	private List<TradeOrderItemsPackageDTO> tradeOrderItemsPackages = new ArrayList<TradeOrderItemsPackageDTO>();//套装商品订单记录
	
	public String getSkuString() {
		return skuString;
	}
	public void setSkuString(String skuString) {
		this.skuString = skuString;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public BigDecimal getPrimitivePrice() {
		return primitivePrice;
	}
	public void setPrimitivePrice(BigDecimal primitivePrice) {
		this.primitivePrice = primitivePrice;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public java.lang.Integer getNum() {
		return num;
	}
	public void setNum(java.lang.Integer num) {
		this.num = num;
	}
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}
	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public java.lang.Integer getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(java.lang.Integer promotionType) {
		this.promotionType = promotionType;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
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
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public BigDecimal getPay() {
		return pay;
	}
	public void setPay(BigDecimal pay) {
		this.pay = pay;
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

	public List<TradeOrderItemsPackageDTO> getTradeOrderItemsPackages() {
		return tradeOrderItemsPackages;
	}

	public void setTradeOrderItemsPackages(List<TradeOrderItemsPackageDTO> tradeOrderItemsPackages) {
		this.tradeOrderItemsPackages = tradeOrderItemsPackages;
	}

}
