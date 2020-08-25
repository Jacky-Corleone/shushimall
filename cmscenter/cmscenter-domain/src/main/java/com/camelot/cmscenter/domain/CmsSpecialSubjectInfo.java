package com.camelot.cmscenter.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * 专题BeanInfo
 * @author jh
 *
 */
public class CmsSpecialSubjectInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4945589633901823889L;
	  private String id;//ID
	  private String specialSubjectName;//专题名称
	  private String productCategory;//产品类别
	  private String specialSubjectCategory;//专题类别
	  private String specialSubjectTitle;//专题标题
	  private String specialSubjectModel;//专题模版
	  private String categoryProfile;//类别简介
	  private String specialSubjectPic;//专题图片
	  private int sort;//排序
	  private String address;//地址
	  private String keywords;//关键字
	  private String foregroundFlag;//是否为前台
	  private String displayFlag;//是否显示
	  private String delFlag;
	  private String createBy;
	  private Date createDate;
	  private String updateBy;
	  private Date updateDate;
	  private Date createDateStart;
	  private Date createDateEnd;
	  private Date updateDateStart;
	  private Date updateDateEnd;
	  private String remarks;//备注
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpecialSubjectName() {
		return specialSubjectName;
	}
	public void setSpecialSubjectName(String specialSubjectName) {
		this.specialSubjectName = specialSubjectName;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public String getSpecialSubjectCategory() {
		return specialSubjectCategory;
	}
	public void setSpecialSubjectCategory(String specialSubjectCategory) {
		this.specialSubjectCategory = specialSubjectCategory;
	}
	public String getSpecialSubjectTitle() {
		return specialSubjectTitle;
	}
	public void setSpecialSubjectTitle(String specialSubjectTitle) {
		this.specialSubjectTitle = specialSubjectTitle;
	}
	public String getSpecialSubjectModel() {
		return specialSubjectModel;
	}
	public void setSpecialSubjectModel(String specialSubjectModel) {
		this.specialSubjectModel = specialSubjectModel;
	}
	public String getCategoryProfile() {
		return categoryProfile;
	}
	public void setCategoryProfile(String categoryProfile) {
		this.categoryProfile = categoryProfile;
	}
	public String getSpecialSubjectPic() {
		return specialSubjectPic;
	}
	public void setSpecialSubjectPic(String specialSubjectPic) {
		this.specialSubjectPic = specialSubjectPic;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getForegroundFlag() {
		return foregroundFlag;
	}
	public void setForegroundFlag(String foregroundFlag) {
		this.foregroundFlag = foregroundFlag;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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
	public Date getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}
	public Date getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public Date getUpdateDateStart() {
		return updateDateStart;
	}
	public void setUpdateDateStart(Date updateDateStart) {
		this.updateDateStart = updateDateStart;
	}
	public Date getUpdateDateEnd() {
		return updateDateEnd;
	}
	public void setUpdateDateEnd(Date updateDateEnd) {
		this.updateDateEnd = updateDateEnd;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	  
}
