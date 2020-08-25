package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 企业报名集采信息
 * @author 周志军
 *
 */
public class QuerySignUpInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	//集采详情ID
	private Long activitesDetailsId;
	//采购单位
    private String enterpriseName;
	//联系人
    private String enterpriseLinkman;
    //联系人电话
    private String phoneNo;
	//联系地址
    private String address;
	//预期采购数量
    private Integer enterpriseEstimateNum;
	//预期采购价格
    private String enterpriseEstimatePrice;
    //活动编号
    private String activityNum;
    //活动名称
    private String activityName;
    public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getEnterpriseEstimateNum() {
		return enterpriseEstimateNum;
	}
	public void setEnterpriseEstimateNum(Integer enterpriseEstimateNum) {
		this.enterpriseEstimateNum = enterpriseEstimateNum;
	}
	public String getEnterpriseEstimatePrice() {
		return enterpriseEstimatePrice;
	}
	public void setEnterpriseEstimatePrice(String enterpriseEstimatePrice) {
		this.enterpriseEstimatePrice = enterpriseEstimatePrice;
	}
	public String getActivityNum() {
		return activityNum;
	}
	public void setActivityNum(String activityNum) {
		this.activityNum = activityNum;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}
	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}
	
}
