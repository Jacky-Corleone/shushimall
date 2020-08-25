package com.camelot.basecenter.platformservice;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.basecenter.service.PlatformServiceRuleExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class PlatformServiceRuleTest {
   
	   private PlatformServiceRuleExportService platformServiceRuleExportService;
	   ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			platformServiceRuleExportService = (PlatformServiceRuleExportService) ctx.getBean("platformServiceRuleExportService");
		}
		
		@Test
		public void queryListTest(){
			PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
		    dto.setRuleId(1L);
		   // dto.setRuleName("aa");
			Pager page=new Pager();
			page.setPage(1);
			page.setRows(10);
			ExecuteResult<DataGrid<PlatformServiceRuleDTO>> er=platformServiceRuleExportService.queryList(dto, page);
			System.out.println(er.getResult().getTotal());
		}
		@Test
		public void modifyTest(){
			PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
			dto.setRuleId(2L);
			dto.setRuleName("服务规则");
			dto.setDetails("详细描述");
			dto.setSimpleIntro("规则简介");
			ExecuteResult<String> er=platformServiceRuleExportService.modifyPlatformServiceRule(dto);
			System.out.println(er.getResult());
		}
		@Test
		public void deleteTest(){
			PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
			dto.setRuleId(20150256L);
			dto.setStatus(0);
			ExecuteResult<String> er=platformServiceRuleExportService.modifyPlatformServiceRuleStatus(dto);
			System.out.println(er.getResult());
		}
}
