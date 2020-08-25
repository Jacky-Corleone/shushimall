package com.camelot.report;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.ItemSkuSellReportIn;
import com.camelot.report.dto.ItemSkuSellReportOut;
import com.camelot.report.service.ItemSkuSellReportService;

public class ItemSkuSellReportServiceTest {

	private ItemSkuSellReportService itemSkuSellReportService;
	private ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		itemSkuSellReportService = (ItemSkuSellReportService) ctx.getBean("itemSkuSellReportService");
	}
	
	@Test
	public void testGetItemSkuSellListByPager() {
		ItemSkuSellReportIn reportIn=new ItemSkuSellReportIn();
		reportIn.setStartDate("20150701");
		reportIn.setEndDate("20150714");
		//reportIn.setDateFormat("yyyy.MM.dd");
		reportIn.setShopId(2000000157L);
		//reportIn.setSkuId(1000000107L);
		reportIn.setItemId(1000000112L);
		DataGrid<ItemSkuSellReportOut> dataGrid = itemSkuSellReportService.getItemSkuSellListByPager(reportIn, new Pager<ItemSkuSellReportOut>());
		
		System.out.println("----------------"+dataGrid.getTotal());
		System.out.println("----------------"+dataGrid.getRows());
	}

	@Test
	public void testGetItemSkuSellListByLion() throws Exception {
		ItemSkuSellReportIn reportIn=new ItemSkuSellReportIn();
		reportIn.setStartDate("20150601");
		reportIn.setEndDate("20150714");
		reportIn.setShopId(2000000157L);
		reportIn.setSkuId(1000000107L);
		reportIn.setDayInterval(7);
		
		DataGrid<ItemSkuSellReportOut> dataGrid = itemSkuSellReportService.getItemSkuSellListByLion(reportIn);
		
		System.out.println(dataGrid.getRows());

	}
	

}
