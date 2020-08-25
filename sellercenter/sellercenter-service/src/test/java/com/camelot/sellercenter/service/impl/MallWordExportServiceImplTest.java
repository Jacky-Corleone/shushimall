package com.camelot.sellercenter.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;
import com.camelot.sellercenter.mallword.service.MallWordExportService;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;

public class MallWordExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MallWordExportServiceImplTest.class);
	ApplicationContext ctx = null;
	MallWordExportService mallWordExportService = null;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallWordExportService = (MallWordExportService) ctx.getBean("mallWordExportService");
	}

	@Test
	public void testAdd() {
		MallWordDTO dto = new MallWordDTO();
		dto.setWord("测试word");
		ExecuteResult<MallWordDTO> result =mallWordExportService.add(dto);

		LOGGER.info(JSONObject.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDelete() {
		ExecuteResult<String> result =mallWordExportService.delete(18L);

		LOGGER.info(JSONObject.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}

}
