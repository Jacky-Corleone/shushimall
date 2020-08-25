package com.camelot.goodscenter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>Description: [item_attr商家属性关联表]</p>
 * Created on 2015-3-11
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemAttrSeller implements Serializable {

	private static final long serialVersionUID = -3655847602085448831L;

	private Long sellerAttrId;//ID
	private Long sellerId;//买家ID
	private Long categoryId;//类目ID
	private Long attrId;//属性ID
	private String attrName;//属性名称
	private Integer attrType;//属性类型:1:销售属性;2:非销售属性
	private Integer selectType;//是否多选。1：单选；2：多选
	private Integer attrStatus;//记录状态   1、有效；2、无效
	private Integer sortNumber;//排序号
	private Date created;//创建时间
	private Date modified;//修改时间
	private Long shopId;//店铺ID
	public Long getSellerAttrId() {
		return sellerAttrId;
	}
	public void setSellerAttrId(Long sellerAttrId) {
		this.sellerAttrId = sellerAttrId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getAttrId() {
		return attrId;
	}
	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}
	public Integer getAttrType() {
		return attrType;
	}
	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	public Integer getSelectType() {
		return selectType;
	}
	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}
	public Integer getAttrStatus() {
		return attrStatus;
	}
	public void setAttrStatus(Integer attrStatus) {
		this.attrStatus = attrStatus;
	}
	public Integer getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
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
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	
}
