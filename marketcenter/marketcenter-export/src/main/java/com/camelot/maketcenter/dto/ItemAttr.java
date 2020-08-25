package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>Description: [商品属性对象]</p>
 * Created on 2015-3-4
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemAttr implements Serializable {

	private static final long serialVersionUID = -212851812667326995L;

	private Long id;
	private String name;
	private Integer status;//属性状态 1有效 2 删除
	
	private List<ItemAttrValue> values;
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
	public List<ItemAttrValue> getValues() {
		return values;
	}
	public void setValues(List<ItemAttrValue> values) {
		this.values = values;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
