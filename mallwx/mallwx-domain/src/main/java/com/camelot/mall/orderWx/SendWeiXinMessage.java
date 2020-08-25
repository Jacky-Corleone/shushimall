package com.camelot.mall.orderWx;

import java.io.Serializable;

/**
 * 发送微信消息时的模板信息
 * @author xujie
 *
 */
public class SendWeiXinMessage implements Serializable {

	private static final long serialVersionUID = -7879464876832997883L;
	
	private String openId,//微信的openId
				   modeId,//模板id
				   first,
				   keyword1,//店铺     用户id
				   keyword2,//下单时间   操作
				   keyword3,//商品
				   keyword4,//金额
				   keyword5,//金额
				   remark,
				   orderID,//订单号
				   orderMoneySum,//待付金额
				   orderProductPrice,//订单金额
				   orderProductName,//商品详情
				   orderAddress,//收货信息
				   orderName;//订单编号

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getModeId() {
		return modeId;
	}

	public void setModeId(String modeId) {
		this.modeId = modeId;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public String getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}

	public String getKeyword5() {
		return keyword5;
	}

	public void setKeyword5(String keyword5) {
		this.keyword5 = keyword5;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderProductPrice() {
		return orderProductPrice;
	}

	public void setOrderProductPrice(String orderProductPrice) {
		this.orderProductPrice = orderProductPrice;
	}

	public String getOrderProductName() {
		return orderProductName;
	}

	public void setOrderProductName(String orderProductName) {
		this.orderProductName = orderProductName;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderMoneySum() {
		return orderMoneySum;
	}

	public void setOrderMoneySum(String orderMoneySum) {
		this.orderMoneySum = orderMoneySum;
	}
}
