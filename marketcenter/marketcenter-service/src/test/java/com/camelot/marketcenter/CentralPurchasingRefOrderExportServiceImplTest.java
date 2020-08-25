package com.camelot.marketcenter;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.service.impl.CentralPurchasingRefOrderExportServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class CentralPurchasingRefOrderExportServiceImplTest {
	private final static Logger logger = LoggerFactory.getLogger(CentralPurchasingRefOrderExportServiceImplTest.class);
    private CentralPurchasingRefOrderExportServiceImpl centralPurchasingRefOrderExportService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		centralPurchasingRefOrderExportService = (CentralPurchasingRefOrderExportServiceImpl) ctx.getBean("centralPurchasingRefOrderExportService");
	}

	@Test
	public void addCentralPurchasingTest() {
		CentralPurchasingRefOrderDTO orderDTO = new CentralPurchasingRefOrderDTO();
		orderDTO.setActivitesDetailsId(1L);
		orderDTO.setInsertBy(1L);
		orderDTO.setInsertTime(new Date());
		orderDTO.setItemId(1L);
		orderDTO.setOrderId("123");
		orderDTO.setPurchaseNum(1);
		orderDTO.setPurchasePrice(BigDecimal.valueOf(100));
		orderDTO.setSkuId(1L);
		orderDTO.setUpdateBy(1L);
		orderDTO.setUpdateTime(new Date());
		ExecuteResult<Boolean> result = centralPurchasingRefOrderExportService.addCentralPurchasingRefOrder(orderDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryCentralPurchasingActivity(){
		CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO = new CentralPurchasingRefOrderDTO();
		Pager page = new Pager();
		page.setRows(28);
		ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> er = centralPurchasingRefOrderExportService.queryCentralPurchasingRefOrder(centralPurchasingRefOrderDTO, page);
		logger.info("\n 方法[{}]，结果：[{}]","testQueryCentralPurchasingActivity",JSON.toJSONString(er));
		Assert.assertEquals(true, er.isSuccess());
	}
}
