package com.camelot.ecm.settlement;

import com.camelot.common.ExcelField;

/**
 * Created by sevelli on 15-4-24.
 */
public class SettlementImportQuery {
    private String orderNo;//订单号
    private String orderAmount;//订单金额
    private String factorage;//订单手续费
    private String bizType;//业务类型
    private String orderTypeStirng;//订单类型
    private String orderStatsString;//订单状态
    private String backCount;//已退款金额
    private String liquidateStatusString;//清算状态
    private String completedTime;//交易时间
    private String callBankTime;//银行返回时间
    private String oraTradeNo;//原交易号
    private java.lang.String remark1;//备注1
    private java.lang.String remark2;//备注2

    @ExcelField(title = "订单号",type=0,sort=10)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    @ExcelField(title = "订单金额",type=0,sort=20)
    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
    @ExcelField(title = "订单手续费",type=0,sort=30)
    public String getFactorage() {
        return factorage;
    }

    public void setFactorage(String factorage) {
        this.factorage = factorage;
    }
    @ExcelField(title = "业务类型",type=0,sort=40)
    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
    @ExcelField(title = "订单类型",type=0,sort=50)
    public String getOrderTypeStirng() {
        return orderTypeStirng;
    }

    public void setOrderTypeStirng(String orderTypeStirng) {
        this.orderTypeStirng = orderTypeStirng;
    }
    @ExcelField(title = "订单状态",type=0,sort=60)
    public String getOrderStatsString() {
        return orderStatsString;
    }

    public void setOrderStatsString(String orderStatsString) {
        this.orderStatsString = orderStatsString;
    }
    @ExcelField(title = "清算状态",type=0,sort=70)
    public String getLiquidateStatusString() {
        return liquidateStatusString;
    }

    public void setLiquidateStatusString(String liquidateStatusString) {
        this.liquidateStatusString = liquidateStatusString;
    }
    @ExcelField(title = "交易时间",type=0,sort=80)
    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }
    @ExcelField(title = "银行返回时间",type=0,sort=90)
    public String getCallBankTime() {
        return callBankTime;
    }

    public void setCallBankTime(String callBankTime) {
        this.callBankTime = callBankTime;
    }
    @ExcelField(title = "备注1",type=0,sort=100)
    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    @ExcelField(title = "备注2",type=0,sort=110)
    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    @ExcelField(title = "已退款金额",type=0,sort=65)
    public String getBackCount() {
        return backCount;
    }

    public void setBackCount(String
                                     backCount) {
        this.backCount = backCount;
    }
    @ExcelField(title = "原交易号",type=0,sort=95)
    public String getOraTradeNo() {
        return oraTradeNo;
    }

    public void setOraTradeNo(String oraTradeNo) {
        this.oraTradeNo = oraTradeNo;
    }
}
