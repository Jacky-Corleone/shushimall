package com.camelot.report;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.OrderDealReportIn;
import com.camelot.report.dto.OrderDealReportOut;
import com.camelot.report.service.OrderDealReportService;

public class OrderDealReportServiceTest {

	private OrderDealReportService orderDealReportService;
	private ApplicationContext ctx;
	
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		orderDealReportService = (OrderDealReportService) ctx.getBean("orderDealReportService");
	}
	
	@Test
	public void testGetOrderDealSum() {
		OrderDealReportIn orderDealReportIn=new OrderDealReportIn();
		orderDealReportIn.setShopId(2000000156L);
		orderDealReportIn.setStartDate("2015-07-01");
		orderDealReportIn.setEndDate("2015-07-07");
		OrderDealReportOut orderDealReportOut = orderDealReportService.getOrderDealSum(orderDealReportIn);
		
		System.out.println("------------成交金额:"+orderDealReportOut.getPayPriceTotal());
		System.out.println("------------成交人数:"+orderDealReportOut.getBuyPersonNum());
		System.out.println("------------成交商品数:"+orderDealReportOut.getPayGoodsNum());
		System.out.println("------------订单数:"+orderDealReportOut.getOrderNum());
		System.out.println("------------成交转化率:"+orderDealReportOut.getPayConversion());
		
	}

	@Test
	public void testGetOrderDealListByPager() {
		
		OrderDealReportIn orderDealReportIn=new OrderDealReportIn();
		orderDealReportIn.setShopId(2000000156L);
		orderDealReportIn.setDateFormat("yyyy-MM-dd");
		orderDealReportIn.setStartDate("20150701");
		orderDealReportIn.setStartDate("20150708");
		
		Pager<OrderDealReportOut> pager=new Pager<OrderDealReportOut>();
		pager.setPage(1);
		//pager.setRows(5);
		DataGrid<OrderDealReportOut> dataGrid = orderDealReportService.getOrderDealListByPager(orderDealReportIn, pager);
		System.out.println("------------"+dataGrid.getRows());
		System.out.println("------------"+dataGrid.getTotal());
	}

	@Test
	public void testGetOrderDealListByLion() throws Exception {
		OrderDealReportIn orderDealReportIn=new OrderDealReportIn();
		orderDealReportIn.setShopId(2000000157L);
		orderDealReportIn.setStartDate("20150705");
		orderDealReportIn.setEndDate("20150707");
		orderDealReportIn.setDayInterval(7);
		DataGrid<OrderDealReportOut> dataGrid = orderDealReportService.getOrderDealListByLion(orderDealReportIn);
		System.out.println("------------"+dataGrid.getRows().toString());
	}

}
