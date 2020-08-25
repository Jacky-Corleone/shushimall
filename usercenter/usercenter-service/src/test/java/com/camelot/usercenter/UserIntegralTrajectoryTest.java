package com.camelot.usercenter;

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
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;


public class UserIntegralTrajectoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserIntegralTrajectoryTest.class);
	ApplicationContext ctx = null;
	UserIntegralTrajectoryExportService userIntegralTrajectoryService = null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userIntegralTrajectoryService = (UserIntegralTrajectoryExportService) ctx.getBean("userIntegralTrajectoryService");
	}
	
	@Test
	public void testInsertIntegralTrajectory(){
		UserIntegralTrajectoryDTO trajectoryDTO = new UserIntegralTrajectoryDTO();
		trajectoryDTO.setIntegralType(1);
		ExecuteResult<UserIntegralTrajectoryDTO> er = userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(trajectoryDTO);
		Assert.assertTrue(er.isSuccess());
	}
	@Test
	public void testUpdateIntegralTrajectory(){
		UserIntegralTrajectoryDTO trajectoryDTO = new UserIntegralTrajectoryDTO();
		trajectoryDTO.setIntegralType(3);
		trajectoryDTO.setId(1L);
		ExecuteResult<Boolean> er = userIntegralTrajectoryService.updateUserIntegralTrajectoryDTO(trajectoryDTO);
		Assert.assertTrue(er.isSuccess());
	}
	@Test
	public void testQueryIntegralTrajectory(){
		UserIntegralTrajectoryDTO trajectoryDTO = new UserIntegralTrajectoryDTO();
		trajectoryDTO.setIntegralType(3);
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = userIntegralTrajectoryService.queryUserIntegralTrajectoryDTO(trajectoryDTO,null);
		LOGGER.info(JSON.toJSONString(er));
		Assert.assertTrue(er.isSuccess());
	}
	
	@Test
	public void dubboStartTest(){
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryTotalIntegral(){
		ExecuteResult<Long> total = userIntegralTrajectoryService.queryTotalIntegral(1000000005L);
		LOGGER.info(JSON.toJSONString(total));
		Assert.assertTrue(total.isSuccess());
	}
	
	@Test
	public void testQueryUserIntegralByType(){
		UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
		userIntegralTrajectoryDTO.setIntegralType(1);
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = userIntegralTrajectoryService.queryUserIntegralByType(userIntegralTrajectoryDTO, null);
		LOGGER.info(JSON.toJSONString(er));
		Assert.assertTrue(er.isSuccess());
	}
}
