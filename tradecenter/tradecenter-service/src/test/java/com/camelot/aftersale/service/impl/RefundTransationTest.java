package com.camelot.aftersale.service.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dto.RefundTransationsDTO;
import com.camelot.aftersale.service.RefundTransationsService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

public class RefundTransationTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundTransationTest.class);
    private ApplicationContext ctx;
    private RefundTransationsService refundTransationsService;
    @Before
    public void setUp() throws Exception {
    	 ctx = new ClassPathXmlApplicationContext("test.xml");
    	 refundTransationsService = (RefundTransationsService) ctx.getBean("refundTransationsService");
    }
    
    @Test
    public void testCreateRefundTransationsDTO(){
    	RefundTransationsDTO refundTransationsDTO=new RefundTransationsDTO();
    	refundTransationsDTO.setOutTradeNo("30120150804003399");
    	refundTransationsDTO.setOrderNo("201508040161501");
    	refundTransationsDTO.setCodeNo("2000020150805000340");
    	refundTransationsDTO.setTotalAmount(new BigDecimal(0.05));
    	refundTransationsDTO.setRefundAmount(new BigDecimal(0.03));
    	refundTransationsDTO.setPayBank("WX");
    	refundTransationsDTO.setState(1);
//    	refundTransationsDTO.setInsertBy(2312321);
    	//String refundOutNo=refundTransationsService.getRefundOutTradeNo(refundTransationsDTO.getPayBank());
    	//refundTransationsDTO.setOutRefundNo(refundOutNo);
    	ExecuteResult<RefundTransationsDTO> resout=refundTransationsService.createRefundTransationsDTO(refundTransationsDTO);
    	LOGGER.info("\n 操作结果{},信息{}", resout.isSuccess(),	JSONObject.toJSONString(resout));
    }
    @Test
    public void testUpdateRefundTransationsDTO(){
    	RefundTransationsDTO refundTransationsDTO=new RefundTransationsDTO();
    	refundTransationsDTO.setOutRefundNo("10120151015000000");
//    	refundTransationsDTO.setInsertBy(0);
    	refundTransationsDTO.setPayBank("wx");
    	ExecuteResult<String> res=refundTransationsService.updateRefundTransationsDTO(refundTransationsDTO);
    	LOGGER.info("\n 操作结果{},信息{}", res.isSuccess(),	JSONObject.toJSONString(res));
    }
    
    @Test
    public void testQueryRefundTransationByRefundNo(){
    	ExecuteResult<RefundTransationsDTO> res=refundTransationsService.queryRefundTransationByRefundNo("10120151015000000");
    }
    @Test
    public void testSearchRefundTransationsDTO(){
    	RefundTransationsDTO refundTransationsDTO=new RefundTransationsDTO();
    	refundTransationsDTO.setPayBank("wx");
    	DataGrid<RefundTransationsDTO> dg=refundTransationsService.searchRefundTransationsDTO(null, refundTransationsDTO);
    	LOGGER.info("\n 操作结果{},信息{}", dg.getRows(),	JSONObject.toJSONString(dg));
    
    }
    
}
