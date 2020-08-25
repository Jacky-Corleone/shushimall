package com.camelot.searchcenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [商品属性对象]</p>
 * Created on 2015-3-4
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SearchItemAttrValue implements Serializable {

	private static final long serialVersionUID = 1878550677473628783L;
	
	private Long id;
	private String name;
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
	
	
	

}
