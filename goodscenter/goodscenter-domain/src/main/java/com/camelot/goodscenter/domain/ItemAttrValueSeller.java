package com.camelot.goodscenter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>Description: [item_attr_value商家属性值关联表]</p>
 * Created on 2015-3-11
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemAttrValueSeller implements Serializable {

	private static final long serialVersionUID = 8742432937807704561L;

	private Long sellerAttrValueId;
	private Long sellerAttrId;//商家属性关联ID
	private Long valueId;//属性值ID
	private String valueName;//属性值名称
	private Integer sortNumber;//排序号
	private Integer attrValueStatus;//属性值状态
	private Date created;
	private Date modified;
	public Long getSellerAttrValueId() {
		return sellerAttrValueId;
	}
	public void setSellerAttrValueId(Long sellerAttrValueId) {
		this.sellerAttrValueId = sellerAttrValueId;
	}
	public Long getSellerAttrId() {
		return sellerAttrId;
	}
	public void setSellerAttrId(Long sellerAttrId) {
		this.sellerAttrId = sellerAttrId;
	}
	public Long getValueId() {
		return valueId;
	}
	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}
	public Integer getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}
	public Integer getAttrValueStatus() {
		return attrValueStatus;
	}
	public void setAttrValueStatus(Integer attrValueStatus) {
		this.attrValueStatus = attrValueStatus;
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
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	
}
