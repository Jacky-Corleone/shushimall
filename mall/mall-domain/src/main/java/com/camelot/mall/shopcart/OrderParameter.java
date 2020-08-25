package com.camelot.mall.shopcart;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
/**
 * 
 * <p>Description: [提交订单所需的参数对象]</p>
 * Created on 2016年2月4日
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class OrderParameter implements Serializable{
	private static final long serialVersionUID = 501650676309422171L;
	private List<ProductInPriceDTO> products;//订单商品集合
	private String promptly;//是否立即购买
	private String shopPromoId;//满减活动id
	private String couponId;//优惠劵活动id
	private Integer integral;//积分
	private String ctoken;//购物车token
	private String uid;//用户id
	private TradeOrdersDTO dto;//订单对象
	private Map<Long, Integer> freightDeliveryType;//运费
	private Integer platformId;//平台ids
	private String promotion;//活动id
	private String markdown;//直降id
	private String areaCode;//区域编码
	
	public List<ProductInPriceDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductInPriceDTO> products) {
		this.products = products;
	}
	public String getPromptly() {
		return promptly;
	}
	public void setPromptly(String promptly) {
		this.promptly = promptly;
	}
	public String getShopPromoId() {
		return shopPromoId;
	}
	public void setShopPromoId(String shopPromoId) {
		this.shopPromoId = shopPromoId;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public String getCtoken() {
		return ctoken;
	}
	public void setCtoken(String ctoken) {
		this.ctoken = ctoken;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public TradeOrdersDTO getDto() {
		return dto;
	}
	public void setDto(TradeOrdersDTO dto) {
		this.dto = dto;
	}
	public Map<Long, Integer> getFreightDeliveryType() {
		return freightDeliveryType;
	}
	public void setFreightDeliveryType(Map<Long, Integer> freightDeliveryType) {
		this.freightDeliveryType = freightDeliveryType;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public String getMarkdown() {
		return markdown;
	}
	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
