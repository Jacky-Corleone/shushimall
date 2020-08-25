package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;

import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums;

/**
 * 用户组织机构
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserOrganizationDTO implements Serializable {

    private static final long serialVersionUID = 5634863436752338146L;
    private java.lang.String organizationId;//  组织机构代码
    private java.lang.String organizationPicSrc;//  组织机构图片地址
    private java.lang.Integer organizationStatus;//   组织机构审核状态 0驳回/1待确认/2确认
    private String userfulTime; //组织机构代码有效期
    private java.lang.String organizationStatusLabel;//   组织机构审核状态 0驳回/1待确认/2确认
    private String auditRemark; //审核备注 (驳回原因)

    public String getUserfulTime() {
        return userfulTime;
    }

    public void setUserfulTime(String userfulTime) {
        this.userfulTime = userfulTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public java.lang.String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(java.lang.String organizationId) {
        this.organizationId = organizationId;
    }

    public java.lang.String getOrganizationPicSrc() {
        return organizationPicSrc;
    }

    public void setOrganizationPicSrc(java.lang.String organizationPicSrc) {
        this.organizationPicSrc = organizationPicSrc;
    }

    public java.lang.Integer getOrganizationStatus() {
        return organizationStatus;
    }

    public String getOrganizationStatusLabel() {
        return organizationStatusLabel;
    }

    public void setOrganizationStatus(java.lang.Integer organizationStatus) {

        UserEnums.UserAuditStatus comStatus = UserEnums.UserAuditStatus.getEnumByOrdinal(organizationStatus);
        if (comStatus != null) {
            this.organizationStatusLabel = comStatus.getLabel().toString();
        }

        this.organizationStatus = organizationStatus;
    }

}
