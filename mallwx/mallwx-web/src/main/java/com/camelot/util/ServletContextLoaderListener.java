package com.camelot.util;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.openplatform.util.SysProperties;

public class ServletContextLoaderListener implements javax.servlet.ServletContextListener{
	private final static Logger LOGGER = LoggerFactory.getLogger(ServletContextLoaderListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("====================ftp_server_dir====================");
		LOGGER.info(SysProperties.getProperty("ftp_server_dir"));
		LOGGER.info("====================ftp_server_dir====================");
		//图片服务器地址
		sce.getServletContext().setAttribute("imageServerAddr", SysProperties.getProperty("ftp_server_dir"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
