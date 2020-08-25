package com.camelot.basecenter.domain;

/** 
 * Description: [描述该类概要功能介绍]
 * Created on 2016年02月24日
 * @author  <a href="mailto: XXXXXX@camelotchina.com">中文名字</a>
 * @version 1.0 
 * Copyright (c) 2016年 北京柯莱特科技有限公司 交付部 
 */
public class Iptable {

	//ID
	private String id;
	//起始数字
	private String startipnum;
	//起始IP段
	private String startiptext;
	//结束数字
	private String endipnum;
	//结束IP段
	private String endiptext;
	//国家
	private String country;
	//运营商
	private String local;
	//查询数字
	private Long num;
	
// setter and getter
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	public String getStartipnum(){
		return startipnum;
	}
	
	public void setStartipnum(String startipnum){
		this.startipnum = startipnum;
	}
	public String getStartiptext(){
		return startiptext;
	}
	
	public void setStartiptext(String startiptext){
		this.startiptext = startiptext;
	}
	public String getEndipnum(){
		return endipnum;
	}
	
	public void setEndipnum(String endipnum){
		this.endipnum = endipnum;
	}
	public String getEndiptext(){
		return endiptext;
	}
	
	public void setEndiptext(String endiptext){
		this.endiptext = endiptext;
	}
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	public String getLocal(){
		return local;
	}
	
	public void setLocal(String local){
		this.local = local;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}
	
}
