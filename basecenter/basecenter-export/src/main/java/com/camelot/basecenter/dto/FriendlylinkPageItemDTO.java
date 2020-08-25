package com.camelot.basecenter.dto;

import java.io.Serializable;

/** 
 * <p>Description: [友情链接 页面链接与中间表]</p>
 * Created on 2016年3月28日
 * @author  <a href="mailto: wufiuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */

public class FriendlylinkPageItemDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055262783490270451L;

	private Long id;
	
	private Long pageId;
	
	private Long itemId;
	
	
	private FriendlylinkItemDTO item;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public FriendlylinkItemDTO getItem() {
		return item;
	}

	public void setItem(FriendlylinkItemDTO item) {
		this.item = item;
	}
	
	
}
