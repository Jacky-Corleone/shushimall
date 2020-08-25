package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** 
 * <p>Description: [友情链接  页面表]</p>
 * Created on 2016年3月18日
 * @author  <a href="mailto: wufiuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */

public class FriendlylinkPageDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8273224274816633088L;
	
	private Long id;
	
	private String pageName;
	
	private String pageUrl;
	
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
	
	private List<FriendlylinkPageItemDTO> pageItems;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public List<FriendlylinkPageItemDTO> getPageItems() {
		return pageItems;
	}

	public void setPageItem(List<FriendlylinkPageItemDTO> pageItems) {
		this.pageItems = pageItems;
	}

}
