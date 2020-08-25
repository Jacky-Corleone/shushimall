package com.camelot.centralPurchasing.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * 集采活动
 * @author 周志军
 *
 */
public class CentralPurchasingEnterprise implements Serializable {

	private static final long serialVersionUID = 1L;

	//主键
    private Long enterpriseId;
	//采购单位
    private String enterpriseName;
	//联系人
    private String enterpriseLinkman;
	//手机号码
    private String phoneNo;
	//邮箱
    private String email;
	//联系地址
    private String address;
	//QQ/微信
    private String qqWx;
	//业务类型
    private String businessType;
	//插入时间
    private Date insertTime;
	//插入人
    private Long insertBy;
	//更新时间
    private Date updateTime;
	//更新人
    private Long updateBy;
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getEnterpriseLinkman() {
		return enterpriseLinkman;
	}
	public void setEnterpriseLinkman(String enterpriseLinkman) {
		this.enterpriseLinkman = enterpriseLinkman;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQqWx() {
		return qqWx;
	}
	public void setQqWx(String qqWx) {
		this.qqWx = qqWx;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Long getInsertBy() {
		return insertBy;
	}
	public void setInsertBy(Long insertBy) {
		this.insertBy = insertBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
}
