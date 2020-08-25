package com.camelot.openplatform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *  定时任务控制
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-6-5
 */
public class SchedulerFactoryBeanProxy extends SchedulerFactoryBean{

	private static final Logger logger = LoggerFactory.getLogger(SchedulerFactoryBeanProxy.class);
	private boolean startFlag = true;
	
	public void afterPropertiesSet() throws Exception {
		if(!startFlag){
			logger.warn("*******************quartz not start!*******************");
			return;
		}
		super.afterPropertiesSet();
	}

	public boolean isStartFlag() {
		return startFlag;
	}

	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}
	
}
