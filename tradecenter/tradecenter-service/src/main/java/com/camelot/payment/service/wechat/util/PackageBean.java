package com.camelot.payment.service.wechat.util;

/**
 *  
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2014-11-7
 */
public class PackageBean {
	
	private String attach; // 附加数据，原样返回 （128）
	private String bankType="WX"; // *银行通道类型 固定为WX
	private String body; // *商品描述（128）
	private String feeType="1";// *支付币种。默认人民币
	private String inputCharset="UTF-8";// *字符编码 'GBK','UTF-8',默认'GBK'
	private String goodsTag;// 商品标记
	private String notifyurl;// *支付完成后接受微信通知支付结果的URL(255)
	private String outTradeNo; // *商户系统内部的，确保商户系统唯一（32）
	private String partner; // *注册时分配的财付通商户号
	private String productFee;// 商品费用，单位分。如果有值，必须保证 transortFee+productFee=tot
	private String spbillCreateIp;// * 用户浏览器端IP(15)
	private String timeExpire;// 订单失效时间
	private String timeStart; // 订单生产时间
	private String totalFee; // *订单总金额，单位为分
	private String transortFee;// 物流费用，单位分。如果有值，必须保证 transortFee+productFee=totalFee
	
	private String partnerKey;// 
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeExpire() {
		return timeExpire;
	}
	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}
	public String getTransortFee() {
		return transortFee;
	}
	public void setTransortFee(String transortFee) {
		this.transortFee = transortFee;
	}
	public String getProductFee() {
		return productFee;
	}
	public void setProductFee(String productFee) {
		this.productFee = productFee;
	}
	public String getGoodsTag() {
		return goodsTag;
	}
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}
	public String getBankType() {
		return bankType;
	}
	public String getFeeType() {
		return feeType;
	}
	public String getInputCharset() {
		return inputCharset;
	}
	public String getPartnerKey() {
		return partnerKey;
	}
	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}
	
	
}
