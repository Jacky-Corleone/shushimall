package com.camelot.goodscenter.dto;

import java.io.Serializable;
/**
 * 
 * @author Terry
 *
 */
public class ItemAttrValueItemDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Long attrId;//  属性ID
		private java.lang.Long valueId;//  属性值ID
		private java.lang.Long itemId;//  商品ID
		private java.lang.Integer attrType;//  属性类型:1:销售属性;2:非销售属性
		private java.lang.Integer sortNumber;//  排序号。越小越靠前。
		private java.lang.Integer status;//  记录状态   1.新建  2.删除
		private java.util.Date created;//  创建日期
		private java.util.Date modified;//  修改日期
	
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getAttrId() {
		return this.attrId;
	}

	public void setAttrId(java.lang.Long value) {
		this.attrId = value;
	}
	
	public java.lang.Long getValueId() {
		return this.valueId;
	}

	public void setValueId(java.lang.Long value) {
		this.valueId = value;
	}
	
	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}
	
	public java.lang.Integer getAttrType() {
		return this.attrType;
	}

	public void setAttrType(java.lang.Integer value) {
		this.attrType = value;
	}
	
	public java.lang.Integer getSortNumber() {
		return this.sortNumber;
	}

	public void setSortNumber(java.lang.Integer value) {
		this.sortNumber = value;
	}
	
	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

}

