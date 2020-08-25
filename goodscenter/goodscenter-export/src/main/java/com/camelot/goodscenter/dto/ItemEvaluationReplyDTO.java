package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品评价回复DTO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationReplyDTO implements Serializable {
	
	private static final long serialVersionUID = 580712480302604048L;
	private java.lang.Long id;//  id
	private java.lang.Long evaluationId;//  评价id
	private java.lang.String content;//  评价内容
	private java.util.Date createTime;//  创建时间
	private java.util.Date modifyTime;//  编辑时间
	
	public java.lang.Long getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(java.lang.Long evaluationId) {
		this.evaluationId = evaluationId;
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
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	
}
