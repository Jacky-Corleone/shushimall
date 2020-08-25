package com.camelot.ecm.usercenter;

import com.camelot.common.ExcelField;
import com.camelot.usercenter.enums.UserEnums;

import java.util.Date;

/**
 * Created by sevelli on 15-4-14.
 */
public class UserQuery {
    private Long uid; // 用户ID
    private String uidString;

    private String uname; // 用户名
    private String umobile; // 电话
    private Integer usertype;// 用户类型 1 普通用户 2 买家  3卖家
//    private String usertypeString;

    private Long parentId;// 父账号ID
//    private String parentIdString;

    private String userEmail;// 邮箱
    private String nickname;// 用户昵称
    private Integer userstatus;// 当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    private String userstatusString;

    private Long shopId; // 店铺ID
    private String shopIdString;

    private String securityLevel;// 安全等级
    private String auditor;// 审核人
    private String auditRemark;// 审核备注
    private Integer quickType;// 快速注册
    private Date created;// 创建时间
    private Date updated;// 最后修改时间
    private Integer paymentStatus; // 缴费状态1待确认，2已确认
    private String paymentStatusString;

    private String companyName; // 公司名称
    private String companyAddr; // 公司地址
    private Integer auditStatus;// 用户审核状态 0驳回/1待审核/2审核通过/3暂不合作
    private String auditStatusString;
    private Date auditTime;// 审核时间
    private String auditorBegin; // 审核创建 开始时间
    private String auditorEnd; // 审核创建 结束时间

    private Integer isHavePayPassword; // 是否有支付密码 1有  0 无
    private String createTimeBegin; // 注册  开始 时间 范围
    private String createTimeEnd; //   注册  结束 时间范围

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getUid() {
        return uid;
    }
    @ExcelField(title = "商家编号",align = 2,sort = 10)
    public String getUidString(){
        return uid==null?"":uid.toString();
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    @ExcelField(title = "用户名",align = 2,sort = 20)
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
    @ExcelField(title = "用户手机号",align = 2,sort = 30)
    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public Integer getUsertype() {
        return usertype;
    }
    @ExcelField(title = "用户类型",align = 2,sort = 40)
    public String getUsertypeString(){
        return UserEnums.UserType.getEnumBycode(usertype).getLabel();
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    @ExcelField(title = "邮箱",align = 2,sort = 50)
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    @ExcelField(title = "昵称",align = 2,sort = 60)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserstatus() {
        return userstatus;
    }
    @ExcelField(title = "用户状态",align = 2,sort = 70)
    public String getUserstatusString(){
        return UserEnums.UserStatus.getEnumByOrdinal(userstatus).getLabel();
    }

    @ExcelField(title = "审核状态",align = 2,sort = 80)
    public String getUserAuditStatusLabel() {
        if(auditStatus!=null){
            if(userstatus!=null&&(userstatus==5||userstatus==3)&&auditStatus==2){
                //如果 用户审核状态为审核通过 用户状态未审核 以用户状态为主
                return UserEnums.UserAuditStatus.getEnumByOrdinal(1).getLabel();
            }else if(UserEnums.UserAuditStatus.getEnumByOrdinal(new Integer(auditStatus))!=null){
                return UserEnums.UserAuditStatus.getEnumByOrdinal(new Integer(auditStatus)).getLabel();
            }
        }
        if(userstatus!=null){
            if(userstatus==3){
                return UserEnums.UserAuditStatus.getEnumByOrdinal(1).getLabel();
            }else if(userstatus==4){
                return UserEnums.UserAuditStatus.getEnumByOrdinal(2).getLabel();
            }else if(userstatus==5){
                return UserEnums.UserAuditStatus.getEnumByOrdinal(1).getLabel();
            }else if(userstatus==6){
                return UserEnums.UserAuditStatus.getEnumByOrdinal(2).getLabel();
            }
        }
        return null;
    }
    public void setUserstatus(Integer userstatus) {
        this.userstatus = userstatus;
    }
    public Long getShopId() {
        return shopId;
    }

    @ExcelField(title = "店铺ID",align = 2,sort = 90)
    public String getShopIdString(){
        return shopId==null?"":shopId.toString();
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }
    @ExcelField(title = "审核人",align = 2,sort = 100)
    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Integer getQuickType() {
        return quickType;
    }

    public void setQuickType(Integer quickType) {
        this.quickType = quickType;
    }
    @ExcelField(title = "创建时间",align = 2,sort = 110)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    @ExcelField(title = "最后更新时间",align = 2,sort = 120)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    @ExcelField(title = "公司名称",align = 2,sort = 130)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @ExcelField(title = "公司地址",align = 2,sort = 140)
    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    @ExcelField(title = "审核时间",align = 2,sort = 140)
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditorBegin() {
        return auditorBegin;
    }

    public void setAuditorBegin(String auditorBegin) {
        this.auditorBegin = auditorBegin;
    }

    public String getAuditorEnd() {
        return auditorEnd;
    }

    public void setAuditorEnd(String auditorEnd) {
        this.auditorEnd = auditorEnd;
    }

    public Integer getIsHavePayPassword() {
        return isHavePayPassword;
    }

    public void setIsHavePayPassword(Integer isHavePayPassword) {
        this.isHavePayPassword = isHavePayPassword;
    }

    public void setUidString(String uidString) {
        this.uidString = uidString;
    }


    public void setUserstatusString(String userstatusString) {
        this.userstatusString = userstatusString;
    }

    public void setShopIdString(String shopIdString) {
        this.shopIdString = shopIdString;
    }

    public String getPaymentStatusString() {
        return paymentStatusString;
    }

    public void setPaymentStatusString(String paymentStatusString) {
        this.paymentStatusString = paymentStatusString;
    }

    public String getAuditStatusString() {
        return auditStatusString;
    }

    public void setAuditStatusString(String auditStatusString) {
        this.auditStatusString = auditStatusString;
    }

}
