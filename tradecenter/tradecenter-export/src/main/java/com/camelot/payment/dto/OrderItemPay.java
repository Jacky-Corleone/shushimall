package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 *  支付子订单
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-17
 */
public class OrderItemPay  implements Serializable {
    
	private static final long serialVersionUID = -5572206567233104376L;
	private java.lang.String subOrderId;//  子订单号
	private java.lang.String subOrderSubject;//  子订单名称
	private BigDecimal subOrderPrice;//  价格
	private java.lang.String shopId;//  店铺ID
	private Long sellerId;//  用户ID
	private java.lang.Integer status;//  有效性 0[无效]/1[有效]
	private java.lang.String remark;//  备注
	private java.util.Date created;//  创建时间
	public java.lang.String getSubOrderId() {
		return subOrderId;
	}
	public void setSubOrderId(java.lang.String subOrderId) {
		this.subOrderId = subOrderId;
	}
	public java.lang.String getSubOrderSubject() {
		return subOrderSubject;
	}
	public void setSubOrderSubject(java.lang.String subOrderSubject) {
		this.subOrderSubject = subOrderSubject;
	}
	public BigDecimal getSubOrderPrice() {
		return subOrderPrice;
	}
	public void setSubOrderPrice(BigDecimal subOrderPrice) {
		this.subOrderPrice = subOrderPrice;
	}
	public java.lang.String getShopId() {
		return shopId;
	}
	public void setShopId(java.lang.String shopId) {
		this.shopId = shopId;
	}
	public java.lang.Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(java.lang.Long sellerId) {
		this.sellerId = sellerId;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date created) {
		this.created = created;
	}
}

