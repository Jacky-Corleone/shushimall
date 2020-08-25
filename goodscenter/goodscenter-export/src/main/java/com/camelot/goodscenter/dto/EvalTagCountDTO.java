package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * <p>评价标签计数</p>
 * <p>封装了{@link EvalTag}, 增加了count字段</p>
 * Created on 2016年2月24日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0
 * @see EvalTag
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class EvalTagCountDTO implements Serializable{

	private static final long serialVersionUID = -5835649866828809081L;
	private EvalTag evalTag; // 标签
	private Integer count; // 标签计数
	public EvalTag getEvalTag() {
		return evalTag;
	}
	public void setEvalTag(EvalTag evalTag) {
		this.evalTag = evalTag;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
