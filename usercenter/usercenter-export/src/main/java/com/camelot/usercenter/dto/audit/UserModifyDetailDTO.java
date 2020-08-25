package com.camelot.usercenter.dto.audit;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *  
 *  @author 
 *  @createDate 
 **/
public class UserModifyDetailDTO implements Serializable {

	private static final long serialVersionUID = 8796864295116362066L;
	private java.lang.Long id;//  id
	private java.lang.Long modifyInfoId;//  关联modify_info表的id
	private java.lang.String propertiesColumn;//  申请修改的属性列名 (中文名称)
	private java.lang.String columnName; //申请修改的 列名(英文列名)
	private java.lang.String beforeChange;//  修改前的内容
	private java.lang.String afterChange;//  修改后的内容
	private java.util.Date createTime;//  申请创建时间
	private java.lang.String reviewReason;//  审核理由
	private String reviewUserId;//  审核人id
	private java.util.Date reviewTime;//  审核时间
	private java.lang.String tableName;//  要修改的表名字
	private java.lang.String beforeChangeValue;//  修改前的列中值
	private java.lang.String afterChangeValue;//  afterChangeValue
	private String  changeColumnName;//  被修改数据列ID 列名称
	private String changeValueId; //被修改数据主键ID
	
	
	
	
	public String getChangeColumnName() {
		return changeColumnName;
	}

	public void setChangeColumnName(String changeColumnName) {
		this.changeColumnName = changeColumnName;
	}

	public String getChangeValueId() {
		return changeValueId;
	}

	public void setChangeValueId(String changeValueId) {
		this.changeValueId = changeValueId;
	}

	public java.lang.String getColumnName() {
		return columnName;
	}

	public void setColumnName(java.lang.String columnName) {
		this.columnName = columnName;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.Long getModifyInfoId() {
		return this.modifyInfoId;
	}

	public void setModifyInfoId(java.lang.Long value) {
		this.modifyInfoId = value;
	}

	public java.lang.String getPropertiesColumn() {
		return this.propertiesColumn;
	}

	public void setPropertiesColumn(java.lang.String value) {
		this.propertiesColumn = value;
	}

	public java.lang.String getBeforeChange() {
		return this.beforeChange;
	}

	public void setBeforeChange(java.lang.String value) {
		this.beforeChange = value;
	}

	public java.lang.String getAfterChange() {
		return this.afterChange;
	}

	public void setAfterChange(java.lang.String value) {
		this.afterChange = value;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}

	public java.lang.String getReviewReason() {
		return this.reviewReason;
	}

	public void setReviewReason(java.lang.String value) {
		this.reviewReason = value;
	}

    public String getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public java.util.Date getReviewTime() {
		return this.reviewTime;
	}

	public void setReviewTime(java.util.Date value) {
		this.reviewTime = value;
	}

	public java.lang.String getTableName() {
		return this.tableName;
	}

	public void setTableName(java.lang.String value) {
		this.tableName = value;
	}

	public java.lang.String getBeforeChangeValue() {
		return this.beforeChangeValue;
	}

	public void setBeforeChangeValue(java.lang.String value) {
		this.beforeChangeValue = value;
	}

	public java.lang.String getAfterChangeValue() {
		return this.afterChangeValue;
	}

	public void setAfterChangeValue(java.lang.String value) {
		this.afterChangeValue = value;
	}


}
