package com.camelot.aftersale;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ComplainServiceTest{
	private static final Logger LOGGER = LoggerFactory.getLogger(ComplainServiceTest.class);
	
	private ComplainExportService complainExportService;
	private ApplicationContext ctx;
	private ComplainDTO complainDTO;
	@SuppressWarnings("rawtypes")
	private Pager page=new Pager();
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		complainExportService = (ComplainExportService) ctx.getBean("complainExportService");
	}
	
	@Test
	public void testaddComplainInfo(){
		complainDTO=new ComplainDTO();
		complainDTO.setOrderId("22233");
		complainDTO.setShopName("aaa");
        complainDTO.setReturnGoodsId(222222L);
        complainDTO.setComplainPicUrl("ttttttt");
        complainDTO.setComplainPhone("32423432432");
		ExecuteResult<ComplainDTO> executeResult = complainExportService.addComplainInfo(complainDTO);
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
	}
	
	@Test
	public void testmodifyComplainInfo(){
		complainDTO=new ComplainDTO();
		complainDTO.setOrderId("222");
		ExecuteResult<String> executeResult = complainExportService.modifyComplainInfo(complainDTO);
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
	}
	
	@Test
	public void testfindInfoById(){
		ExecuteResult<ComplainDTO> executeResult = complainExportService.findInfoById(4l);
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
	}
	@Test
	public void testfindEarlyComplainInfoByCondition(){
		ComplainDTO complainDTO = new ComplainDTO();
		List<Long> buyIdList = new ArrayList<Long>();
		buyIdList.add(1000001260l);
		complainDTO.setBuyIdList(buyIdList);
		DataGrid<ComplainDTO> dg = complainExportService.findEarlyComplainInfoByCondition(complainDTO,null);
		LOGGER.info("操作结果{}", JSON.toJSON(dg));
	}
	
	
	@Test
	public void testfindInfoByCondition(){
		
		complainDTO=new ComplainDTO();
//		complainDTO.setOrderId(222l);
		DataGrid<ComplainDTO> executeResult = complainExportService.findInfoByCondition(complainDTO,page);
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
