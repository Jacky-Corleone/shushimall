package com.camelot.shop.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.combin.ShopAudiDTO;
import com.camelot.storecenter.service.ShopAudiExportService;

public class ShopAudiExportServiceTest {
	
	ApplicationContext ctx = null;
	ShopAudiExportService shopAudiExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopAudiExportService = (ShopAudiExportService) ctx.getBean("shopAudiExportService");
	}
	@Test
	public void  queryShopAuditInfoTest(){
		ExecuteResult<ShopAudiDTO> result = shopAudiExportService.queryShopAuditInfo(1l);
		Assert.assertEquals(true, result.isSuccess());
	}
	
}
