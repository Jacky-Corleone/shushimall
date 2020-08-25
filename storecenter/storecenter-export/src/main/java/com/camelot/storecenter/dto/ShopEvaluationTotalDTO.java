package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:店铺信誉统计DTO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopEvaluationTotalDTO implements Serializable {
	
	private static final long serialVersionUID = -5865680190161597185L;
	private Long shopId;//查询字段 店铺id
	private Double shopReputation;//店铺信誉
	private Double shopDescription; //描述相符
	private Map<Integer,Double> shopDescriptionDetails; //描述相符的评分详情 key取值:1 2 3 4 5  value:对应的百分比
	private Double shopService;//服务
	private Map<Integer,Double> shopServiceDetails; //描述相符的评分详情 key取值:1 2 3 4 5  value:对应的百分比
	private Double shopArrival;//到货速度
	private Map<Integer,Double> shopArrivalDetails; //描述相符的评分详情 key取值:1 2 3 4 5  value:对应的百分比
	private BigInteger allCount; //店铺评价总数量
	private BigInteger allSum;//店铺评价总分数
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Double getShopReputation() {
		return shopReputation;
	}
	public void setShopReputation(Double shopReputation) {
		this.shopReputation = shopReputation;
	}
	public Double getShopDescription() {
		return shopDescription;
	}
	public void setShopDescription(Double shopDescription) {
		this.shopDescription = shopDescription;
	}
	public Map<Integer, Double> getShopDescriptionDetails() {
		return shopDescriptionDetails;
	}
	public void setShopDescriptionDetails(
			Map<Integer, Double> shopDescriptionDetails) {
		this.shopDescriptionDetails = shopDescriptionDetails;
	}
	public Double getShopService() {
		return shopService;
	}
	public void setShopService(Double shopService) {
		this.shopService = shopService;
	}
	public Map<Integer, Double> getShopServiceDetails() {
		return shopServiceDetails;
	}
	public void setShopServiceDetails(Map<Integer, Double> shopServiceDetails) {
		this.shopServiceDetails = shopServiceDetails;
	}
	public Double getShopArrival() {
		return shopArrival;
	}
	public void setShopArrival(Double shopArrival) {
		this.shopArrival = shopArrival;
	}
	public Map<Integer, Double> getShopArrivalDetails() {
		return shopArrivalDetails;
	}
	public void setShopArrivalDetails(Map<Integer, Double> shopArrivalDetails) {
		this.shopArrivalDetails = shopArrivalDetails;
	}
	public BigInteger getAllCount() {
		return allCount;
	}
	public void setAllCount(BigInteger allCount) {
		this.allCount = allCount;
	}
	public BigInteger getAllSum() {
		return allSum;
	}
	public void setAllSum(BigInteger allSum) {
		this.allSum = allSum;
	}
	
	
}
