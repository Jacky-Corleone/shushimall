package com.camelot.usercenter.dto.audit;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class UserAuditLogDTO implements Serializable {
    /**
     * <p>Discription:[字段功能描述]</p>
     */
    private static final long serialVersionUID = 1636824973011241200L;
    private java.lang.Long id;//  id
    private java.lang.Long userId;//  用户ID
    private java.lang.Long auditId;//  审核记录ID
    private java.lang.Integer auditStatus;//  审核状态 :0:驳回1:待审核2:通过，
    private java.util.Date auditDate;//  审核时间
    private java.lang.String remark;//  审核描述
    private java.util.Date createDt;//  createDt
    private java.util.Date lastUpdDt;//  lastUpdDt
    private java.lang.String deletedFlag;//  删除标记  1 删除 0 未删除
    private java.lang.String auditLogType;//   日志类型 1 入驻审核  2 修改审核
    private int userType;//用户类型
    private String userName;
	private String nickName;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long value) {
        this.id = value;
    }

    public int getUserType() {
        return userType;
    }


    public void setUserType(int userType) {
        this.userType = userType;
    }

    public java.lang.Long getUserId() {
        return this.userId;
    }

    public void setUserId(java.lang.Long value) {
        this.userId = value;
    }

    public java.lang.Long getAuditId() {
        return this.auditId;
    }

    public void setAuditId(java.lang.Long value) {
        this.auditId = value;
    }

    public java.lang.Integer getAuditStatus() {
        return this.auditStatus;
    }

    public void setAuditStatus(java.lang.Integer value) {
        this.auditStatus = value;
    }

    public java.util.Date getAuditDate() {
        return this.auditDate;
    }

    public void setAuditDate(java.util.Date value) {
        this.auditDate = value;
    }

    public java.lang.String getRemark() {
        return this.remark;
    }

    public void setRemark(java.lang.String value) {
        this.remark = value;
    }

    public java.util.Date getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(java.util.Date value) {
        this.createDt = value;
    }

    public java.util.Date getLastUpdDt() {
        return this.lastUpdDt;
    }

    public void setLastUpdDt(java.util.Date value) {
        this.lastUpdDt = value;
    }

    public java.lang.String getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(java.lang.String value) {
        this.deletedFlag = value;
    }

    public java.lang.String getAuditLogType() {
        return this.auditLogType;
    }

    public void setAuditLogType(java.lang.String value) {
        this.auditLogType = value;
    }

	public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}


