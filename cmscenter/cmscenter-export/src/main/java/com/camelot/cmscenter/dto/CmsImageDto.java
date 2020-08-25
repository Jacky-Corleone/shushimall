package com.camelot.cmscenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>Description: [图片管理服务]</p>
 * Created on 2016年2月22日
 * @author  mengbo
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CmsImageDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//
	private String aid;//artile外键
	private String imgtype;//图片类型
	private String imgname;//图片名称
	private String remark;//备注
	private String path;//路片路径
	private Integer viewnum;//浏览次数
	private Integer addview;//添加次数
	private Integer praisenum;//点赞数
	private Integer addpraise;//添加点赞数
	private String iscomment;//
	private Integer list;//排序
	private Integer ishide;//Integer
	private String editor;//修改人
	private String author;//作者
	private String releasetime;//发布时间
	private Date addtime;//添加时间
	private Date createdate;//创建时间
	private Date updateby;//更新时间
	private String createby;//创建人
	private String delflag;//删除标识
	private String remarks;//备注
	private String title;//外键
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

	public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
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

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdateby() {
		return updateby;
	}

	public void setUpdateby(Date updateby) {
		this.updateby = updateby;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getDelflag() {
		return delflag;
	}

	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
