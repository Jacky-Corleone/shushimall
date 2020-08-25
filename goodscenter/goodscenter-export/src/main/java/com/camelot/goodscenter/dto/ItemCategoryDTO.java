package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;

public class ItemCategoryDTO implements Serializable{
	private static final long serialVersionUID = 2990627565835351070L;
	 private Long categoryCid;        //类目id
	 private Long categoryParentCid;  //父级ID
	 private Long[] categoryParentCids;  //父级ID组
	 private String categoryCName;    //类目名称
	 private Integer categoryLev;     //类目级别
	 private Integer categoryStatus;  //1:有效;0:删除
	 private Integer categoryHasLeaf; //是否叶子节点
	 private Integer categorySortNumber;//排序号
	 private Integer categoryHomeShow; //是否前台首页展示,1:是;2:否
	 private Date categoryCreated;     //创建日期
	 private Date categoryModified;    //修改日期
	 
	 
	 
	public Long[] getCategoryParentCids() {
		return categoryParentCids;
	}
	public void setCategoryParentCids(Long[] categoryParentCids) {
		this.categoryParentCids = categoryParentCids;
	}
	public Long getCategoryCid() {
		return categoryCid;
	}
	public void setCategoryCid(Long categoryCid) {
		this.categoryCid = categoryCid;
	}
	public Long getCategoryParentCid() {
		return categoryParentCid;
	}
	public void setCategoryParentCid(Long categoryParentCid) {
		this.categoryParentCid = categoryParentCid;
	}
	public String getCategoryCName() {
		return categoryCName;
	}
	public void setCategoryCName(String categoryCName) {
		this.categoryCName = categoryCName;
	}
	public Integer getCategoryLev() {
		return categoryLev;
	}
	public void setCategoryLev(Integer categoryLev) {
		this.categoryLev = categoryLev;
	}
	public Integer getCategoryStatus() {
		return categoryStatus;
	}
	public void setCategoryStatus(Integer categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
	public Integer getCategoryHasLeaf() {
		return categoryHasLeaf;
	}
	public void setCategoryHasLeaf(Integer categoryHasLeaf) {
		this.categoryHasLeaf = categoryHasLeaf;
	}
	public Integer getCategorySortNumber() {
		return categorySortNumber;
	}
	public void setCategorySortNumber(Integer categorySortNumber) {
		this.categorySortNumber = categorySortNumber;
	}
	public Integer getCategoryHomeShow() {
		return categoryHomeShow;
	}
	public void setCategoryHomeShow(Integer categoryHomeShow) {
		this.categoryHomeShow = categoryHomeShow;
	}
	public Date getCategoryCreated() {
		return categoryCreated;
	}
	public void setCategoryCreated(Date categoryCreated) {
		this.categoryCreated = categoryCreated;
	}
	public Date getCategoryModified() {
		return categoryModified;
	}
	public void setCategoryModified(Date categoryModified) {
		this.categoryModified = categoryModified;
	}
	
     
} 
