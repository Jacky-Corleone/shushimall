package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.PayTypeEnum;

/**
 *  支付接收参数对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-2-27
 */
public class PayReqParam implements Serializable {

	private static final long serialVersionUID = -2425498182827442262L;

	private String system;// 调用系统编码
	private String sign;// 签名
	
	private String outTradeNo;// 对外交易号
	
	private String orderNo;// 订单号
	private String subject;// 商品名称
	private PayOrderTypeEnum payOrderType=PayOrderTypeEnum.Parent;// 支付订单类型，默认父级订单
	private String orderParentNo;// 父级订单号,当支付订单类型为子单时，该项为必填（payIndex）
	private BigDecimal totalFee;// 交易金额 单位：0.01元
	private BigDecimal reFundFee;// 交易金额 单位：0.01元
	private String orderDetails;// 订单信息
	private String deliveryType;// 配送方式
	private String discountAmount;	//折扣金额
	private String bankDiscount;	//推荐支付的折扣
	private PayTypeEnum payType=PayTypeEnum.PAY_ONLINE;// 购物车勾选支付方式 在线支付（默认）， 货到付款
	
	private PayBankEnum payBank;// 支付渠道  支付宝、网银等等
	private Long sellerId;// 卖家Id
	private String seller;// 卖家名称
	private String toAccount;// 收款账号
	private String buyer;// 买家名称
	private String fromAccount;// 付款账号
	
	private String curType;// 币种
	private String extraParam;// 额外参数
	private String defaultBank;// 默认银行
	private String openid;
//	private String customIp;// 客户端IP
//	private Date payDate;// 支付时间
	private String qrPayMode;// 二维码模式 0:支付宝 3:微信,默认为2
	
	private Integer platformId;//平台ID，null为科印，2为绿印

	private List<OrderItemPay> orderItemPays;// 子订单信息
	private String customIp;//订单生成的机器 IP

	public String getCustomIp() {
		return customIp;
	}

	public void setCustomIp(String customIp) {
		this.customIp = customIp;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public PayOrderTypeEnum getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(PayOrderTypeEnum payOrderType) {
		this.payOrderType = payOrderType;
	}
	public String getOrderParentNo() {
		return orderParentNo;
	}

	public void setOrderParentNo(String orderParentNo) {
		this.orderParentNo = orderParentNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getBankDiscount() {
		return bankDiscount;
	}

	public void setBankDiscount(String bankDiscount) {
		this.bankDiscount = bankDiscount;
	}

	public PayTypeEnum getPayType() {
		return payType;
	}

	public void setPayType(PayTypeEnum payType) {
		this.payType = payType;
	}

	public PayBankEnum getPayBank() {
		return payBank;
	}

	public void setPayBank(PayBankEnum payBank) {
		this.payBank = payBank;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}

	public List<OrderItemPay> getOrderItemPays() {
		return orderItemPays;
	}

	public void setOrderItemPays(List<OrderItemPay> orderItemPays) {
		this.orderItemPays = orderItemPays;
	}

	public String getDefaultBank() {
		return defaultBank;
	}

	public void setDefaultBank(String defaultBank) {
		this.defaultBank = defaultBank;
	}

	public String getQrPayMode() {
		return qrPayMode;
	}

	public void setQrPayMode(String qrPayMode) {
		this.qrPayMode = qrPayMode;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public BigDecimal getReFundFee() {
		return reFundFee;
	}

	public void setReFundFee(BigDecimal reFundFee) {
		this.reFundFee = reFundFee;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
