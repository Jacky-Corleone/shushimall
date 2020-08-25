package com.camelot.goodscenter.domain;

import java.io.Serializable;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品评价回复domain类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationReply implements Serializable {

	private static final long serialVersionUID = -5384707102805105545L;
	private java.lang.Long id;//  id
	private java.lang.Long evaluation_id;//  评价id
	private java.lang.String content;//  评价内容
	private java.util.Date createTime;//  创建时间
	private java.util.Date modifyTime;//  编辑时间
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getEvaluation_id() {
		return evaluation_id;
	}
	public void setEvaluation_id(java.lang.Long evaluation_id) {
		this.evaluation_id = evaluation_id;
	}
	public java.lang.String getContent() {
		return content;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
