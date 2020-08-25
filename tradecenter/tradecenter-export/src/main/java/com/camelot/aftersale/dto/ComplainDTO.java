package com.camelot.aftersale.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 投诉
 * 
 * @author
 */
public class ComplainDTO implements Serializable {

	private static final long serialVersionUID = 5316326774432973486L;
	private java.lang.Long id;// id
	private java.lang.String orderId;// 订单号
	private java.lang.Long skuId;// skuID
	private java.lang.Long refundId;// 退货ID
	private java.lang.Integer status;// 状态
	private java.lang.Long buyerId;// 买家ID
	private java.lang.Long sellerId;// 卖家ID
	private java.lang.String shopName;// 卖家店铺名称
	private java.lang.Integer type;// 类型 1.退款申诉 2.申请售后
	private java.lang.String complainContent;// 投诉内容
	private java.lang.String statusText;// 状态描述
	private java.util.Date created;// 创建时间
	private java.util.Date modified;// 修改时间
	private String comment; // 投诉说明
	private String createdBegin;// 创建开始时间
	private String createdEnd;// 创建结束时间
	private java.lang.String complainPicUrl;// 投诉凭证
	private java.util.Date resolutionTime;// 解决时间
	private String complainPhone; // 投诉人电话
	private String complainEmail;// 投诉人邮箱
	private Long returnGoodsId; // 退货单ID
	private String complainResion;//
	private String complainType; // 投诉类型 1买家 2 卖家
	private String complainTypeTotal; // 投诉类型 1投诉方 2双方
	private String statusSelect; // 状态筛选 1:status<=2 2:投诉页面用
	private java.lang.Long complainGroup; //仲裁分组
	private List<Long> buyIdList;
    
    public java.lang.Long getComplainGroup() {
		return complainGroup;
	}

	public void setComplainGroup(java.lang.Long complainGroup) {
		this.complainGroup = complainGroup;
	}

	public String getComplainTypeTotal() {
		return complainTypeTotal;
	}

	public void setComplainTypeTotal(String complainTypeTotal) {
		this.complainTypeTotal = complainTypeTotal;
	}

	public String getStatusSelect() {
		return statusSelect;
	}

	public void setStatusSelect(String statusSelect) {
		this.statusSelect = statusSelect;
	}

	public String getComplainType() {
		return complainType;
	}

	public void setComplainType(String complainType) {
		this.complainType = complainType;
	}

	public String getComplainResion() {
		return complainResion;
	}

	public void setComplainResion(String complainResion) {
		this.complainResion = complainResion;
	}

	public Long getReturnGoodsId() {
		return returnGoodsId;
	}

	public void setReturnGoodsId(Long returnGoodsId) {
		this.returnGoodsId = returnGoodsId;
	}

	public String getComplainPhone() {
		return complainPhone;
	}

	public void setComplainPhone(String complainPhone) {
		this.complainPhone = complainPhone;
	}

	public String getComplainEmail() {
		return complainEmail;
	}

	public void setComplainEmail(String complainEmail) {
		this.complainEmail = complainEmail;
	}

	public String getComplainPicUrl() {
		return complainPicUrl;
	}

	public void setComplainPicUrl(String complainPicUrl) {
		this.complainPicUrl = complainPicUrl;
	}

	public Date getResolutionTime() {
		return resolutionTime;
	}

	public void setResolutionTime(Date resolutionTime) {
		this.resolutionTime = resolutionTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.String getOrderId() {
		return orderId;
	}

	public void setOrderId(java.lang.String orderId) {
		this.orderId = orderId;
	}

	public java.lang.Long getSkuId() {
		return this.skuId;
	}

	public void setSkuId(java.lang.Long value) {
		this.skuId = value;
	}

	public java.lang.Long getRefundId() {
		return this.refundId;
	}

	public void setRefundId(java.lang.Long value) {
		this.refundId = value;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}

	public java.lang.Long getBuyerId() {
		return this.buyerId;
	}

	public void setBuyerId(java.lang.Long value) {
		this.buyerId = value;
	}

	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}

	public java.lang.String getStatusText() {
		return this.statusText;
	}

	public void setStatusText(java.lang.String value) {
		this.statusText = value;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

	public String getCreatedBegin() {
		return createdBegin;
	}

	public void setCreatedBegin(String createdBegin) {
		this.createdBegin = createdBegin;
	}

	public String getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = createdEnd;
	}

	public java.lang.String getShopName() {
		return shopName;
	}

	public void setShopName(java.lang.String shopName) {
		this.shopName = shopName;
	}

	public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}

	public java.lang.String getComplainContent() {
		return complainContent;
	}

	public void setComplainContent(java.lang.String complainContent) {
		this.complainContent = complainContent;
	}

	public List<Long> getBuyIdList() {
		return buyIdList;
	}

	public void setBuyIdList(List<Long> buyIdList) {
		this.buyIdList = buyIdList;
	}

}
