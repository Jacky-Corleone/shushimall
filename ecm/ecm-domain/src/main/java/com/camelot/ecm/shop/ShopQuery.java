package com.camelot.ecm.shop;


import com.camelot.storecenter.dto.ShopDTO;

public class ShopQuery extends ShopDTO{
    private String sellerName;
    private Integer auditStatus;//审核状态，0待审核，1已驳回，2，3确认收费
    private String companyName;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
