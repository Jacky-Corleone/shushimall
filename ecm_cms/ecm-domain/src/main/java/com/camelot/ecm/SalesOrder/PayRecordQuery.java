package com.camelot.ecm.SalesOrder;

import com.camelot.common.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sevelli on 15-4-29.
 */
public class PayRecordQuery {
    private java.lang.String orderId;//  订单id
    private String orderIdString;

    private java.lang.Long sellerId;//  卖家ID
    private String sellerIdString;

    private java.lang.Long buyerId;//  买家ID
    private String buyerIdString;

    private BigDecimal paymentPrice;//  实际支付金额
    private String paymentPriceString;

    private Integer state;//订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
    private String stateString;

    private Date paymentTime;//订单支付时间

    private Integer paymentType;//支付类型
    private String paymentTypeString;//

    public String getOrderId() {
        return orderId;
    }

	@ExcelField(title = "订单编号", align = 2, sort = 5)
	public String getOrderIdString() {
		return StringUtils.isBlank(orderId) ? "" : orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

    public Long getSellerId() {
        return sellerId;
    }

    @ExcelField(title="商家账号", align=2, sort=10)
    public String getSellerIdString(){
        return sellerId==null?"":sellerId.toString();
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    @ExcelField(title="买家账号", align=2, sort=20)
    public String getBuyerIdString(){
        return buyerId==null?"":buyerId.toString();
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getPaymentPrice() {
        return paymentPrice;
    }

    @ExcelField(title = "订单金额",align = 2,sort = 40)
    public String getPaymentPriceString(){
        return paymentPrice==null?"0.00":paymentPrice.toPlainString();
    }
    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public Integer getState() {
        return state;
    }

    @ExcelField(title="订单状态", align=2, sort=30)
    public String getStateString(){
        String stateString = "";
        switch (state){
            case 1:
                stateString = "待付款";
                break;
            case 2:
                stateString ="待配送";
                break;
            case 3:
                stateString ="待确认送货";
                break;
            case 4:
                stateString ="待评价";
                break;
            case 5:
                stateString ="已完成";
                break;
        }
        return stateString;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    @ExcelField(title="支付时间", align=2, sort=50)
    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public void setOrderIdString(String orderIdString) {
        this.orderIdString = orderIdString;
    }

    public void setSellerIdString(String sellerIdString) {
        this.sellerIdString = sellerIdString;
    }

    public void setBuyerIdString(String buyerIdString) {
        this.buyerIdString = buyerIdString;
    }


    public void setPaymentPriceString(String paymentPriceString) {
        this.paymentPriceString = paymentPriceString;
    }


    public void setStateString(String stateString) {
        this.stateString = stateString;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    @ExcelField(title="支付类型", align=2, sort=60)
    public String getPaymentTypeString() {
        switch (paymentType){
            case 1:
                paymentTypeString = "网银在线";
                break;
            case 2:
                paymentTypeString = "中信账户";
                break;
        }
        return paymentTypeString;
    }

    public void setPaymentTypeString(String paymentTypeString) {
        this.paymentTypeString = paymentTypeString;
    }
}
