package com.camelot.basecenter.domain;

import java.util.Date;
/*
 * 基础省市县编码表
 */
public class AddressBase {
    private Integer id;
	private String code;//县镇市省编码
	private String parentcode;//父级编码
	private String name;//地区名称
	private int level;//等级:1省 2市 3县/县级市/区 4镇/街道 5村/社
	private int yn;
	private Date createtime;//开始时间：样式-“yyyyMMdd”
	private Date updatetime;//结束时间：样式-“yyyyMMdd”
	
	private String nameFirstLetter;//地区名称(拼音首字母)
	private String nameLetter;//地区名称(拼音)
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getYn() {
		return yn;
	}
	public void setYn(int yn) {
		this.yn = yn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getNameFirstLetter() {
		return nameFirstLetter;
	}
	public void setNameFirstLetter(String nameFirstLetter) {
		this.nameFirstLetter = nameFirstLetter;
	}
	public String getNameLetter() {
		return nameLetter;
	}
	public void setNameLetter(String nameLetter) {
		this.nameLetter = nameLetter;
	}
}
