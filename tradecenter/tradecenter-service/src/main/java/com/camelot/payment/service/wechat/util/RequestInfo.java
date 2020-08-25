package com.camelot.payment.service.wechat.util;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信订单信息(发送)
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2014-11-18
 */
@XStreamAlias("xml")
public class RequestInfo {

	private String appid;// *微信分配的公众账号 ID
	private String mch_id;// *微信支付分配的商户号
	private String device_info;// 微信支付分配的终端设备号
	private String nonce_str;// * 随机字符
	private String sign;// * 签名
	private String body;// *描述
	private String attach;// 附加数据
	private String out_trade_no;// *商户订单号
	private String total_fee;// *订单总金额，单位为分
	private String spbill_create_ip;// *订单生成的机器 IP
	private String time_start;// 交易起始时间
	private String time_expire;// 交易结束时间
	private String goods_tag;// 商品标记
	private String notify_url;// *接收微信支付成功通知 通知地址
	private String trade_type;// *交易类型 JSAPI、NATIVE、APP
	private String openid;// 用户在商户 appid 下的唯一 trade_type 为 JSAPI 时，此参数必传
	private String product_id;// 只在 trade_type 为 NATIVE时需要填写。此 id 为二维码中包含的商品 ID，商户自行 维护。
	private String out_refund_no;// 退款单据号
	private String op_user_id;//操作员帐号, 默认为商户号
	private String refund_fee;// 退款金额
	private String refund_fee_type;// 货币种类
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
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
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getOp_user_id() {
		return op_user_id;
	}
	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getRefund_fee_type() {
		return refund_fee_type;
	}
	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}

}
