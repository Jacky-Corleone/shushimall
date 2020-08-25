package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;

import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums;

/**
 * 用户税务
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserTaxDTO implements Serializable {

    private static final long serialVersionUID = -2927249287642057646L;
    private java.lang.String taxManId;//  税务人识别号
    private java.lang.String taxpayerType;//  纳税人类型
    private java.lang.String taxpayerCode;//  税务人类型税码
    private java.lang.String taxRegistrationCertificatePicSrc;//  税务登记证电子版图片地址
    private java.lang.String taxpayerCertificatePicSrc;//  纳税人资格证电子版图片地址
    private java.lang.Integer taxStatus;//  税务审核状态  0驳回/1待确认/2确认
    private java.lang.String taxStatusLabel;//  税务审核状态  0驳回/1待确认/2确认

    private UserEnums.TaxType taxType;//纳税类型 纳税类型税码
    private String auditRemark; //审核备注 (驳回原因)


    public UserEnums.TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(UserEnums.TaxType taxType) {
        this.taxType = taxType;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public java.lang.String getTaxManId() {
        return taxManId;
    }

    public void setTaxManId(java.lang.String taxManId) {
        this.taxManId = taxManId;
    }

    public java.lang.String getTaxpayerType() {
        return taxpayerType;
    }

    public void setTaxpayerType(java.lang.String taxpayerType) {
        this.taxpayerType = taxpayerType;
    }

    public java.lang.String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(java.lang.String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public java.lang.String getTaxRegistrationCertificatePicSrc() {
        return taxRegistrationCertificatePicSrc;
    }

    public void setTaxRegistrationCertificatePicSrc(
            java.lang.String taxRegistrationCertificatePicSrc) {
        this.taxRegistrationCertificatePicSrc = taxRegistrationCertificatePicSrc;
    }

    public java.lang.String getTaxpayerCertificatePicSrc() {
        return taxpayerCertificatePicSrc;
    }

    public void setTaxpayerCertificatePicSrc(
            java.lang.String taxpayerCertificatePicSrc) {
        this.taxpayerCertificatePicSrc = taxpayerCertificatePicSrc;
    }

    public java.lang.Integer getTaxStatus() {
        return taxStatus;
    }

    public String getTaxStatusLabel() {
        return taxStatusLabel;
    }

    public void setTaxStatus(java.lang.Integer taxStatus) {

        UserEnums.UserAuditStatus comStatus = UserEnums.UserAuditStatus.getEnumByOrdinal(taxStatus);
        if (comStatus != null) {
            this.taxStatusLabel = comStatus.getLabel().toString();
        }
        this.taxStatus = taxStatus;
    }

}
