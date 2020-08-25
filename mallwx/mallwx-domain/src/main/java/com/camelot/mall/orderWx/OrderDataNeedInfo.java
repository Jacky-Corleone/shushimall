package com.camelot.mall.orderWx;

import java.io.Serializable;
/**
 * 
* @ClassName: OrderDataNeedInfo
* @Description: 订单所需的页面数据
* @author dell
* @date 2015-6-10 - 下午3:21:10
* @version : 1.0
 */
public class OrderDataNeedInfo implements Serializable{

	private static final long serialVersionUID = -3010638653128509436L;

	private String address,//地址id
					invoiceType,//发票类型
					invoiceTitle;//发票抬头

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	
}
