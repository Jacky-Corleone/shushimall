package com.camelot.util;

public class WebSiteMessageResult {
	// 买家id
	private Long buyerId;
	// 卖家id
	private Long sellerId;
	// 订单id
	private String orderId;
	// 商品id
	private Long goodsId;
	// 申请单id
	private Long applyId;
	// 日期
	private String date;
	// 处理结果
	private String backResult;
	// 申诉单id
	private Long backId;
	// 内容
	private String content;
	private String tempType;
	// 新手机
	private String phone;
	// 新邮箱
	private String mail;
	// 金额
	private String money;

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBackResult() {
		return backResult;
	}

	public void setBackResult(String backResult) {
		this.backResult = backResult;
	}

	public Long getBackId() {
		return backId;
	}

	public void setBackId(Long backId) {
		this.backId = backId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTempType() {
		return tempType;
	}

	public void setTempType(String tempType) {
		this.tempType = tempType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

}
