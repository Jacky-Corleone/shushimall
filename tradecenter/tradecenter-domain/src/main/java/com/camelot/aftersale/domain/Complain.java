package com.camelot.aftersale.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 投诉
 *
 * @author
 */
public class Complain implements Serializable {

    private static final long serialVersionUID = -2147068697611207856L;
    private java.lang.Long id;//  id
    private java.lang.String orderId;//  订单号
    private java.lang.Long skuId;//  skuID
    private java.lang.Long refundId;//  退货ID
    private java.lang.Long buyerId;//  买家ID
    private java.lang.Long sellerId;//  卖家ID
    private java.lang.String shopName;//  卖家店铺名称
    private java.lang.Integer type;//  类型 1.退款申诉 2.申请售后
    private java.lang.String complainContent;//  投诉内容
    private java.lang.String complainPicUrl;//  投诉凭证
    private String complainResion;// 仲裁原因
    private java.lang.Integer status;// 状态 0.待仲裁 1.已仲裁
    private java.lang.String statusText;//  状态描述
    private java.util.Date resolutionTime;//  解决时间
    private java.util.Date created;//  创建时间
    private java.util.Date modified;//  修改时间
    private String createdBegin;//  创建开始时间
    private String createdEnd;//  创建结束时间
    private String comment;//  投诉说明
    private String complainPhone; //投诉人电话
    private String complainEmail;//投诉人邮箱
    private Long returnGoodsId; //退货单ID
    private String complainType; //投诉类型 1卖家 2 卖家
    private String complainTypeTotal; // 投诉类型 1投诉方   2双方
    private String statusSelect; //状态筛选    1:status<=2	（暂时废弃）
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

    public Long getReturnGoodsId() {
        return returnGoodsId;
    }

    public void setReturnGoodsId(Long returnGoodsId) {
        this.returnGoodsId = returnGoodsId;
    }

    public String getComplainResion() {
        return complainResion;
    }

    public void setComplainResion(String complainResion) {
        this.complainResion = complainResion;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getOrderId() {
        return orderId;
    }

    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }

    public java.lang.Long getSkuId() {
        return skuId;
    }

    public void setSkuId(java.lang.Long skuId) {
        this.skuId = skuId;
    }

    public java.lang.Long getRefundId() {
        return refundId;
    }

    public void setRefundId(java.lang.Long refundId) {
        this.refundId = refundId;
    }

    public java.lang.Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(java.lang.Long buyerId) {
        this.buyerId = buyerId;
    }

    public java.lang.Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(java.lang.Long sellerId) {
        this.sellerId = sellerId;
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

    public java.lang.String getComplainPicUrl() {
        return complainPicUrl;
    }

    public void setComplainPicUrl(java.lang.String complainPicUrl) {
        this.complainPicUrl = complainPicUrl;
    }

    public java.lang.Integer getStatus() {
        return status;
    }

    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.String getStatusText() {
        return statusText;
    }

    public void setStatusText(java.lang.String statusText) {
        this.statusText = statusText;
    }

    public java.util.Date getResolutionTime() {
        return resolutionTime;
    }

    public void setResolutionTime(java.util.Date resolutionTime) {
        this.resolutionTime = resolutionTime;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    public java.util.Date getModified() {
        return modified;
    }

    public void setModified(java.util.Date modified) {
        this.modified = modified;
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

	public List<Long> getBuyIdList() {
		return buyIdList;
	}

	public void setBuyIdList(List<Long> buyIdList) {
		this.buyIdList = buyIdList;
	}

}

