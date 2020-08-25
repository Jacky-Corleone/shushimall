package com.camelot.util;

import javax.servlet.ServletContextEvent;

import com.camelot.openplatform.util.SysProperties;

public class ServletContextLoaderListener implements javax.servlet.ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("====================ftp_server_dir====================");
		System.out.println(SysProperties.getProperty("ftp_server_dir"));
		System.out.println("====================ftp_server_dir====================");
		//图片服务器地址
		sce.getServletContext().setAttribute("imageServerAddr", SysProperties.getProperty("ftp_server_dir"));
        //网站首页域名
        sce.getServletContext().setAttribute("siteDomain", SysProperties.getProperty("site.domain"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
