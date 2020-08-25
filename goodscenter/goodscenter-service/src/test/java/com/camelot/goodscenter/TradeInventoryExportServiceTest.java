package com.camelot.goodscenter;



import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.InventoryModifyDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TradeInventoryExportServiceTest {

	ApplicationContext ctx = null;
	private TradeInventoryExportService tradeInventoryExportService = null; 
	private static final Logger logger = LoggerFactory.getLogger(TradeInventoryExportServiceTest.class);
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		tradeInventoryExportService = (TradeInventoryExportService) ctx.getBean("tradeInventoryExportService");
	}
	
	@Test
	public void queryBySkuIdTest(){
	 ExecuteResult<TradeInventoryDTO> er=tradeInventoryExportService.queryTradeInventoryBySkuId(7L);
	 TradeInventoryDTO t=er.getResult();
	 System.out.println(t.getTotalInventory());
	 System.out.println(t.getModified());
	 System.out.println(t.getCreateUser());
	}
	@Test
	public void queryTest(){
      TradeInventoryInDTO dto=new TradeInventoryInDTO();
      dto.setShopId(2000000305L);
	  Pager page=new Pager();
	 ExecuteResult<DataGrid<TradeInventoryOutDTO>> er=tradeInventoryExportService.queryTradeInventoryList(dto, page);
	 logger.info(JSON.toJSONString(er));
	}
	
	@Test
	public void modifyTest(){
		List<InventoryModifyDTO> dtoList=new ArrayList<InventoryModifyDTO>();
		InventoryModifyDTO dto = new InventoryModifyDTO();
		dto.setSkuId(1000000715L);
		dto.setTotalInventory(200);
		dtoList.add(dto);
		dto = new InventoryModifyDTO();
		dto.setSkuId(1000000716L);
		dto.setTotalInventory(200);
		dtoList.add(dto);
		dto = new InventoryModifyDTO();
		dto.setSkuId(1000000717L);
		dto.setTotalInventory(200);
		dtoList.add(dto);
		ExecuteResult<String> er=tradeInventoryExportService.modifyInventoryByIds(dtoList);
	    Assert.assertEquals(true, er.isSuccess());
	}
}
