package com.camelot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSiteMessageResult {
	// 买家id
	private Long buyerId;
	// 卖家id
	private Long sellerId;
	// 卖家id集合
	private Long[] sellerIds;
	// 买家id集合
	private Long[] buyerIds;
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
	// 协议名称
	private String agreementName;
	//询价名称
	private String inquiryName;
	//卖家名称
	private String sellerName;
	//买家名称
	private String userName;
	//询价接受卖家店铺与询价名称集合
	private List<HashMap<String, String>> quoteInquirys; 
	//询价批量发布消息替换询价名称集合
	private List<String> releaseInquirys; 
	//临时判断变量
	private String tmpJuge;
	//求购接受卖家集合
	private List<Map<String, Object>> askList; 
	//临时变量
	private String tmp;
	//优惠券名称
	private String couponName;
	// 退款/退货：1同意 2拒绝
	private Integer agreeOrReject;
	
	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public List<Map<String, Object>> getAskList() {
		return askList;
	}

	public void setAskList(List<Map<String, Object>> askList) {
		this.askList = askList;
	}

	public String getTmpJuge() {
		return tmpJuge;
	}

	public void setTmpJuge(String tmpJuge) {
		this.tmpJuge = tmpJuge;
	}

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

	public String getAgreementName() {
		return agreementName;
	}

	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}

	public String getInquiryName() {
		return inquiryName;
	}

	public void setInquiryName(String inquiryName) {
		this.inquiryName = inquiryName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public List<HashMap<String, String>> getQuoteInquirys() {
		return quoteInquirys;
	}

	public void setQuoteInquirys(List<HashMap<String, String>> quoteInquirys) {
		this.quoteInquirys = quoteInquirys;
	}

	public Long[] getSellerIds() {
		return sellerIds;
	}

	public void setSellerIds(Long[] sellerIds) {
		this.sellerIds = sellerIds;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getReleaseInquirys() {
		return releaseInquirys;
	}

	public void setReleaseInquirys(List<String> releaseInquirys) {
		this.releaseInquirys = releaseInquirys;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Long[] getBuyerIds() {
		return buyerIds;
	}

	public void setBuyerIds(Long[] buyerIds) {
		this.buyerIds = buyerIds;
	}

	public Integer getAgreeOrReject() {
		return agreeOrReject;
	}

	public void setAgreeOrReject(Integer agreeOrReject) {
		this.agreeOrReject = agreeOrReject;
	}
	
}
