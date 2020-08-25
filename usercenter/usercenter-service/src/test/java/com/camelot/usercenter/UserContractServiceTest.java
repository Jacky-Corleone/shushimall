package com.camelot.usercenter;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.contract.UserContractAuditDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.service.UserContractService;


public class UserContractServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserContractServiceTest.class);
	ApplicationContext ctx = null;
	UserContractService userContractService = null;
	UserContractDTO userContractDTO= null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userContractService = (UserContractService) ctx.getBean("userContractService");
		userContractDTO=new UserContractDTO();
		
	}
	
	@Test
	public void testmodifyUserContractById() {
		userContractDTO.setId(38L);
		userContractDTO = userContractService.findUserContractById(38L,null);
		ExecuteResult<UserContractDTO> result=userContractService.modifyUserContractById(userContractDTO,"1000000762");
		LOGGER.info("操作方法{}，结果信息{}","testfindById", JSONObject.toJSONString(result));
        Assert.assertNotNull(result);
	}
	@Ignore
	public void testfindById() {
		UserContractDTO result=userContractService.findUserContractById(101l, null);
		LOGGER.info("操作方法{}，结果信息{}","testfindById", JSONObject.toJSONString(result));
	}
	
	@Ignore
	public void testfindByCondition(){

//		userContractDTO.setContractStatus();
        userContractDTO.setCreatorId(519L);
		DataGrid<UserContractDTO> dataGrid=userContractService.findListByCondition(userContractDTO, null);
		LOGGER.info("操作方法{}，结果信息{}","testfindByCondition", JSONObject.toJSONString(dataGrid));
	}
	
	@Ignore
	public void testfindAuditListByCId(){
		DataGrid<UserContractAuditDTO> dataGrid=userContractService.findAuditListByCId(115L,null);
		LOGGER.info("操作方法{}，结果信息{}","findAuditListByCId", JSONObject.toJSONString(dataGrid));
	}
	
}
