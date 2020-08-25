package com.camelot.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 
 * <p>Description: [店铺销售分析 报表类   出参dto]</p>
 * Created on 2015-7-17
 * @author  武超强
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

public class ItemSkuSellReportOut implements Serializable {

	private static final long serialVersionUID =1l;
	private static NumberFormat currency = NumberFormat.getNumberInstance(); //处理数字，显示为带逗号的货币形式
	
	//  销售额
	private BigDecimal sellPriceTotal; 
	private String sellPriceTotalStr;
	
	//  销售量
	private BigDecimal sellTotalNum; 
	private	String sellTotalNumStr;
	
	private String dealDate;	//日期
	
	public ItemSkuSellReportOut() {
	}
	
	public ItemSkuSellReportOut(String dealDate) {
		this.dealDate = dealDate;
		sellPriceTotal=new BigDecimal(0);
		sellTotalNum=new BigDecimal(0);
		sellPriceTotalStr="0";
		sellTotalNumStr="0";
	}
	
	public BigDecimal getSellPriceTotal() {
		return sellPriceTotal;
	}
	public void setSellPriceTotal(BigDecimal sellPriceTotal) {
		this.sellPriceTotal = sellPriceTotal;
		this.sellPriceTotalStr=currency.format(sellPriceTotal);
	}
	public BigDecimal getSellTotalNum() {
		return sellTotalNum;
	}
	public void setSellTotalNum(BigDecimal sellTotalNum) {
		this.sellTotalNum = sellTotalNum;
		this.sellTotalNumStr=currency.format(sellTotalNum);
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getSellPriceTotalStr() {
		return sellPriceTotalStr;
	}

	public void setSellPriceTotalStr(String sellPriceTotalStr) {
		this.sellPriceTotalStr = sellPriceTotalStr;
	}

	public String getSellTotalNumStr() {
		return sellTotalNumStr;
	}

	public void setSellTotalNumStr(String sellTotalNumStr) {
		this.sellTotalNumStr = sellTotalNumStr;
	}
	
}
