package com.camelot.tradecenter;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;
import com.camelot.tradecenter.service.TradeOrderItemsPackageExportService;

/**
 * 
 * <p>Description: [套装商品订单记录测试]</p>
 * Created on 2016年2月25日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class TradeOrderItemsPackageExportServiceImplAllTest {

	private static final Logger logger = LoggerFactory.getLogger(TradeOrderItemsPackageExportServiceImplAllTest.class);

	private TradeOrderItemsPackageExportService tradeOrderItemsPackageExportService = null;
	ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeOrderItemsPackageExportService = (TradeOrderItemsPackageExportService) ctx.getBean("tradeOrderItemsPackageExportService");
	}

	/**
	 * 
	 * <p>Discription:[添加]</p>
	 * Created on 2016年2月25日
	 * @author:[宋文斌]
	 */
	@Test
	public void addTest() {
		TradeOrderItemsPackageDTO dto = new TradeOrderItemsPackageDTO();
		dto.setAddSource(9);
		dto.setCreateTime(new Date());
		dto.setItemId(8L);
		dto.setOrderId("7");
		dto.setOrderItemsId(1L);
		dto.setSkuId(2L);
		dto.setSubItemId(3L);
		dto.setSubSkuId(4L);
		dto.setUpdateTime(new Date());
		ExecuteResult<String> result = this.tradeOrderItemsPackageExportService.add(dto);
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void queryTradeOrderItemsPackageDTOsTest() {
		TradeOrderItemsPackageDTO dto = new TradeOrderItemsPackageDTO();
		ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> result = tradeOrderItemsPackageExportService.queryTradeOrderItemsPackageDTOs(dto, null);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryByIdTest() {
		ExecuteResult<TradeOrderItemsPackageDTO> result = tradeOrderItemsPackageExportService.queryById(1L);
		Assert.assertEquals(true, result.isSuccess());
	}
}
