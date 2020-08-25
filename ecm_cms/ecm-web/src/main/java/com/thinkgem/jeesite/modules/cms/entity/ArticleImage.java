/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 文章Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "cms_article_img")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed @Analyzer(impl = IKAnalyzer.class)
public class ArticleImage extends IdEntity<ArticleImage> {


	private static final long serialVersionUID = 1L;
	private String aid;//文章编号
	private String imgtype;//图片类型
	private String imgname;//图片名称
	private String remark;//备注
	private String path;//路片路径
	private Integer viewnum;//浏览次数
	private Integer addview;//添加次数
	private Integer praisenum;//点赞数
	private Integer addpraise;//添加点赞数
	private Integer iscomment;//
	private Integer list;//排序
	private Integer ishide;//隐藏
	private Integer ishtml;//是否生成静态页面
	private String editor;//修改人
	private String author;//作者
	private Date releasetime;//发布时间
	private Date addtime;//添加时间
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getImgtype() {
		return imgtype;
	}

	public void setImgtype(String imgtype) {
		this.imgtype = imgtype;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getViewnum() {
		return viewnum;
	}

	public void setViewnum(Integer viewnum) {
		this.viewnum = viewnum;
	}

	public Integer getAddview() {
		return addview;
	}

	public void setAddview(Integer addview) {
		this.addview = addview;
	}

	public Integer getPraisenum() {
		return praisenum;
	}

	public void setPraisenum(Integer praisenum) {
		this.praisenum = praisenum;
	}

	public Integer getAddpraise() {
		return addpraise;
	}

	public void setAddpraise(Integer addpraise) {
		this.addpraise = addpraise;
	}

	public Integer getIscomment() {
		return iscomment;
	}

	public void setIscomment(Integer iscomment) {
		this.iscomment = iscomment;
	}

	public Integer getList() {
		return list;
	}

	public void setList(Integer list) {
		this.list = list;
	}

	public Integer getIshide() {
		return ishide;
	}

	public void setIshide(Integer ishide) {
		this.ishide = ishide;
	}

	public Integer getIshtml() {
		return ishtml;
	}

	public void setIshtml(Integer ishtml) {
		this.ishtml = ishtml;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(Date releasetime) {
		this.releasetime = releasetime;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}


