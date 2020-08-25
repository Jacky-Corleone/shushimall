package com.camelot.openplatform.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author
 * 
 */
public class SystemBootstrap implements InitializingBean {
	/**
	 * CONFIG_FILE_PATH 系统变量配置文件路径
	 */
	private static final String CONFIG_FILE_PATH = "/env.properties";
	private final static Logger logger = LoggerFactory
			.getLogger(SystemBootstrap.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = SystemBootstrap.class
					.getResourceAsStream(CONFIG_FILE_PATH);
			properties.load(inputStream);
			logger.info("系统配置项:" + properties);
		} catch (Exception e) {
			logger.error("读取系统配置文件时发生错误：", e);
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("关闭文件输入流失败：", e);
				}
			}
		}
		SysProperties.init(properties);
	}
}
