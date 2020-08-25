package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:店铺信誉统计查询DTO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopEvaluationTotalQueryDTO implements Serializable {
	private static final long serialVersionUID = 4037053705999883984L;
	private Integer scopeVal;//评分级别
	private BigInteger scopeCount;//同一级别评分的数量
	private BigInteger scopeSum;//评分总和
	
	public Integer getScopeVal() {
		return scopeVal;
	}
	public void setScopeVal(Integer scopeVal) {
		this.scopeVal = scopeVal;
	}
	public BigInteger getScopeCount() {
		return scopeCount;
	}
	public void setScopeCount(BigInteger scopeCount) {
		this.scopeCount = scopeCount;
	}
	public BigInteger getScopeSum() {
		return scopeSum;
	}
	public void setScopeSum(BigInteger scopeSum) {
		this.scopeSum = scopeSum;
	}
	
	
}
