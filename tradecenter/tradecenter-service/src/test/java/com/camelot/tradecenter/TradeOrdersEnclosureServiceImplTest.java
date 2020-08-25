package com.camelot.tradecenter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrdersEnclosureDTO;
import com.camelot.tradecenter.service.TradeOrdersEnclosureService;

public class TradeOrdersEnclosureServiceImplTest {

	private TradeOrdersEnclosureService tradeOrdersEnclosureService = null;
	ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeOrdersEnclosureService = (TradeOrdersEnclosureService) ctx.getBean("tradeOrdersEnclosureService");
	}
	
//	@Test
//	public void addEnclosureTest() {
//		TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO = new TradeOrdersEnclosureDTO();
//		tradeOrdersEnclosureDTO.setEnclosureUrl("url------111");
//		tradeOrdersEnclosureDTO.setIsDelete(0);
//		tradeOrdersEnclosureDTO.setOrderId("23sss");
//		tradeOrdersEnclosureService.addEnclosure(tradeOrdersEnclosureDTO);
//	}
	
	@Test
	public void queryEnclosuresTest(){
		TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO = new TradeOrdersEnclosureDTO();
		tradeOrdersEnclosureDTO.setOrderId("23sss");
		tradeOrdersEnclosureDTO.setIsDelete(0);
		DataGrid<TradeOrdersEnclosureDTO> result = tradeOrdersEnclosureService.queryEnclosures(tradeOrdersEnclosureDTO, new Pager());
		System.out.println(result.getTotal());
	}
}
