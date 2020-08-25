package com.camelot.aftersale.dto.returngoods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class TradeReturnGoodsDTO implements Serializable {

    private static final long serialVersionUID = -2497059488040149936L;
    private Long id;//  id
    private Integer state;//  状态     1 退款申请等待卖家确认中  2 卖家不同意协议,等待买家修改 3 退款申请达成,等待买家发货   4 买家已退货,等待卖家确认收货  5 退款关闭  6 退款成功 7 退款中8.待平台处理退款 9平台处理退款中 10 退款失败  11 退款申请成功,等待同意退款
    private String codeNo;//  单据编号 (业务编号)
    private String orderId;//  订单ID
    private String orderName;// 订单名称
    private String orderStatus; // 订单状态
    private String buyId;//  买家ID
    private String returnResult;//  退货原因
    private String returnPic; //退货 退款 原因凭证
    private String remark;//  问题描述
    private String buyerName;//  买家姓名
    private String buyerAddress;//  买家联系地址
    private String buyerPhone;//  买家联系电话
    private String buyerPostcode;//  买家邮政编码
    private java.util.Date applyDt;//  申请时间
    private String auditorId;//  审核人ID
    private String auditorName;//  审核人名称
    private java.util.Date auditDt;//  审核时间
    private String auditRemark;//  审核备注
    private String expressNo;//  退货快递单号
    private String expressName;//  快递公司
    private BigDecimal orderPrice; //订单总金额
    private BigDecimal refundFreight;//  退款运费金额
    private BigDecimal refundGoods;//  退款货品总金额
    private String sellerId;//  卖家id
    private String returnAddress;//  退货地址
    private String returnPhone;//  退货联系电话
    private String returnPostcode;//  退货邮政编码
    private java.util.Date createdDt;//  创建时间
    private String createdBy;//  创建人
    private java.util.Date lastUpdDt;//  最后修改时间
    private String lastUpdBy;//  最后修改人
    private String deletedFlag;//  删除标记
    private String add1;//  add1
    private String add2;//  add2
    private String add3;//  add3

    private java.util.Date applyDtBegin; //申请开始时间
    private java.util.Date applyDtEnd; //申请结束时间
    private List<TradeReturnPicDTO> picDTOList; //退货原因图片
    private String isCustomerService; //是否售后 1 售后 0 退款
    private Date timeNode; // 定时任务时间节点
    
    private BigDecimal factorage;//第三方手续费
    
    private List<Long> buyIdList;
    
    private String confirmStatus;//平台确认状态；0:平台待确认   1：平台确认通过   2：平台处理中
    
    private List<String> confirmStatusList;
    
    private String orderPayBank;//支付方式
    
    private List<String> orderPayBankList;//支付方式集合
    
    private java.lang.Integer afterService;// 申请售后服务(1:无,2:进行中,3:完成 4：已关闭 5：部分商品已完成)  
    
    private Date refundTime;//平台确认退款时间
    
    private Date refundTimeBegin;//平台确认开始时间
    
    private Date refundTimeEnd;//平台确认结束时间
    
    private String passKey;//退货单号加密结果
    
    private String isService;//是否为纯服务订单 1为纯服务，2不是纯服务
    
    public String getIsCustomerService() {
        return isCustomerService;
    }

    public void setIsCustomerService(String isCustomerService) {
        this.isCustomerService = isCustomerService;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public Date getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(Date timeNode) {
        this.timeNode = timeNode;
    }

    public List<TradeReturnPicDTO> getPicDTOList() {
        return picDTOList;
    }

    public void setPicDTOList(List<TradeReturnPicDTO> picDTOList) {
        this.picDTOList = picDTOList;
    }

    public Date getApplyDtBegin() {
        return applyDtBegin;
    }

    public void setApplyDtBegin(Date applyDtBegin) {
        this.applyDtBegin = applyDtBegin;
    }

    public Date getApplyDtEnd() {
        return applyDtEnd;
    }

    public void setApplyDtEnd(Date applyDtEnd) {
        this.applyDtEnd = applyDtEnd;
    }

    public String getReturnPic() {
        return returnPic;
    }

    public void setReturnPic(String returnPic) {
        this.returnPic = returnPic;
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer value) {
        this.state = value;
    }

    public String getCodeNo() {
        return this.codeNo;
    }

    public void setCodeNo(String value) {
        this.codeNo = value;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String value) {
        this.orderId = value;
    }

    public String getBuyId() {
        return this.buyId;
    }

    public void setBuyId(String value) {
        this.buyId = value;
    }

    public String getReturnResult() {
        return this.returnResult;
    }

    public void setReturnResult(String value) {
        this.returnResult = value;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String value) {
        this.remark = value;
    }

    public String getBuyerName() {
        return this.buyerName;
    }

    public void setBuyerName(String value) {
        this.buyerName = value;
    }

    public String getBuyerAddress() {
        return this.buyerAddress;
    }

    public void setBuyerAddress(String value) {
        this.buyerAddress = value;
    }

    public String getBuyerPhone() {
        return this.buyerPhone;
    }

    public void setBuyerPhone(String value) {
        this.buyerPhone = value;
    }

    public String getBuyerPostcode() {
        return this.buyerPostcode;
    }

    public void setBuyerPostcode(String value) {
        this.buyerPostcode = value;
    }

    public java.util.Date getApplyDt() {
        return this.applyDt;
    }

    public void setApplyDt(java.util.Date value) {
        this.applyDt = value;
    }

    public String getAuditorId() {
        return this.auditorId;
    }

    public void setAuditorId(String value) {
        this.auditorId = value;
    }

    public String getAuditorName() {
        return this.auditorName;
    }

    public void setAuditorName(String value) {
        this.auditorName = value;
    }

    public java.util.Date getAuditDt() {
        return this.auditDt;
    }

    public void setAuditDt(java.util.Date value) {
        this.auditDt = value;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public void setAuditRemark(String value) {
        this.auditRemark = value;
    }

    public String getExpressNo() {
        return this.expressNo;
    }

    public void setExpressNo(String value) {
        this.expressNo = value;
    }

    public String getExpressName() {
        return this.expressName;
    }

    public void setExpressName(String value) {
        this.expressName = value;
    }

    public BigDecimal getRefundFreight() {
        return this.refundFreight;
    }

    public void setRefundFreight(BigDecimal value) {
        this.refundFreight = value;
    }

    public BigDecimal getRefundGoods() {
        return this.refundGoods;
    }

    public void setRefundGoods(BigDecimal value) {
        this.refundGoods = value;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String value) {
        this.sellerId = value;
    }

    public String getReturnAddress() {
        return this.returnAddress;
    }

    public void setReturnAddress(String value) {
        this.returnAddress = value;
    }

    public String getReturnPhone() {
        return this.returnPhone;
    }

    public void setReturnPhone(String value) {
        this.returnPhone = value;
    }

    public String getReturnPostcode() {
        return this.returnPostcode;
    }

    public void setReturnPostcode(String value) {
        this.returnPostcode = value;
    }

    public java.util.Date getCreatedDt() {
        return this.createdDt;
    }

    public void setCreatedDt(java.util.Date value) {
        this.createdDt = value;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    public java.util.Date getLastUpdDt() {
        return this.lastUpdDt;
    }

    public void setLastUpdDt(java.util.Date value) {
        this.lastUpdDt = value;
    }

    public String getLastUpdBy() {
        return this.lastUpdBy;
    }

    public void setLastUpdBy(String value) {
        this.lastUpdBy = value;
    }

    public String getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(String value) {
        this.deletedFlag = value;
    }

    public String getAdd1() {
        return this.add1;
    }

    public void setAdd1(String value) {
        this.add1 = value;
    }

    public String getAdd2() {
        return this.add2;
    }

    public void setAdd2(String value) {
        this.add2 = value;
    }

    public String getAdd3() {
        return this.add3;
    }

    public void setAdd3(String value) {
        this.add3 = value;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

	public BigDecimal getFactorage() {
		return factorage;
	}

	public void setFactorage(BigDecimal factorage) {
		this.factorage = factorage;
	}

	public List<Long> getBuyIdList() {
		return buyIdList;
	}

	public void setBuyIdList(List<Long> buyIdList) {
		this.buyIdList = buyIdList;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public List<String> getConfirmStatusList() {
		return confirmStatusList;
	}

	public void setConfirmStatusList(List<String> confirmStatusList) {
		this.confirmStatusList = confirmStatusList;
	}

	public String getOrderPayBank() {
		return orderPayBank;
	}

	public void setOrderPayBank(String orderPayBank) {
		this.orderPayBank = orderPayBank;
	}

	public List<String> getOrderPayBankList() {
		return orderPayBankList;
	}

	public void setOrderPayBankList(List<String> orderPayBankList) {
		this.orderPayBankList = orderPayBankList;
	}

	public java.lang.Integer getAfterService() {
		return afterService;
	}

	public void setAfterService(java.lang.Integer afterService) {
		this.afterService = afterService;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Date getRefundTimeBegin() {
		return refundTimeBegin;
	}

	public void setRefundTimeBegin(Date refundTimeBegin) {
		this.refundTimeBegin = refundTimeBegin;
	}

	public Date getRefundTimeEnd() {
		return refundTimeEnd;
	}

	public void setRefundTimeEnd(Date refundTimeEnd) {
		this.refundTimeEnd = refundTimeEnd;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public String getIsService() {
		return isService;
	}

	public void setIsService(String isService) {
		this.isService = isService;
	}
    
}

