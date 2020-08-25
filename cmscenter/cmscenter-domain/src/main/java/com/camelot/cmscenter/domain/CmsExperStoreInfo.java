package com.camelot.cmscenter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 全国体验店Info
 * @author jh
 *
 */
public class CmsExperStoreInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 店铺名称
	 */
	private String storeName;
	/**
	 * 坐标
	 */
	private String coordinate;
	/**
	 * 店铺地址
	 */
	private String address;
	/**
	 * 店铺介绍
	 */
	private String introduce;
	/**
	 * 省Id
	 */
	private String provinceId;
	/**
	 * 市的父级Id
	 */
	private String pernetId;
	/**
	 * 市
	 */
	private String cityId;
	/**
	 * 是否删除
	 */
	private int delFlag;
	/**
	 * 添加人
	 */
	private String createBy;
	/**
	 * 添加时间
	 */
	private Date createDate;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	/**
	 * 备注
	 * @return
	 */
	private String remarks;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPernetId() {
		return pernetId;
	}
	public void setPernetId(String pernetId) {
		this.pernetId = pernetId;
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
	
}
