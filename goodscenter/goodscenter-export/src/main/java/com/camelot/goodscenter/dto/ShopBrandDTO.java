package com.camelot.goodscenter.dto;

import java.io.Serializable;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * <p>Description: [店铺品牌DTO]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopBrandDTO implements Serializable {

	private static final long serialVersionUID = -7961591200167653142L;
	
	@PropertyMapping("id")
	private Long id;//
	
	@PropertyMapping("shopId")
	private Long shopId;//铺店id
	
	@PropertyMapping("brandId")
	private Long brandId;//品牌id
	
	@PropertyMapping("platformId")
	private Long platformId;//平台id
	
	@PropertyMapping("status")
	private String status;//店铺品牌状态：1为申请，2为通过，3为驳回
	
	@PropertyMapping("created")
	private java.util.Date created;//创建时间
	
	@PropertyMapping("modified")
	private java.util.Date modified;//更新时间
	
	@PropertyMapping("operatorId")
	private Long operatorId;//操作人
	
	@PropertyMapping("comment")
	private String comment;//回复
	
	@PropertyMapping("sellerId")
	private Long sellerId;//卖家id
	
	@PropertyMapping("cid")
	private Long cid;//三级类目id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public java.util.Date getModified() {
		return modified;
	}

	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
}
