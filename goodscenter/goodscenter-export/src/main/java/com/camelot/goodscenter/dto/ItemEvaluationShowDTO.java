package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * <p>Description: [商品晒单表]</p>
 * Created on 2015年12月11日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationShowDTO implements Serializable{
	private static final long serialVersionUID = -390798210835481L;
	
	private Long id;
	private Long evaluationId;
	private Long skuId;
	private String imgUrl;
	private Integer isDelete;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(Long evaluationId) {
		this.evaluationId = evaluationId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
}
