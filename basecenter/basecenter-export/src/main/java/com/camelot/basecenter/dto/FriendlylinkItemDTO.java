package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [友情链接  页面表]</p>
 * Created on 2016年3月28日
 * @author  <a href="mailto: wufiuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */

public class FriendlylinkItemDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2573373069194166867L;
	
	private Long id;
	
	private String linkName;
	
	private String linkUrl;
	
	private Date createTime;//创建时间
	
	private Date updateTime;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	
	
}
