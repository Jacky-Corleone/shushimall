package com.camelot.usercenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserEnums.UserAuditStatus;

/**
 * <p>
 * Description: [用户基本信息DTO]
 * </p>
 * Created on 2015年2月3日
 *
 * @author <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserDTO implements Serializable {


    private static final long serialVersionUID = 7146819742471999697L;
    private Long uid; // 用户ID
    private String uname; // 用户名
    private String umobile; // 电话
    private Integer usertype;// 用户类型 1 普通用户 2 买家  3卖家
    private Long parentId;// 父账号ID
    private String userEmail;// 邮箱
    private String password; //密码
    private String nickname;// 用户昵称
    private Integer userstatus;// 当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    private Long shopId; // 店铺ID
    private String securityLevel;// 安全等级
    private String auditor;// 审核人
    private String auditRemark;// 审核备注
    private Integer quickType;// 快速注册
    private Date created;// 创建时间
    private Date updated;// 最后修改时间
    private Integer paymentStatus; // 缴费状态1待确认，2已确认
    private Integer deleted; //是否可用状态 1 不可用 0 可用(冻结状态1 未冻结0)
    private String companyName; // 公司名称
    private String companyAddr; // 公司地址
    private Integer auditStatus;// 用户审核状态 0驳回/1待审核/2审核通过/3暂不合作
    private Date auditTime;// 审核时间
    private String auditorBegin; // 审核创建 开始时间
    private String auditorEnd; // 审核创建 结束时间
   
    private Long[] shopIds; // 店铺Id组
    private Integer isHavePayPassword; // 是否有支付密码 1有  0 无
    private String createTimeBegin; // 注册  开始 时间 范围
    private String createTimeEnd; //   注册  结束 时间范围
    private List<String> idList;
    private List<Long> uidList;
    private List<Integer> userStatusList; //店铺状态组

    private String linkMan; //联系人
    private UserEnums.DepartMent department;//所属部门
    private String linkPhoneNum; //固定电话
    private Integer platformId;// 平台ID
    private BigDecimal growthValue;//用户成长值
    private Long totalIntegral; // 总积分
    
    private String vipLevel;//1-普通会员：<3000 成长值  2-铜牌会员：>=3000 成长值   3-银牌会员：>=10000 成长值   4-金牌会员：>=100000 成长值  5-钻石会员：>=300000 成长值
    
    public String getLinkMan() {
        return linkMan;
    }

	public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public UserEnums.DepartMent getDepartment() {
        return department;
    }

    public void setDepartment(UserEnums.DepartMent department) {
        this.department = department;
    }

    public String getLinkPhoneNum() {
        return linkPhoneNum;
    }

    public void setLinkPhoneNum(String linkPhoneNum) {
        this.linkPhoneNum = linkPhoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

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

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public Integer getUsertype() {
        return usertype;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserstatus() {
        return userstatus;
    }

    public String getUserAuditStatusLabel() {
        if (auditStatus != null) {
            if (userstatus != null && (userstatus == 5 || userstatus == 3) && auditStatus == 2) {
                //如果 用户审核状态为审核通过 用户状态未审核 以用户状态为主
                return UserAuditStatus.getEnumByOrdinal(1).getLabel();
            } else if (UserAuditStatus.getEnumByOrdinal(auditStatus) != null) {
                return UserAuditStatus.getEnumByOrdinal(auditStatus).getLabel();
            }
        }
        if (userstatus != null) {
            if (userstatus == 3) {
                return UserAuditStatus.getEnumByOrdinal(1).getLabel();
            } else if (userstatus == 4) {
                return UserAuditStatus.getEnumByOrdinal(2).getLabel();
            } else if (userstatus == 5) {
                return UserAuditStatus.getEnumByOrdinal(1).getLabel();
            } else if (userstatus == 6) {
                return UserAuditStatus.getEnumByOrdinal(2).getLabel();
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

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

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

    public Long[] getShopIds() {
        return shopIds;
    }

    public void setShopIds(Long[] shopIds) {
        this.shopIds = shopIds;
    }

    public Integer getIsHavePayPassword() {
        return isHavePayPassword;
    }

    public void setIsHavePayPassword(Integer isHavePayPassword) {
        this.isHavePayPassword = isHavePayPassword;
    }

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public List<Long> getUidList() {
		return uidList;
	}

	public void setUidList(List<Long> uidList) {
		this.uidList = uidList;
	}

	public List<Integer> getUserStatusList() {
		return userStatusList;
	}

	public void setUserStatusList(List<Integer> userStatusList) {
		this.userStatusList = userStatusList;
	}

	public BigDecimal getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(BigDecimal growthValue) {
		this.growthValue = growthValue;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Long getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(Long totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	
}
