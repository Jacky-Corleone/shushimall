package com.camelot.usercenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.util.SysProperties;

public class StartServer {
	private final static Logger LOG = LoggerFactory.getLogger(StartServer.class);

	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath*:/spring*/**/spring-*.xml");
			context.start();
			LOG.info("=========================={}启动成功==========================", SysProperties.getProperty("server.name"));
		} catch (Exception e) {
			LOG.error("== DubboProvider context start error:", e);
		}
		synchronized (StartServer.class) {
			try {
				StartServer.class.wait();
			} catch (InterruptedException e) {
				LOG.error("== synchronized error:", e);
			}
		}

	}
	
}
