package com.camelot.sattlecenter;
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;



/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class SattleItemExpenseTest {
    
	private SettleItemExpenseExportService settleItemExpenseExportService;
	private ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			settleItemExpenseExportService = (SettleItemExpenseExportService) ctx.getBean("settleItemExpenseExportService");
		}
   @SuppressWarnings("rawtypes")
   @Test
   public void querySettleItemExpenseTest(){
	   SettleItemExpenseDTO sieDto=new SettleItemExpenseDTO();
	   Pager page=new Pager();
	   ExecuteResult<DataGrid<SettleItemExpenseDTO>> result = settleItemExpenseExportService.querySettleItemExpense(sieDto, page);
	   Assert.assertEquals(true, result.isSuccess());
   }
   @Test
   public void getSettleItemExpenseTest(){
	   ExecuteResult<SettleItemExpenseDTO> result = settleItemExpenseExportService.getSettleItemExpense(1l);
	   Assert.assertEquals(true, result.isSuccess());
   }
   @Test
   public void addSettleItemExpenseTest(){
	   SettleItemExpenseDTO sieDto=new SettleItemExpenseDTO();
	   sieDto.setItemId(1l);
	   sieDto.setShopId(1l);
	   sieDto.setSellerId(1l);
	   sieDto.setRebateRate(BigDecimal.valueOf(10));
	   sieDto.setServiceFee(BigDecimal.valueOf(10));
	   sieDto.setCashDeposit(BigDecimal.valueOf(10));
	   ExecuteResult<String> result = settleItemExpenseExportService.addSettleItemExpense(sieDto);
	   Assert.assertEquals(true, result.isSuccess());
   }
   @Test
   public void modifySettleItemExpenseTest(){
	   SettleItemExpenseDTO sieDto=new SettleItemExpenseDTO();
	   sieDto.setId(1l);
	   sieDto.setRebateRate(BigDecimal.valueOf(110));
	   sieDto.setServiceFee(BigDecimal.valueOf(110));
	   sieDto.setCashDeposit(BigDecimal.valueOf(110));
	   ExecuteResult<String> result = settleItemExpenseExportService.modifySettleItemExpense(sieDto);
	   Assert.assertEquals(true, result.isSuccess());
   }
   @Test
   public void getSettlementInfoTest(){
	   SettlementInfoInDTO settlementInfoInDTO=new SettlementInfoInDTO();
	   settlementInfoInDTO.setCid(388l);
	   settlementInfoInDTO.setItemId(1000000432l);
	   ExecuteResult<SettlementInfoOutDTO> result = settleItemExpenseExportService.getSettlementInfo(settlementInfoInDTO);
	   Assert.assertEquals(true, result.isSuccess());
   }
}
