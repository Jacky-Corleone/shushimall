package com.camelot.shop.test;


import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopTemplatesDTO;
import com.camelot.storecenter.service.ShopTemplatesExportService;

public class ShopTemplatesExportServiceTest {
	
	ApplicationContext ctx = null;
	ShopTemplatesExportService shopTemplatesExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopTemplatesExportService = (ShopTemplatesExportService) ctx.getBean("shopTemplatesExportService");
	}
	@Test
	public void  queryShopTemplateListTest(){
		ShopTemplatesDTO shopTemplatesDTO=new ShopTemplatesDTO();
		shopTemplatesDTO.setShopId(1l);
		ExecuteResult<List<ShopTemplatesDTO>> result = shopTemplatesExportService.createShopTemplatesList(shopTemplatesDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void  modifyShopTemplatesStatusTest(){
		 ExecuteResult<String> result = shopTemplatesExportService.modifyShopTemplatesStatus(3l,1l);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void  modifyShopTemplatesColorTest(){
		 ExecuteResult<String> result = shopTemplatesExportService.modifyShopTemplatesColor(2l, "#111222");
		 Assert.assertEquals(true, result.isSuccess());
	}
	
}
