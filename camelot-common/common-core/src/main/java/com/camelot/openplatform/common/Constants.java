package com.camelot.openplatform.common;

/**
 * 
 * <p>
 * Description: [系统常用变量]
 * </p>
 * Created on 2015年9月1日
 * 
 * @author <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class Constants {

	/**
	 * 绿印平台ID
	 */
	public final static Integer PLATFORM_ID = 2;
	
	/**
	 * 科印平台ID
	 */
	public final static Integer PLATFORM_KY_ID = 1;
	
	/**
	 * 小管家平台ID
	 */
	public final static Integer PLATFORM_WX_ID = 3;

	/**
	 * 绿印平台logo放入redis中的key
	 */
	public final static String KEY_GREEN_LOGO_REDIS = "greenLogo";

	/**
	 * 科印平台logo放入redis中的key
	 */
	public final static String KEY_LOGO_REDIS = "logo";

	/**
	 * 二级域名存放在redis中的key
	 */
	public static final String SECOND_DOMAIN_KEY = "second_domain_key";

	/**
	 * 三级域名存放在redis中的key
	 */
	public static final String THIRD_DOMAIN_KEY = "third_domain_key";
}
