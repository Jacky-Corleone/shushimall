package com.camelot.sellercenter.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;

public class LogoExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogoExportServiceImplTest.class);
	ApplicationContext ctx = null;
	LogoExportService logoExportService = null;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		logoExportService = (LogoExportService) ctx.getBean("logoServiceImpl");
	}
	
	@Test
	public void testGetMallLogo() {
		ExecuteResult<LogoDTO> result = logoExportService.getMallLogoByPlatformId(null);
		Assert.assertEquals(true, result.isSuccess());
		LOGGER.info(JSON.toJSONString(result));
	}
	
	@Test
	public void testModifyMallLogo() {
		ExecuteResult<String> result = logoExportService.modifyMallLogoByPlatformId("绿印2", "123", 2);
		Assert.assertEquals(true, result.isSuccess());
		LOGGER.info(JSON.toJSONString(result));
	}
}
