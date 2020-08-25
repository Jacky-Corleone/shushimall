package com.camelot.openplatform.common.enmu;

/**
 * 
 * <p>Description: 枚举类</p>
 * Created on 2015年11月12日
 * @author  <a href="mailto: yinguijin@camelotchina.com">尹归晋</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum BasicEnum {

	//状态
	INT_ENUM_STATIC_ADDED(1,"1"),//上架
	INT_ENUM_STATIC_SHELVES(0,"0"),//下架
	//轮播图类型
	INT_ENUM_BANNERTYPE_BIGWHEEL(1,"1"),//大轮播
	INT_ENUM_BANNERTYPE_LITTLEWHEEL(2,"2"),//小轮播
	//主题类型
	THEME_ID_CAR(-1), 
	THEME_ID_HOME(0), 
	INT_ENUM_THEMETYPE_HOME(1),//首页 
	INT_ENUM_THEMETYPE_CATEGORY(2),//类目
	INT_ENUM_THEMETYPE_AREA(3),//地区
	INT_ENUM_THEMETYPE_INTEGRAL(4),//积分商城
	//广告类型
	INT_ENUM_ADTYPE_THEME(1, "主题广告"), 
	INT_ENUM_ADTYPE_LOGIN(2, "登录广告"), 
	INT_ENUM_ADTYPE_HEAD(3, "头部广告"), 
	INT_ENUM_ADTYPE_CATEGORY(4, "类目广告"), 
	INT_ENUM_ADTYPE_HOTFEWIMG(5, "热销小图"), 
	INT_ENUM_ADTYPE_HOTBIGIMG(6, "热销大图");

	private Integer intVlue;

	private String stringValue;

	private BasicEnum(Integer intVlue, String stringValue) {
		this.intVlue = intVlue;
		this.stringValue = stringValue;

	}

	private BasicEnum(String stringValue) {
		this.stringValue = stringValue;
	}

	private BasicEnum(Integer intValue) {
		this.intVlue = intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public int getIntVlue() {
		return intVlue;
	}
}
