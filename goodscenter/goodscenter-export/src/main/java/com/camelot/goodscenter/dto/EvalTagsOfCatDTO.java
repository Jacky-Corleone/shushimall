package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>DTO for 评价标签</p>
 * Created on 2016年2月22日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class EvalTagsOfCatDTO implements Serializable {
	private static final long serialVersionUID = 5368776539501936127L;
	private Integer cidL1; // 一级类目id
	private Integer cidL2; // 二级类目id
	private Date created; // 创建时间
	private Date modified; // 更新时间
	private Short deleted; // 删除标志位(0：未删除， 1：删除)
	private ItemCategoryDTO catL1;
	private ItemCategoryDTO catL2;
	private List<EvalTag> evalTagGroups;
	private List<String> evalTagNames;
	
	public Integer getCidL2() {
		return cidL2;
	}

	public void setCidL2(Integer cidL2) {
		this.cidL2 = cidL2;
	}

	public Integer getCidL1() {
		return cidL1;
	}

	public void setCidL1(Integer cidL1) {
		this.cidL1 = cidL1;
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

	public Short getDeleted() {
		return deleted;
	}

	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}

	public List<EvalTag> getEvalTagGroups() {
		return evalTagGroups;
	}

	public void setEvalTagGroups(List<EvalTag> evalTagGroups) {
		this.evalTagGroups = evalTagGroups;
	}

	public ItemCategoryDTO getCatL1() {
		return catL1;
	}

	public void setCatL1(ItemCategoryDTO catL1) {
		this.catL1 = catL1;
	}

	public ItemCategoryDTO getCatL2() {
		return catL2;
	}

	public void setCatL2(ItemCategoryDTO catL2) {
		this.catL2 = catL2;
	}

	public List<String> getEvalTagNames() {
		return evalTagNames;
	}

	public void setEvalTagNames(List<String> evalTagNames) {
		this.evalTagNames = evalTagNames;
	}
}
