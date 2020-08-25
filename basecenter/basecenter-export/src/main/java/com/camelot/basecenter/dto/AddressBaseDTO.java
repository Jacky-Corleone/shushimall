package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [基础省市县编码表]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class AddressBaseDTO implements Serializable{
	/**
	 * <p>Discription:[省市县类]</p>
	 */
	private static final long serialVersionUID = -7667606312154895090L;
	
	private int id;// ID
	private String code;//县镇市省编码
	private String parentcode;//父级编码
	private String name;//地区名称
	private int level;//等级:1省 2市 3县/县级市/区 4镇/街道 5村/社
	private int yn;   //状态：1启用 2已作废
	private Date createtime;//开始时间：样式-“yyyyMMdd”
	private Date updatetime;//结束时间：样式-“yyyyMMdd”
	private String nameFirstLetter;//地区名称(拼音首字母)
	private String nameLetter;//地区名称(拼音)
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
