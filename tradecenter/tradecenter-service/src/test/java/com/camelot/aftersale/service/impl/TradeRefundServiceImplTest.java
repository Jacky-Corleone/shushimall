package com.camelot.aftersale.service.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.service.TradeRefundService;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.job.ConfirmSettleJob;


public class TradeRefundServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeRefundServiceImplTest.class);
    private ApplicationContext ctx;
    private TradeRefundService tradeRefundService;
    private ConfirmSettleJob confirmSettleJob;
    @Before
    public void setUp() throws Exception {
    	 ctx = new ClassPathXmlApplicationContext("test.xml");
    	 tradeRefundService = (TradeRefundService) ctx.getBean("tradeRefundService");
    	 confirmSettleJob= (ConfirmSettleJob) ctx.getBean("confirmSettleJob");
    }
    /**
   	 * @throws Exception 
   	 * 
   	 */
   	@Test
   	public void testaaa() throws Exception {
   		confirmSettleJob.collateSettleDetail();
   	}
   	
    /**
   	 * @throws Exception 
   	 * 
   	 */
   	@Test
   	public void testrefundApply() throws Exception {
   		RefundPayParam refundPayParam =new RefundPayParam();
   		refundPayParam.setOrderNo("26");
   		refundPayParam.setOrderLabel("测试2");
   		refundPayParam.setBuyerId(1000000188l);
   		refundPayParam.setReproNo("112");
   		refundPayParam.setRefundAmount(new BigDecimal("0.3"));
   		refundPayParam.setOrderPayBank(PayBankEnum.CB.getQrCode());
   		ExecuteResult<RefundPayParam> executeResult = tradeRefundService.refundApply(refundPayParam);
   		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
   	}
   	
   	/**
   	 * @throws Exception 
   	 * 
   	 */
   	@Test
   	public void testRefundDeal() throws Exception {
   		RefundPayParam refundPayParam =new RefundPayParam();
   		refundPayParam.setReproNo("2000020160108000433");
   		refundPayParam.setOrderNo("201601080129401");
   		refundPayParam.setItemId(1000000180L);
   		ExecuteResult<String> executeResult = tradeRefundService.refundDeal(refundPayParam,true,true,true);
   		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(), JSONObject.toJSONString(executeResult));
   	}
   	
   	/**
   	 * @throws Exception 
   	 * 
   	 */
   	@Test
   	public void testfindRefInfoById() {
   		ExecuteResult<RefundPayParam> executeResult = tradeRefundService.findRefInfoByRefNo("1");
   		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult.getResult()));
   	}
   	
   	/**
   	 * @throws Exception 
   	 * 
   	 */
   	@Test
   	public void testfindRefInfoByCondition() {
   		RefundPayParam refundPayParam =new RefundPayParam();
   		refundPayParam.setId(2l);
   		refundPayParam.setBuyerId(33l);
   		DataGrid<RefundPayParam> executeResult = tradeRefundService.findRefInfoByCondition(refundPayParam, null);
   		LOGGER.info("\n 操作结果{},信息{}", "",	JSONObject.toJSONString(executeResult));
   	}
}