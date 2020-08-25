package com.camelot.ecm.SalesOrder;


import com.camelot.common.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sevelli on 15-4-10.
 */
public class TradeOrderQuery {
    private String num;
    private String id;
    private String parentOrderId;
    private String sellerName;
    private String buyerName;
    private String name;
    private String orderStace;
    private String buyerPj;
    private String sellerPj;
    private String qxflag;
    //private String shipMentType;
    private String paymentprice;
    private String zflx;
    private String promocode;
    private String ordertime;
    private String address;
    @ExcelField(title="序号", align=2, sort=10)
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    @ExcelField(title="订单编号", align=2, sort=20)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="主订单号", align=2, sort=30)
    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }
    @ExcelField(title="商家账号", align=2, sort=40)
    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    @ExcelField(title="买家账号", align=2, sort=50)
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    @ExcelField(title="收货人", align=2, sort=60)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @ExcelField(title="订单状态", align=2, sort=70)
    public String getOrderStace() {
        return orderStace;
    }

    public void setOrderStace(String orderStace) {
        this.orderStace = orderStace;
    }
    @ExcelField(title="买家评价", align=2, sort=80)
    public String getBuyerPj() {
        return buyerPj;
    }

    public void setBuyerPj(String buyerPj) {
        this.buyerPj = buyerPj;
    }
    @ExcelField(title="卖家评价", align=2, sort=90)
    public String getSellerPj() {
        return sellerPj;
    }

    public void setSellerPj(String sellerPj) {
        this.sellerPj = sellerPj;
    }
    @ExcelField(title="是否取消", align=2, sort=100)
    public String getQxflag() {
        return qxflag;
    }

    public void setQxflag(String qxflag) {
        this.qxflag = qxflag;
    }
/*    @ExcelField(title="配送方式", align=2, sort=110)
    public String getShipMentType() {
        return shipMentType;
    }

    public void setShipMentType(String shipMentType) {
        this.shipMentType = shipMentType;
    }*/
    @ExcelField(title="订单金额", align=2, sort=120)
    public String getPaymentprice() {
        return paymentprice;
    }

    public void setPaymentprice(String paymentprice) {
        this.paymentprice = paymentprice;
    }
    @ExcelField(title="付款形式", align=2, sort=130)
    public String getZflx() {
        return zflx;
    }
    public void setZflx(String zflx) {
        this.zflx = zflx;
    }
    @ExcelField(title="优惠码", align=2, sort=140)
    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
    @ExcelField(title="下单时间", align=2, sort=150)
    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }
    @ExcelField(title="配送地址", align=2, sort=160)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
