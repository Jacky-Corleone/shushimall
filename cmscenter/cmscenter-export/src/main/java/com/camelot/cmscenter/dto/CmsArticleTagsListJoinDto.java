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
public class CmsArticleTagsListJoinDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//ID
	private String aid;//文章ID
	private String imgid;//图片ID
	private String tagid;//标签ID
	private String catename;//标签类型
	private Date addtime;//图片seo描述

	private String parentid;//父节点
	private String tagname;//标签名
	private String art_seo;//文章seo描述
	private String pro_seo;//文章seo描述
	private String img_seo;//图片seo描述
	private Integer type;//类型
	private Integer art_tag;//是否为文章标签
	private Integer img_tag;//是否为图片标签
	private Integer pro_nav;//是否为工程筛选项
	private Integer img_nav;//是否为图片筛选项

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

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getArt_seo() {
		return art_seo;
	}

	public void setArt_seo(String art_seo) {
		this.art_seo = art_seo;
	}

	public String getPro_seo() {
		return pro_seo;
	}

	public void setPro_seo(String pro_seo) {
		this.pro_seo = pro_seo;
	}

	public String getImg_seo() {
		return img_seo;
	}

	public void setImg_seo(String img_seo) {
		this.img_seo = img_seo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getArt_tag() {
		return art_tag;
	}

	public void setArt_tag(Integer art_tag) {
		this.art_tag = art_tag;
	}

	public Integer getImg_tag() {
		return img_tag;
	}

	public void setImg_tag(Integer img_tag) {
		this.img_tag = img_tag;
	}

	public Integer getPro_nav() {
		return pro_nav;
	}

	public void setPro_nav(Integer pro_nav) {
		this.pro_nav = pro_nav;
	}

	public Integer getImg_nav() {
		return img_nav;
	}

	public void setImg_nav(Integer img_nav) {
		this.img_nav = img_nav;
	}
}
