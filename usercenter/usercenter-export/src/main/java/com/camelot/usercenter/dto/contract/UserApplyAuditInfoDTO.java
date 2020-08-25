package com.camelot.usercenter.dto.contract;

import java.io.Serializable;

import com.camelot.openplatform.common.DataGrid;
/**
 * 
 * <p>Description: [商家信息审核]</p>
 * Created on 2015-3-10
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class UserApplyAuditInfoDTO  implements Serializable {
    
	private static final long serialVersionUID = 2107533971927947726L;
	private java.lang.Long id;//  id
	private java.lang.String companyName;//  公司名称
	private java.lang.Long shopId;//  店铺编号 商家编号
	private Integer applyType;//  申请类型 ()
	private String applyTime; //申请时间
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(java.lang.String companyName) {
		this.companyName = companyName;
	}
	public java.lang.Long getShopId() {
		return shopId;
	}
	public void setShopId(java.lang.Long shopId) {
		this.shopId = shopId;
	}
	public Integer getApplyType() {
		return applyType;
	}
	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	
	

}

