package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台服务规则表
 */
public class PlatformServiceRuleDTO implements Serializable {

    private static final long serialVersionUID = 23621204996978631L;
    private long ruleId;//服务规则id
    private long platformId;//平台id
    private String ruleName;//服务规则名称
    private String iconImageSrc;//icon图片地址
    private String simpleIntro;//规则简介
    private String details;//详情描述
    private Date createTime;//规则创建时间
    private Date updateTime;//规则更新时间
    private String createUserId;//规则创建人id
    private long helpDocId;//帮助文档id
    private int status;//状态，0：被删除

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getIconImageSrc() {
        return iconImageSrc;
    }

    public void setIconImageSrc(String iconImageSrc) {
        this.iconImageSrc = iconImageSrc;
    }

    public String getSimpleIntro() {
        return simpleIntro;
    }

    public void setSimpleIntro(String simpleIntro) {
        this.simpleIntro = simpleIntro;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public long getHelpDocId() {
        return helpDocId;
    }

    public void setHelpDocId(long helpDocId) {
        this.helpDocId = helpDocId;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
