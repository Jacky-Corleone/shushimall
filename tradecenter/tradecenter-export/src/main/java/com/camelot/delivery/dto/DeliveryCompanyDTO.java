package com.camelot.delivery.dto;

import java.io.Serializable;

/** 
 * <p>Description: [快递商表]</p>
 * Created on 2015年8月10日
 * @author  <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class DeliveryCompanyDTO implements Serializable{
	
	private static final long serialVersionUID = -8515434078844538646L;
	
	private Integer id;
	private String companyName;
	private String companyCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

}
