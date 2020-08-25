package com.camelot.sellercenter.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrInDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;
import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;

public class MallRecAttrExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MallRecAttrExportServiceImplTest.class);
	ApplicationContext ctx = null;
	MallRecAttrExportService mallRecAttrExportService = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallRecAttrExportService = (MallRecAttrExportService) ctx.getBean("mallRecAttrExportService");
	}

	@Test
	public void testQueryMallRecAttrList() {
		Pager page=new Pager();
		MallRecAttrQueryDTO mallRecAttrQueryDTO=new MallRecAttrQueryDTO();
		mallRecAttrQueryDTO.setStatus(1);
		DataGrid<MallRecAttrDTO> res=mallRecAttrExportService.queryMallRecAttrList(page, mallRecAttrQueryDTO);
		LOGGER.info("操作方法{}，结果信息{}","queryMallRecAttrList", JSONObject.toJSONString(res));
	}

	@Test
	public void testdeleteMallRecAttrById() {
		ExecuteResult<String> executeResult=mallRecAttrExportService.deleteMallRecAttrById((long) 1111);
		LOGGER.info("操作方法{}，结果信息{}","testdeleteMallRecAttrById",executeResult);
	}

//	@Test
//	public void testGetMallRecAttrById() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testAddMallRecAttr() {
		MallRecAttrInDTO mallRecAttrInDTO = new MallRecAttrInDTO();
		mallRecAttrInDTO.setThemeId(1);
		mallRecAttrInDTO.setType(1);
		mallRecAttrExportService.addMallRecAttr(mallRecAttrInDTO);
	}
//
//	@Test
//	public void testModifyMallRecAttr() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testModifyMallRecAttrStatus() {
//		fail("Not yet implemented");
//	}

}
