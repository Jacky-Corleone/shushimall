package com.camelot.usercenter.dto.userrule;

import java.io.Serializable;

/**
 * 
 * <p>Description: [商家 平台规则中间表]</p>
 * Created on 2015-3-10
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserPlatformServiceRuleDTO implements Serializable {

	private static final long serialVersionUID = 1693689799095542916L;
	private java.lang.Long id;//  id
	private java.lang.Long userId;//  用户ID
	private java.lang.Long ruleId;//  平台规则ID
	private java.util.Date createTimeBegin; //认证开始时间 
	private java.util.Date createTimeEnd; //认证开始时间 
	private java.util.Date createTime;//  认证时间
	private java.util.Date modifyTime;//  modifyTime
	private Integer isDeleted;//  是否删除 1 删除 0 未删除
	private Long userCount; //商家数量
	private String companyName;// 公司名称
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public java.util.Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public java.util.Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Long getUserCount() {
		return userCount;
	}

	public void setUserCount(Long userCount) {
		this.userCount = userCount;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.Long getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}

	public java.lang.Long getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(java.lang.Long value) {
		this.ruleId = value;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(java.util.Date value) {
		this.modifyTime = value;
	}

	public Integer getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Integer value) {
		this.isDeleted = value;
	}
}
