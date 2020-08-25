package com.camelot.util;

import com.camelot.openplatform.util.SysProperties;

public class Constants {
	/**
	 * 模块类型：买家模块
	 */
	public static Integer MODULE_TYPE_BUYER = 1;
	
	/**
	 * 模块类型：卖家模块
	 */
	public static Integer MODULE_TYPE_SELLER = 2;
	/**
	 * 正式微信appid
	 */
    public static String WXAPPID = SysProperties.getProperty("wxAppId");

	/**
	 * 正式微信appsecret
	 */
	public static String WXAPPSECRET = SysProperties.getProperty("wxAppsecret");
	/**
	 * 正式微信信息测试模板id
	 */
	public static String Url = SysProperties.getProperty("wx_call_back_uri");

}
