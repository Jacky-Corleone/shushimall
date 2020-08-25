package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;
import java.util.List;

import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;

/**
 * 用户信息集合（含营业执照，组织代码，税务，账户，中信账户）
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private java.lang.Long extendId;//  extendId

    private java.lang.Long userId;//  用户id
    private UserType userType;//  用户类型

    private UserDTO userDTO;//  用户基本信息
    private UserBusinessDTO userBusinessDTO;// 用户营业执照
    private UserOrganizationDTO userOrganizationDTO;//用户组织机构
    private UserTaxDTO userTaxDTO;//用户税务
    private UserAccountDTO userAccountDTO;// 用户账户
    private UserCiticDTO userCiticDTO;// 用户中信账户
    private UserManageDTO userManageDTO;//用户运营信息

    private String isHaveSellerCashAccount;//  传入 状态 判断是否有卖家收款账户 1有 0无
    private String isHaveBuyerPaysAccount;//  传入状态  判断是否有买家付款账号  1有 0无
    private List<UserAuditLogDTO> userAuditLogList; //用户 入驻审核日志


    public List<UserAuditLogDTO> getUserAuditLogList() {
        return userAuditLogList;
    }

    public void setUserAuditLogList(List<UserAuditLogDTO> userAuditLogList) {
        this.userAuditLogList = userAuditLogList;
    }

    public String getIsHaveSellerCashAccount() {
        return isHaveSellerCashAccount;
    }

    public void setIsHaveSellerCashAccount(String isHaveSellerCashAccount) {
        this.isHaveSellerCashAccount = isHaveSellerCashAccount;
    }

    public String getIsHaveBuyerPaysAccount() {
        return isHaveBuyerPaysAccount;
    }

    public void setIsHaveBuyerPaysAccount(String isHaveBuyerPaysAccount) {
        this.isHaveBuyerPaysAccount = isHaveBuyerPaysAccount;
    }

    public UserManageDTO getUserManageDTO() {
        return userManageDTO;
    }

    public void setUserManageDTO(UserManageDTO userManageDTO) {
        this.userManageDTO = userManageDTO;
    }

    public java.lang.Long getExtendId() {
        return extendId;
    }

    public void setExtendId(java.lang.Long extendId) {
        this.extendId = extendId;
    }

    public java.lang.Long getUserId() {
        return userId;
    }

    public void setUserId(java.lang.Long userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserBusinessDTO getUserBusinessDTO() {
        return userBusinessDTO;
    }

    public void setUserBusinessDTO(UserBusinessDTO userBusinessDTO) {
        this.userBusinessDTO = userBusinessDTO;
    }

    public UserOrganizationDTO getUserOrganizationDTO() {
        return userOrganizationDTO;
    }

    public void setUserOrganizationDTO(UserOrganizationDTO userOrganizationDTO) {
        this.userOrganizationDTO = userOrganizationDTO;
    }

    public UserTaxDTO getUserTaxDTO() {
        return userTaxDTO;
    }

    public void setUserTaxDTO(UserTaxDTO userTaxDTO) {
        this.userTaxDTO = userTaxDTO;
    }

    public UserAccountDTO getUserAccountDTO() {
        return userAccountDTO;
    }

    public void setUserAccountDTO(UserAccountDTO userAccountDTO) {
        this.userAccountDTO = userAccountDTO;
    }

    public UserCiticDTO getUserCiticDTO() {
        return userCiticDTO;
    }

    public void setUserCiticDTO(UserCiticDTO userCiticDTO) {
        this.userCiticDTO = userCiticDTO;
    }

}
