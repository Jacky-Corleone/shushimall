package com.camelot.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

public class ActivityStatementServiceTest {

	private final static Logger logger = LoggerFactory.getLogger(ActivityStatementServiceTest.class);
    private ActivityStatementSerice activityStatementSerice;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		activityStatementSerice = (ActivityStatementSerice) ctx.getBean("activityStatementSerice");
	}
	
	@Test
	public void testAddActivityStatement(){
		ActivityStatementsDTO dto=new ActivityStatementsDTO();
		dto.setOrderId("11111");
		dto.setShopId(2000050555l);
		dto.setState(2);
		dto.setTotalDiscountAmount(new BigDecimal(5));
		dto.setTotalRefundAmount(new BigDecimal(2));
		ExecuteResult<String> result=activityStatementSerice.addActivityStatement(dto);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryActivityStatementsByOrderId(){
		ExecuteResult<ActivityStatementsDTO> result=activityStatementSerice.queryActivityStatementsByOrderId("11111");
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testUpdateActivityStatement(){
		ActivityStatementsDTO dto=new ActivityStatementsDTO();
		dto.setOrderId("11111");
		dto.setShopId(111111l);
		dto.setState(2);
		ExecuteResult<String> result=activityStatementSerice.updateActivityStatement(dto);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryActivityStatementsDTO(){
		ActivityStatementsDTO dto=new ActivityStatementsDTO();
		//dto.setOrderId("11111");
//		dto.setShopId(111111L);
//		dto.setState(1);
		List<Long> shopIds = new ArrayList<Long>();
		shopIds.add(111111L);
		shopIds.add(22222L);
		dto.setShopIds(shopIds);
		ExecuteResult<DataGrid<ActivityStatementsDTO>> result=activityStatementSerice.queryActivityStatementsDTO(dto, null);
		logger.info(JSONObject.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	
	@Test
	public void testAddActivityRecord(){
		ActivityRecordDTO dto=new ActivityRecordDTO();
		dto.setOrderId("22222");
		dto.setPromotionId("2");
		dto.setShopId(3l);
		dto.setType(ActivityTypeEnum.PLATFORMCOUPONS.getStatus());
		ExecuteResult<String> result=activityStatementSerice.addActivityRecord(dto);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryActivityRecordDTO(){
		ActivityRecordDTO dto=new ActivityRecordDTO();
		dto.setOrderId("22222");
		ExecuteResult<DataGrid<ActivityRecordDTO>> result=activityStatementSerice.queryActivityRecordDTO(dto,null);
		Assert.assertEquals(true, result.isSuccess());
	}
}
