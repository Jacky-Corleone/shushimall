package com.camelot.marketcenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.service.PromotionMdExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class PromotionMdExportServiceTest {
    private PromotionMdExportService promotionMdExportService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		promotionMdExportService = (PromotionMdExportService) ctx.getBean("promotionMdExportService");
	}
	
	@Test
	public void  queryPromotionFrListTest(){
		PromotionMarkdown promotionMarkdown=new PromotionMarkdown();
		Pager page=new Pager();
		ExecuteResult<DataGrid<PromotionMarkdown>> result = promotionMdExportService.queryPromotionMdList(promotionMarkdown, page);
		System.out.println("result-----"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void delete(){
		ExecuteResult<String> result=promotionMdExportService.deleteMdByPromotionId(30L);
		Assert.assertEquals(true, result.isSuccess());
	}
}
