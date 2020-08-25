package com.camelot.cmscenter.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 专题模版BeanDTO
 * @author jh
 *
 */
public class CmsSpecialSubjectModelInfo implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = -1837246159943911530L;

	/**
	 * 
	 */
	  private String id;//ID
	  
	  private String name;
	  
	  private String createBy;
	  
	  private Date createDate;
	  
	  private String updateBy;
	  
	  private Date updateDate;
	  
	  private String remarks;//备注
	  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	  
	 
	
}
