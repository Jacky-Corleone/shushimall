package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [商品属性对象]</p>
 * Created on 2015-3-4
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2016年2月17日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */ 
public class ItemAttrValue implements Serializable {

	private static final long serialVersionUID = 1878550677473628783L;
	
	private Long id;
	private String name;
	private Integer status;//1 有效 2删除
	
	private Long attrId;//商品属性ID 添加卖家商品属性时 必填
	
	private String itemSkuAttr; // 组合商品sku
	private String itemSkuName; // 组合商品skuName
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAttrId() {
		return attrId;
	}
	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getItemSkuAttr() {
		return itemSkuAttr;
	}
	public void setItemSkuAttr(String itemSkuAttr) {
		this.itemSkuAttr = itemSkuAttr;
	}
	public String getItemSkuName() {
		return itemSkuName;
	}
	public void setItemSkuName(String itemSkuName) {
		this.itemSkuName = itemSkuName;
	}
}
