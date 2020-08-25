package com.camelot.basecenter.dto;

import java.io.Serializable;

/** 
 * <p>Description: [类型表]</p>
 * Created on 2015年8月11日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TypeDTO implements Serializable{
	/**
	 * <p>Discription:[类型]</p>
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;		//id
	private String name;	//名称
	private String type;	//类型
	private Integer status;		//是否启用:1是启用，0是禁用
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
