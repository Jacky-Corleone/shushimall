package com.camelot.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>Description: [从usercenter.user查用户数据   出参dto]</p>
 * Created on 2015-7-21
 * @author  武超强
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

public class PayBuyerInfo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private Long id; // 用户ID
    private String userName; // 用户名
    private String umobile; // 电话
    private Integer userType;// 用户类型 1 普通用户 2 买家  3卖家
    private Long parentId;// 父账号ID
    private String userEmail;// 邮箱
    private Date createTime;// 创建时间
    private Integer deleted; //是否可用状态 1 不可用 0 可用(冻结状态1 未冻结0)
    private BigDecimal payPriceTotal; //购买商品总额
    private BigDecimal orderNum;	//下单总数
    private BigDecimal shopNum;		//购买过的店铺总数
    
    private String createTimeStart; //开始日期  yyyyMMdd	(查询时用)
    private String createTimeEnd; //结束日期   yyyyMMdd	(查询时用)
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUmobile() {
		return umobile;
	}
	public void setUmobile(String umobile) {
		this.umobile = umobile;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public BigDecimal getPayPriceTotal() {
		return payPriceTotal;
	}
	public void setPayPriceTotal(BigDecimal payPriceTotal) {
		this.payPriceTotal = payPriceTotal;
	}
	public BigDecimal getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(BigDecimal orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getShopNum() {
		return shopNum;
	}
	public void setShopNum(BigDecimal shopNum) {
		this.shopNum = shopNum;
	}
	public String getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
    
}
