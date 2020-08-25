package com.camelot.basecenter.domain;

import java.util.Date;

import com.camelot.openplatform.common.PropertyMapping;

public class MallClassify {
    @PropertyMapping("id")
    private Integer  id;//
    @PropertyMapping("title")
	private String title;//分类标题
    @PropertyMapping("status")
    private Integer status;//分类状态 1，已下架 2，已发布
    @PropertyMapping("type")
    private Integer type;//分类类型
    @PropertyMapping("created")
	private Date created;//创建时间
    @PropertyMapping("modified")
	private Date modified;//修改时间
    @PropertyMapping("platformId")
	private Integer platformId;//平台id
    @PropertyMapping("isDeleted")
	private Integer isDeleted;//分类是否删除 1，未删除 2，已删除
    @PropertyMapping("startTime")
	private Date startTime;//开始时间
    @PropertyMapping("endTime")
	private Date endTime;//结束时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
