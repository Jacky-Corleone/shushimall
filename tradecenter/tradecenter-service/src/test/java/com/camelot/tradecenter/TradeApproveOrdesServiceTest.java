package com.camelot.tradecenter;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeApprovedOrdersExportService;

public class TradeApproveOrdesServiceTest {
private static final Logger logger = LoggerFactory.getLogger(TradeApproveOrdesServiceTest.class);
	
	private TradeApprovedOrdersExportService tradeOrderExportService = null;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeOrderExportService = (TradeApprovedOrdersExportService) ctx.getBean("tradeApprovedOrdersExportService");
	}
	
	@Test
	public void testAapproveSubmit(){
		TradeOrdersDTO dto=new TradeOrdersDTO();
		dto.setOrderId("201508260185102");
		dto.setParentOrderId("2015082601851");
		dto.setApproveStatus("1");//审核通过
		tradeOrderExportService.approveSubmit(dto);
	}
}
