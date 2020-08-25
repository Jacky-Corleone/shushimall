package com.camelot.mall.shopcart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** 
 * <p>Description: 描述该类概要功能介绍</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 239720720234441284L;
	private String uid; // 当前登录用户ID
	private String ctoken; // 临时购物车Token
	private String regionId; // 区域编码
	private boolean checked = true;

	private Long sellerId; // 卖家ID
	private Long shopId; // 店铺ID
	private Long itemId; // 商品ID
	private Long skuId; // SKU ID
	private String title; // 商品名称
	private String src; // 商品图片
	private BigDecimal weight; // 重量
	private String weightUnit; // 重量单位
	private BigDecimal volume; // 体积

	private BigDecimal skuPrice; // 商品价格
	private Integer quantity; // 数量
	private BigDecimal total; // 总金额

	private List<ProductAttr> attrs; // 商品属性

	private BigDecimal payPrice; // 付款单价
	private BigDecimal payTotal; // 付款总额

	private Long promId; // 选中活动ID
	private Integer promType; // 活动类型
	private List<Promotion> promotions; // 商品营销活动

	private Long cid; // 商品类目ID
	private Integer qty; // 库存
	private Integer status; // 商品状态

	private Integer unusualState; // null: 正常商品或商品未选中； 1：商品已下架；2：不符合店铺规则, 3：库存不足, 4: 商品暂无报价！
	private List<String> unusualMsg = new ArrayList<String>(); // 商品异常状态描述
	private boolean hasPrice; // 是否有报价
    
    
    private int number;//协议下单购买总数量；
    private String contractType;//协议下单类型；
    private BigDecimal cost;//协议下单总金额；
    private String  contractNo;//协议订单编号

    

	private Long shopFreightTemplateId; // 运费模版ID
	private Integer deliveryType; // 运送方式：1快递，2EMS，3平邮
	//vip卡用到字段
	private BigDecimal discountTotal=BigDecimal.ZERO;//商品的vip卡的折扣金额（包括余额充足及余额不足，余额充足下discountTotal和originalDiscount值相等）
	
    private String ignoreName;//vip卡过滤商品的名字
    
    private BigDecimal originalDiscount=BigDecimal.ZERO;//余额充足的条件下折扣金额
	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCtoken() {
		return ctoken;
	}

	public void setCtoken(String ctoken) {
		this.ctoken = ctoken;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<ProductAttr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<ProductAttr> attrs) {
		this.attrs = attrs;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getPromId() {
		return promId;
	}

	public void setPromId(Long promId) {
		this.promId = promId;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public BigDecimal getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(BigDecimal payTotal) {
		this.payTotal = payTotal;
	}

	public Integer getPromType() {
		return promType;
	}

	public void setPromType(Integer promType) {
		this.promType = promType;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUnusualState() {
		return unusualState;
	}

	public void setUnusualState(Integer unusualState) {
		this.unusualState = unusualState;
	}

	public List<String> getUnusualMsg() {
		return unusualMsg;
	}

	public void setUnusualMsg(List<String> unusualMsg) {
		this.unusualMsg = unusualMsg;
	}

	public boolean isHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(boolean hasPrice) {
		this.hasPrice = hasPrice;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
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

	public BigDecimal getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(BigDecimal discountTotal) {
		this.discountTotal = discountTotal;
	}

	public String getIgnoreName() {
		return ignoreName;
	}

	public void setIgnoreName(String ignoreName) {
		this.ignoreName = ignoreName;
	}

	public BigDecimal getOriginalDiscount() {
		return originalDiscount;
	}

	public void setOriginalDiscount(BigDecimal originalDiscount) {
    }

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	
	
	
}
