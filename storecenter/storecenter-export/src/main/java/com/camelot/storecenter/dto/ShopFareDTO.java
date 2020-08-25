package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
 * <p>Description: [店铺运费]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopFareDTO implements Serializable{

	private static final long serialVersionUID = -3849989838290003376L;
    
	private Long id;    
	private Long sellerId;  //卖家id
	private Long shopId;   //店铺id
	private String fareName;  //运费名称
	private String fareType;  //运送方式,多个以逗号相隔（1代表快递，2代表货运）
	private short paymentType;  //1:运费到付、2:包邮
    private String fareRegion;  //运送范围,多个以逗号相隔（0代表全国）
    private BigDecimal firstWeightPrice;  //首重价格
    private BigDecimal continueWeightPrice; //续重价格
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getFareName() {
		return fareName;
	}
	public void setFareName(String fareName) {
		this.fareName = fareName;
	}
	public String getFareType() {
		return fareType;
	}
	public void setFareType(String fareType) {
		this.fareType = fareType;
	}
	public short getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(short paymentType) {
		this.paymentType = paymentType;
	}
	public String getFareRegion() {
		return fareRegion;
	}
	public void setFareRegion(String fareRegion) {
		this.fareRegion = fareRegion;
	}
	public BigDecimal getFirstWeightPrice() {
		return firstWeightPrice;
	}
	public void setFirstWeightPrice(BigDecimal firstWeightPrice) {
		this.firstWeightPrice = firstWeightPrice;
	}
	public BigDecimal getContinueWeightPrice() {
		return continueWeightPrice;
	}
	public void setContinueWeightPrice(BigDecimal continueWeightPrice) {
		this.continueWeightPrice = continueWeightPrice;
	}
    
    
}
