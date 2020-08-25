package com.camelot.usercenter.dto.fieldIdentification;

import java.io.Serializable;
import java.util.Date;

/**
 * 卖家实地认证图片DTO
 * 董其超
 * 2015年7月17日
 */
public class FieldIdentificationPictureDTO implements Serializable {

	private static final long serialVersionUID = 4553512059500803524L;

	private Long id;//主键
	private Long userId; //卖家ID
	private Long extendId; //卖家扩展ID
	private Integer pictureType; //实地认证图片类型：0厂房或产品，1企业大门，2办公场所，3其他证书
	private Integer sortNumber; //实地认证图片排序号
	private String pictureSrc; //实地认证图片URL地址
	private String uploadorId;//上传人ID
	private Date createDate;//创建时间
	private Date updateDate;//修改时间
	private Integer deleteFlag;//逻辑删除标记：0未逻辑删除，1已逻辑删除


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getExtendId() {
		return extendId;
	}
	public void setExtendId(Long extendId) {
		this.extendId = extendId;
	}
	public Integer getPictureType() {
		return pictureType;
	}
	public void setPictureType(Integer pictureType) {
		this.pictureType = pictureType;
	}
	public Integer getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}
	public String getPictureSrc() {
		return pictureSrc;
	}
	public void setPictureSrc(String pictureSrc) {
		this.pictureSrc = pictureSrc;
	}
	public String getUploadorId() {
		return uploadorId;
	}
	public void setUploadorId(String uploadorId) {
		this.uploadorId = uploadorId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
