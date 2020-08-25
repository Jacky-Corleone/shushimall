package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [文档分类]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallClassifyDTO implements Serializable{
		private static final long serialVersionUID = 1712537032811990206L;
		private Integer  id;//编号
		private String title;//分类标题
		private Integer status;//分类状态 1，已发布  0  已下架
		private Integer type;//分类类型
		private Date created;//创建时间
		private Date modified;//修改时间
		private Integer platformId;//平台id
		private Integer isDeleted;//分类是否删除 1，删除 0  未删除
		private Date startTime;//开始时间
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
