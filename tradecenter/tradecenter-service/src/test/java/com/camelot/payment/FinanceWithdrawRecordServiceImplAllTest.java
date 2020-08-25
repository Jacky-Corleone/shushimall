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
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;

/**
 * 平台提现日志
 * @author 周乐
 */
public class FinanceWithdrawRecordServiceImplAllTest{
	
	private static final Logger logger = LoggerFactory.getLogger(FinanceWithdrawRecordServiceImplAllTest.class);
	
	private FinanceWithdrawRecordExportService financeWithdrawRecordService = null;
	
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		financeWithdrawRecordService = (FinanceWithdrawRecordExportService) ctx.getBean("financeWithdrawRecordService");
	}
	
	@Test
	public void queryRecordTest() throws ParseException{
		FinanceWithdrawRecordDTO entity = new FinanceWithdrawRecordDTO();
		DataGrid<FinanceWithdrawRecordDTO> result = financeWithdrawRecordService.queryFinanceWithdrawByCondition(entity, null);
		
		logger.info(JSON.toJSONString(result));
//		Assert.assertEquals(true, result);
	}
	
	@Test
	public void addRecordTest() throws ParseException{
		FinanceWithdrawRecordDTO entity = new FinanceWithdrawRecordDTO();
		entity.setCreatedTime(new Date());
		entity.setAmount(new BigDecimal(230000));
		entity.setModifiedTime(new Date());
		entity.setStatus(1);
		entity.setTradeNo("001100");
		entity.setType(1);
		entity.setUserId(76L);
		
		ExecuteResult<String> result = financeWithdrawRecordService.addRecord(entity);
		Assert.assertTrue(result.isSuccess());
	}
}
