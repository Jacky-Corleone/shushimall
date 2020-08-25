package com.camelot.cmscenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>Description: [文章表签]</p>
 * Created on 2016年2月22日
 * @author  mengbo
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class CmsArticleTagsListDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//ID
	private String aid;//文章ID
	private String imgid;//图片ID
	private String tagid;//标签ID
	private String catename;//标签类型
	private Date addtime;//图片seo描述

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}
