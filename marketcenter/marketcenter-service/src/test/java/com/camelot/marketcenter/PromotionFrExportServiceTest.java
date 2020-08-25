package com.camelot.marketcenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class PromotionFrExportServiceTest {
    private PromotionFrExportService promotionFrExportService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		promotionFrExportService = (PromotionFrExportService) ctx.getBean("promotionFrExportService");
	}
	@Test
	public void  queryPromotionFrListTest(){
		PromotionFullReduction promotionFullReduction=new PromotionFullReduction();
		Pager page=new Pager();
		ExecuteResult<DataGrid<PromotionFullReduction>> result = promotionFrExportService.queryPromotionFrList(promotionFullReduction, page);
		System.out.println("result-----"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void deleteFrByPromotionId(){
		ExecuteResult<String> deleteFrByPromotionId=promotionFrExportService.deleteFrByPromotionId(22L);
		Assert.assertEquals(true, deleteFrByPromotionId.isSuccess());
	}
}
