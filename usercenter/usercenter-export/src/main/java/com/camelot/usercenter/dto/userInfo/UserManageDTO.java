package com.camelot.usercenter.dto.userInfo;

import com.camelot.usercenter.enums.UserEnums;

import java.io.Serializable;

/**
 * 用户经营信息
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserManageDTO implements Serializable {

    private static final long serialVersionUID = 7334827194404049511L;
    private UserEnums.DealerType dealerType; //经营类型
    private String homePage ; //公司网站
    private String  saleNum ;// 最近一年销售额
    private String isHaveEbusiness ; //是否有同类电子商务经验 1 是 0 否
    private String webOperaPeo ; //网站运营人数
    private UserEnums.ERPType erpType ; //erp类型
    private String userManageStatus; //用户运营信息状态 0驳回/1待确认/2确认
    private String auditRemark; //审核备注 (驳回原因)

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getUserManageStatus() {
        return userManageStatus;
    }

    public void setUserManageStatus(String userManageStatus) {
        this.userManageStatus = userManageStatus;
    }

    public UserEnums.DealerType getDealerType() {
        return dealerType;
    }

    public void setDealerType(UserEnums.DealerType dealerType) {
        this.dealerType = dealerType;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getIsHaveEbusiness() {
        return isHaveEbusiness;
    }

    public void setIsHaveEbusiness(String isHaveEbusiness) {
        this.isHaveEbusiness = isHaveEbusiness;
    }

    public String getWebOperaPeo() {
        return webOperaPeo;
    }

    public void setWebOperaPeo(String webOperaPeo) {
        this.webOperaPeo = webOperaPeo;
    }

    public UserEnums.ERPType getErpType() {
        return erpType;
    }

    public void setErpType(UserEnums.ERPType erpType) {
        this.erpType = erpType;
    }
}
