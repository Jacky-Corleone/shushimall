package com.camelot.openplatform.util;

import java.util.Properties;

public class SysProperties {

	private static Properties props = null;//存储env.properties文件里所有配置项

	public static void init(Properties properties) {
		props = properties;
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static Properties getProperties() {
		return props;
	}
	
	//下面为jeesite需要的配置信息

	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getProperty("adminPath");
	}

	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getProperty("frontPath");
	}

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getProperty("urlSuffix");
	}

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getProperty("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getProperty("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
}
