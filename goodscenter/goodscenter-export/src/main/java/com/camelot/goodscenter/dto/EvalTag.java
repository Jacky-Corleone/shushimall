package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>DTO for 评价标签</p>
 * Created on 2016年2月22日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class EvalTag implements Serializable{
	private static final long serialVersionUID = -1618505328172321310L;
	private Integer tagId; // 标签id
	private String tagName; // 标签名

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
