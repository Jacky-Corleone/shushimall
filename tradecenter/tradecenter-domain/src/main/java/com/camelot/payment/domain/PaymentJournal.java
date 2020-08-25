package com.camelot.payment.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *  支付流水对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-2-27
 */
public class PaymentJournal implements Serializable {

	private static final long serialVersionUID = 2680214472092414591L;
	
	private Long id;
	private String outTradeNo;//对外交易号
	private String orderNo;//订单号
	private String type;//数据类型：1，支付请求；2，同步结果通知；3，异步结果通知
	private String details;//请求或结果的json串
	private Date createdTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
