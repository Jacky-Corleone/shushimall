package com.camelot.sattlecenter;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;



/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class SattleCatExpenseTest {
    
	private SattleCatExpenseExportService sattleCatExpenseExportService;
	private DualAccountInfoService dualAccountInfoService;
	private ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			sattleCatExpenseExportService = (SattleCatExpenseExportService) ctx.getBean("sattleCatExpenseExportService");
		}
   @SuppressWarnings("rawtypes")
@Test
   public void queryListTest(){
	SettleCatExpenseDTO dto=new SettleCatExpenseDTO();
	//dto.setId(2L);
	dto.setCategoryId(389l);
	Pager pager=new Pager();
	ExecuteResult<DataGrid<SettleCatExpenseDTO>> result=sattleCatExpenseExportService.queryCategoryExpenseList(dto, pager);
	Assert.assertEquals(true, result.isSuccess());
   }
   
   @Test
   public void queryByIdsTest(){
	  Long[] ids={1L,2L};
	  ExecuteResult<List<SettleCatExpenseDTO>> result=sattleCatExpenseExportService.queryByIds(ids);
	  Assert.assertEquals(true, result.isSuccess());
   }
   
   @Test
   public void deleteTest(){
	long id=1L;
	 ExecuteResult<String> result=sattleCatExpenseExportService.deleteById(id);
	 Assert.assertEquals(true, result.isSuccess());
   }
   
   @Test
   public void modifyTest(){
	  SettleCatExpenseDTO dto=new SettleCatExpenseDTO();
	  dto.setId(2L);
	  //dto.setServiceFee(20);
	  dto.setCashDeposit(new BigDecimal(77));
	  ExecuteResult<String> result=sattleCatExpenseExportService.modifyCategoryExpense(dto);  
	  Assert.assertEquals(true, result.isSuccess());
   }
}
