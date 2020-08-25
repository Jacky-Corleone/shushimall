/**
 * Copyright By Grandsoft Company Limited.  
 * 2012-7-17 下午02:17:37
 */
package com.camelot.common;

public enum MallType {
	/*TEACH(1,"教学文档"), */
	CENTER(2,"文档中心");
	/*COMPANY(3,"公司信息"), 
	SERVICE(4,"服务规则"), 
	RULE(5,"协议规则"), 
	RATE(6,"收费标准"),
    YJ(7,"页脚文档"),
	LYCENTER(8,"文档中心"),
	LYSERVICE(9,"服务规则"); */
	
	private int key;
	
	private String desc;

	private MallType(int key,String desc) {
		this.key = key;
		this.desc = desc;
	}
	
	private MallType(int key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}
	
	public int getKey(){
		return key;
	}

}
