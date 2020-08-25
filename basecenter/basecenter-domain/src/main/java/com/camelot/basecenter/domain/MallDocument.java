package com.camelot.basecenter.domain;

import java.util.Date;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * 
 * @author 周立明
 *
 */

public class MallDocument {
    @PropertyMapping("mallId")
	private Integer id;//
    @PropertyMapping("mallClassifyId")
	private Integer classifyId;//帮助文档分类
    @PropertyMapping("mallTitle")
	private String title;//帮助文档标题	
    private Integer type;//帮助文档类型
    @PropertyMapping("mallStatus")
    private Integer status;//帮助文档状态 1，已下架 2，已发布
    @PropertyMapping("mallContentUrl")
	private String contentUrl;//内容jfs地址
	private Date created;//创建时间
	private Date modified;//修改时间
	private String isDeleted;//分类是否删除 1，未删除 2，已删除
	@PropertyMapping("mallSortNum")
	private Integer sortNum;//排序号
	private Date startTime;//
	private Date endTime;//
	private Integer platformId;//平台ID
	
// setter and getter
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	public Integer getClassifyId(){
		return classifyId;
	}
	
	public void setClassifyId(Integer classifyId){
		this.classifyId = classifyId;
	}
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
	public String getContentUrl(){
		return contentUrl;
	}
	
	public void setContentUrl(String contentUrl){
		this.contentUrl = contentUrl;
	}
	
	public String getIsDeleted(){
		return isDeleted;
	}
	
	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}
	public Integer getSortNum(){
		return sortNum;
	}
	
	public void setSortNum(Integer sortNum){
		this.sortNum = sortNum;
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

	public Integer getPlatformId(){
		return platformId;
	}
	
	public void setPlatformId(Integer platformId){
		this.platformId = platformId;
	}
}
