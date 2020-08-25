package com.camelot.cmscenter.dto;

import java.io.Serializable;
import java.util.Date;

public class CmsSiteDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;	// 站点名称
	private String title;	// 站点标题
	private String logo;	// 站点logo
	private String description;// 描述，填写有助于搜索引擎优化
	private String keywords;// 关键字，填写有助于搜索引擎优化
	private String theme;	// 主题
	private String copyright;// 版权信息
	private String customIndexView;// 自定义首页视图文件
	private String path;
	
	private String remarks;	// 备注
	private String createBy;	// 创建者
	private Date createDate;// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getCustomIndexView() {
		return customIndexView;
	}
	public void setCustomIndexView(String customIndexView) {
		this.customIndexView = customIndexView;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
