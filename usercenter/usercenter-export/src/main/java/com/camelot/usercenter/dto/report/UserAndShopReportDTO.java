package com.camelot.usercenter.dto.report;

import com.camelot.usercenter.dto.UserMallResourceDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *

 */
public class UserAndShopReportDTO implements Serializable {


    private static final long serialVersionUID = 3292457186202309957L;
    private Long authCustomerNum; //认证商家总数
    private Long activeCustomerNum;//激活得用户总数
    private Long buyerNum; //买家总数
    private Long sellerNum;//卖家总数
    private Long auditCustomer; //待审核商家数量
    private Long auditBuyerNum;//待审核买家数量
    private Long auditSellerNum; //待审核卖家数量
    private Date createDt; //创建时间

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Long getAuthCustomerNum() {
        return authCustomerNum;
    }

    public void setAuthCustomerNum(Long authCustomerNum) {
        this.authCustomerNum = authCustomerNum;
    }

    public Long getActiveCustomerNum() {
        return activeCustomerNum;
    }

    public void setActiveCustomerNum(Long activeCustomerNum) {
        this.activeCustomerNum = activeCustomerNum;
    }

    public Long getBuyerNum() {
        return buyerNum;
    }

    public void setBuyerNum(Long buyerNum) {
        this.buyerNum = buyerNum;
    }

    public Long getSellerNum() {
        return sellerNum;
    }

    public void setSellerNum(Long sellerNum) {
        this.sellerNum = sellerNum;
    }

    public Long getAuditCustomer() {
        return auditCustomer;
    }

    public void setAuditCustomer(Long auditCustomer) {
        this.auditCustomer = auditCustomer;
    }

    public Long getAuditBuyerNum() {
        return auditBuyerNum;
    }

    public void setAuditBuyerNum(Long auditBuyerNum) {
        this.auditBuyerNum = auditBuyerNum;
    }

    public Long getAuditSellerNum() {
        return auditSellerNum;
    }

    public void setAuditSellerNum(Long auditSellerNum) {
        this.auditSellerNum = auditSellerNum;
    }


}
