package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;

import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums;

/**
 * 用户中信账户
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserCiticDTO implements Serializable {

    private static final long serialVersionUID = 8966840175082468226L;
    private java.lang.String sellerFrozenAccount;//  卖家冻结账户
    private java.lang.String sellerWithdrawsCashAccount;//  卖家收款账户
    private java.lang.String buyerPaysAccount;//  买家支付账户
    private java.lang.String buyerFinancingAccount;//  买家融资账户
    private java.lang.Integer accountState;//  账号状态 0驳回/   1待确认  /2确认   3卖家待审核
    private java.lang.String accountStateLabel;//  账号状态

    private String auditRemark; //审核备注 (驳回原因)

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public java.lang.String getSellerFrozenAccount() {
        return sellerFrozenAccount;
    }

    public void setSellerFrozenAccount(java.lang.String sellerFrozenAccount) {
        this.sellerFrozenAccount = sellerFrozenAccount;
    }

    public java.lang.String getSellerWithdrawsCashAccount() {
        return sellerWithdrawsCashAccount;
    }

    public void setSellerWithdrawsCashAccount(
            java.lang.String sellerWithdrawsCashAccount) {
        this.sellerWithdrawsCashAccount = sellerWithdrawsCashAccount;
    }

    public java.lang.String getBuyerPaysAccount() {
        return buyerPaysAccount;
    }

    public void setBuyerPaysAccount(java.lang.String buyerPaysAccount) {
        this.buyerPaysAccount = buyerPaysAccount;
    }

    public java.lang.String getBuyerFinancingAccount() {
        return buyerFinancingAccount;
    }

    public void setBuyerFinancingAccount(java.lang.String buyerFinancingAccount) {
        this.buyerFinancingAccount = buyerFinancingAccount;
    }

    public java.lang.Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(java.lang.Integer accountState) {
        if (accountState == 3) {
            this.accountStateLabel = "买家中信帐号审核通过";
        } else {
            UserEnums.UserAuditStatus comStatus = UserEnums.UserAuditStatus.getEnumByOrdinal(accountState);
            if (comStatus != null) {

                this.accountStateLabel = comStatus.getLabel().toString();
            }
        }
        this.accountState = accountState;
    }

    public java.lang.String getAccountStateLabel() {
        return accountStateLabel;
    }
}
