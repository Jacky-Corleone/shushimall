package com.camelot.marketcenter;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.service.PromotionInfoExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class PromotionInfoExportServiceTest {
	
    private PromotionInfoExportService promotionInfoExportService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		promotionInfoExportService = (PromotionInfoExportService) ctx.getBean("promotionInfoExportService");
	}
	@Test
	public void  queryPromotionFrListTest(){
		PromotionInfo promotionInfo=new PromotionInfo();
		Pager page=new Pager();
		promotionInfo.setIsEffective("1");
		ExecuteResult<DataGrid<PromotionInfo>> result = promotionInfoExportService.queryPromotionInfoList(promotionInfo, page);
		System.out.println("result-----"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
}
