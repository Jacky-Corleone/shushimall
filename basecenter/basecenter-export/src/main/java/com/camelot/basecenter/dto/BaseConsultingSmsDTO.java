package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** 
 * <p>Description: [咨询]</p>
 * Created on 2015年3月20日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class BaseConsultingSmsDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;//主键
	private Long itemId;//商品id
	private String consulting;//咨询
	private String reply;//回复
	private Date created;//创建时间
	private Date modified;//回复时间
	private Long buyerId;//买家Id
	private Long sellerId;//卖家id
	private Integer status;//有效标记 :1有效 2无效
	
	private List<Long> buyerIdList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getConsulting() {
		return consulting;
	}
	public void setConsulting(String consulting) {
		this.consulting = consulting;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Long> getBuyerIdList() {
		return buyerIdList;
	}
	public void setBuyerIdList(List<Long> buyerIdList) {
		this.buyerIdList = buyerIdList;
	}
	
	
}
