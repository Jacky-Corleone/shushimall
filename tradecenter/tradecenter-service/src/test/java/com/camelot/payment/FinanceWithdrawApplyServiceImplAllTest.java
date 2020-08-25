package com.camelot.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;

/**
 * 平台提现日志
 * @author 周乐
 */
public class FinanceWithdrawApplyServiceImplAllTest{
	
	private static final Logger logger = LoggerFactory.getLogger(FinanceWithdrawApplyServiceImplAllTest.class);
	
	private FinanceWithdrawApplyExportService financeWithdrawApplyService = null;
	
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		financeWithdrawApplyService = (FinanceWithdrawApplyExportService) ctx.getBean("financeWithdrawApplyService");
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void queryApplyTest() throws ParseException{
		FinanceWithdrawApplyDTO entity = new FinanceWithdrawApplyDTO();
		entity.setUserId(455l);
		Pager page= new Pager();
		page.setPage(1);
		DataGrid<FinanceWithdrawApplyDTO> result = financeWithdrawApplyService.queryFinanceWithdrawByCondition(entity, page);
		
		logger.info(JSON.toJSONString(result));
//		Assert.assertEquals(true, result);
	}
	
	@Test
	public void addApplyTest() throws ParseException{
		FinanceWithdrawApplyDTO entity = new FinanceWithdrawApplyDTO();
		entity.setAmount(new BigDecimal(23));
		entity.setContent("我要提现");
		entity.setCreatedTime(new Date());
		entity.setModifiedTime(new Date());
		entity.setStatus(1);
		entity.setTradeNo("100001");
		entity.setUserId(78L);
		entity.setUserName("周乐");
		ExecuteResult<String> result = financeWithdrawApplyService.addRecord(entity);
		
		Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void updateApplyTest() throws ParseException{
		FinanceWithdrawApplyDTO entity = new FinanceWithdrawApplyDTO();
		entity.setId(1000012L);
		entity.setAmount(new BigDecimal(230));
		entity.setContent("我要提现000");
		entity.setCreatedTime(new Date());
		entity.setModifiedTime(new Date());
		entity.setStatus(2);
		entity.setTradeNo("100001000");
		entity.setUserId(78L);
		entity.setUserName("周乐00");
		ExecuteResult<String> result = financeWithdrawApplyService.updateRecord(entity);
		
		Assert.assertTrue(result.isSuccess());
	}
}
