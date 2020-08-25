package com.camelot.tradecenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.SalesVolumeDTO;
import com.camelot.tradecenter.service.SalesVolumeExportService;

public class SalesVolumeExportServiceImplAllTest{
	
	private static final Logger logger = LoggerFactory.getLogger(SalesVolumeExportServiceImplAllTest.class);
	
	private SalesVolumeExportService salesVolumeExportService = null;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		salesVolumeExportService = (SalesVolumeExportService) ctx.getBean("salesVolumeExportService");
	}
	
	@Test
	public void testQuerySkuSalesVolume(){
		SalesVolumeDTO inDTO = new SalesVolumeDTO();
		ExecuteResult<List<SalesVolumeDTO>> result = this.salesVolumeExportService.querySkuSalesVolume(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryShopSalesVolume(){
		SalesVolumeDTO inDTO = new SalesVolumeDTO();
		ExecuteResult<List<SalesVolumeDTO>> result = this.salesVolumeExportService.queryShopSalesVolume(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
}
