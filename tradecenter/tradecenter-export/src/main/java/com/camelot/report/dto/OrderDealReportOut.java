package com.camelot.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 
 * <p>Description: [经营状况分析 报表类 出参dto]</p>
 * Created on 2015-7-15
 * @author  武超强
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

public class OrderDealReportOut implements Serializable {

	private static final long serialVersionUID =1l;
	private static NumberFormat currency = NumberFormat.getNumberInstance(); //处理数字，显示为带逗号的货币形式
	
	private BigDecimal payPriceTotal; //  成交金额
	private String payPriceTotalStr; //  成交金额
	
	private BigDecimal buyPersonNum; //  购买人数
	private String buyPersonNumStr; //  购买人数
	
	private BigDecimal payGoodsNum;//  成交商品数
	private String payGoodsNumStr;//  成交商品数
	
	private BigDecimal orderNum;//  下单数
	private String orderNumStr;//  下单数
	
	private BigDecimal payConversion;//  成交转化率
	private String payConversionStr;//  成交转化率
	
	private String dealDate;//  日期
	
	public OrderDealReportOut() {
	}
	
	public OrderDealReportOut(String dealDate) {
		this.dealDate = dealDate;
		payPriceTotal=new BigDecimal(0);
		buyPersonNum=new BigDecimal(0);
		payGoodsNum=new BigDecimal(0);
		orderNum=new BigDecimal(0);
		payConversion=new BigDecimal(0);
		
		payPriceTotalStr="0";
		buyPersonNumStr="0";
		payGoodsNumStr="0";
		orderNumStr="0";
		payConversionStr="0%";
	}

	public BigDecimal getPayPriceTotal() {
		return payPriceTotal;
	}

	public void setPayPriceTotal(BigDecimal payPriceTotal) {
		this.payPriceTotal = payPriceTotal;
		this.payPriceTotalStr =currency.format(payPriceTotal);
	}

	public BigDecimal getBuyPersonNum() {
		return buyPersonNum;
	}

	public void setBuyPersonNum(BigDecimal buyPersonNum) {
		this.buyPersonNum = buyPersonNum;
		this.buyPersonNumStr = currency.format(buyPersonNum);
	}

	public BigDecimal getPayGoodsNum() {
		return payGoodsNum;
	}

	public void setPayGoodsNum(BigDecimal payGoodsNum) {
		this.payGoodsNum = payGoodsNum;
		this.payGoodsNumStr = currency.format(payGoodsNum);
	}

	public BigDecimal getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(BigDecimal orderNum) {
		this.orderNum = orderNum;
		this.orderNumStr = currency.format(orderNum);
	}

	public BigDecimal getPayConversion() {
		return payConversion;
	}

	public void setPayConversion(BigDecimal payConversion) {
		this.payConversion = payConversion;
		if(payConversion.doubleValue()==0){
			this.payConversionStr="0%";
		}else{
			this.payConversionStr = payConversion.multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString()+"%";
		}
	}

	public String getPayConversionStr() {
		return payConversionStr;
	}

	public void setPayConversionStr(String payConversionStr) {
		this.payConversionStr = payConversionStr;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getPayPriceTotalStr() {
		return payPriceTotalStr;
	}

	public void setPayPriceTotalStr(String payPriceTotalStr) {
		this.payPriceTotalStr = payPriceTotalStr;
	}

	public String getBuyPersonNumStr() {
		return buyPersonNumStr;
	}

	public void setBuyPersonNumStr(String buyPersonNumStr) {
		this.buyPersonNumStr = buyPersonNumStr;
	}

	public String getPayGoodsNumStr() {
		return payGoodsNumStr;
	}

	public void setPayGoodsNumStr(String payGoodsNumStr) {
		this.payGoodsNumStr = payGoodsNumStr;
	}

	public String getOrderNumStr() {
		return orderNumStr;
	}

	public void setOrderNumStr(String orderNumStr) {
		this.orderNumStr = orderNumStr;
	}

}
