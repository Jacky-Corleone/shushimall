package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品评价统计明细DTO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationTotalDetailDTO implements Serializable {
	private static final long serialVersionUID = 2760083891309322693L;
	private Double percent; //百分比
	private Integer count; //总数量
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
