package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * 
 * @author 周立明
 *
 */
public class MallDocumentDTO implements Serializable{
	private static final long serialVersionUID = 2990627565835351070L;
	@PropertyMapping("id") 
	private Integer mallId;//
	@PropertyMapping("classifyId")
	private Integer mallClassifyId;//文档分类Id
	private String mallClassifyTitle;//文档分类标题
	private Integer mallType;//文档分类类型
	@PropertyMapping("title")
	private String mallTitle;//帮助文档标题
	@PropertyMapping("status")
	private Integer mallStatus;//帮助文档状态 1，下架 ，2发布
	@PropertyMapping("contentUrl")
	private String mallContentUrl;//内容jfs地址
	private Date mallCreated;//创建时间
	private Date mallBeginCreated;//开始的创建时间
	private Date mallEndCreated;//结束的创建时间
	private Date mallModified;//修改时间
	private Integer mallIsDeleted;//分类是否删除 0，未删除 , 1 已删除
	@PropertyMapping("sortNum")
	private Integer mallSortNum;//排序号
	private Date mallStartTime;//发布时间
	private Date mallBeginStartTime;//开始的发布时间
	private Date mallEndStartTime;//结束的发布时间
	private Date mallEndTime;//结束时间
	private Integer mallPlatformId;//平台ID
	private String mallClassfyTitle;//帮助文档分类标题
	
	public String getMallClassfyTitle() {
		return mallClassfyTitle;
	}
	public void setMallClassfyTitle(String mallClassfyTitle) {
		this.mallClassfyTitle = mallClassfyTitle;
	}
	public Date getMallCreated() {
		return mallCreated;
	}
	public void setMallCreated(Date mallCreated) {
		this.mallCreated = mallCreated;
	}
	public Date getMallBeginCreated() {
		return mallBeginCreated;
	}
	public void setMallBeginCreated(Date mallBeginCreated) {
		this.mallBeginCreated = mallBeginCreated;
	}
	public Date getMallEndCreated() {
		return mallEndCreated;
	}
	public void setMallEndCreated(Date mallEndCreated) {
		this.mallEndCreated = mallEndCreated;
	}
	public Date getMallModified() {
		return mallModified;
	}
	public void setMallModified(Date mallModified) {
		this.mallModified = mallModified;
	}
	public Integer getMallIsDeleted() {
		return mallIsDeleted;
	}
	public void setMallIsDeleted(Integer mallIsDeleted) {
		this.mallIsDeleted = mallIsDeleted;
	}
	public Date getMallStartTime() {
		return mallStartTime;
	}
	public void setMallStartTime(Date mallStartTime) {
		this.mallStartTime = mallStartTime;
	}
	public Date getMallBeginStartTime() {
		return mallBeginStartTime;
	}
	public void setMallBeginStartTime(Date mallBeginStartTime) {
		this.mallBeginStartTime = mallBeginStartTime;
	}
	public Date getMallEndStartTime() {
		return mallEndStartTime;
	}
	public void setMallEndStartTime(Date mallEndStartTime) {
		this.mallEndStartTime = mallEndStartTime;
	}
	public Date getMallEndTime() {
		return mallEndTime;
	}
	public void setMallEndTime(Date mallEndTime) {
		this.mallEndTime = mallEndTime;
	}
	
	public Integer getMallId() {
		return mallId;
	}
	public void setMallId(Integer mallId) {
		this.mallId = mallId;
	}
	public Integer getMallClassifyId() {
		return mallClassifyId;
	}
	public void setMallClassifyId(Integer mallClassifyId) {
		this.mallClassifyId = mallClassifyId;
	}
	public String getMallTitle() {
		return mallTitle;
	}
	public void setMallTitle(String mallTitle) {
		this.mallTitle = mallTitle;
	}
	public Integer getMallStatus() {
		return mallStatus;
	}
	public void setMallStatus(Integer mallStatus) {
		this.mallStatus = mallStatus;
	}
	public String getMallContentUrl() {
		return mallContentUrl;
	}
	public void setMallContentUrl(String mallContentUrl) {
		this.mallContentUrl = mallContentUrl;
	}
	public Integer getMallSortNum() {
		return mallSortNum;
	}
	public void setMallSortNum(Integer mallSortNum) {
		this.mallSortNum = mallSortNum;
	}
	public Integer getMallPlatformId() {
		return mallPlatformId;
	}
	public void setMallPlatformId(Integer mallPlatformId) {
		this.mallPlatformId = mallPlatformId;
	}
	public String getMallClassifyTitle() {
		return mallClassifyTitle;
	}
	public void setMallClassifyTitle(String mallClassifyTitle) {
		this.mallClassifyTitle = mallClassifyTitle;
	}
	public Integer getMallType() {
		return mallType;
	}
	public void setMallType(Integer mallType) {
		this.mallType = mallType;
	}
}
