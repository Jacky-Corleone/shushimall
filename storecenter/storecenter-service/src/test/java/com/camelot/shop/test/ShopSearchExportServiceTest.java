package com.camelot.shop.test;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopSearchExportService;

public class ShopSearchExportServiceTest {
	
	ApplicationContext ctx = null;
	ShopSearchExportService shopSearchExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopSearchExportService = (ShopSearchExportService) ctx.getBean("shopSearchExportService");
	}
	
	@Test
	public void testSearchShop(){
		ShopDTO inDTO = new ShopDTO();
		inDTO.setCollation(1);
		ExecuteResult<DataGrid<ShopDTO>> result = shopSearchExportService.searchShop(inDTO, null);
		Assert.assertEquals(true, result.isSuccess());
	}
}
