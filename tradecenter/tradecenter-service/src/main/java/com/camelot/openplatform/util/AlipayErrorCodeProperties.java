package com.camelot.openplatform.util;

import java.util.Properties;

public class AlipayErrorCodeProperties {
	
	private static Properties props = null;//存储env.properties文件里所有配置项

	public static void init(Properties properties) {
		props = properties;
	}

	public static String getProperty(String key){
		return props.getProperty(key);
	}
	
	public static Properties getProperties(){
		
		return props;
	}
}
