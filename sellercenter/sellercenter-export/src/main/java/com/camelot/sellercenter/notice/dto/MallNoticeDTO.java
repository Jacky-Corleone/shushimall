package com.camelot.sellercenter.notice.dto;

import java.io.Serializable;
import java.util.Date;

public class MallNoticeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long noticeId;

    private String noticeTitle;

    private String noticeContent;

    private Integer noticeStatus; //公告状态

    private Integer isRecommend; //是否置顶

    private Date created;

    private Date modified;

    private Integer sortNum;

    private Long platformId;
    private Integer noticeType;//公告类型 1 文字公告 2 链接公告
    private String url;//公告链接

    private Date createDtBegin;
    private Date createDtEnd;

    private Date publishDtBegin;
    private Date publishDtEnd;
    
    private Integer themeId;//主题id
    private Integer themeType;//主题类型：1、首页；2、类目

    public Date getCreateDtBegin() {
        return createDtBegin;
    }

    public void setCreateDtBegin(Date createDtBegin) {
        this.createDtBegin = createDtBegin;
    }

    public Date getCreateDtEnd() {
        return createDtEnd;
    }

    public void setCreateDtEnd(Date createDtEnd) {
        this.createDtEnd = createDtEnd;
    }

    public Date getPublishDtBegin() {
        return publishDtBegin;
    }

    public void setPublishDtBegin(Date publishDtBegin) {
        this.publishDtBegin = publishDtBegin;
    }

    public Date getPublishDtEnd() {
        return publishDtEnd;
    }

    public void setPublishDtEnd(Date publishDtEnd) {
        this.publishDtEnd = publishDtEnd;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}

	public Integer getThemeType() {
		return themeType;
	}

	public void setThemeType(Integer themeType) {
		this.themeType = themeType;
	}

}
