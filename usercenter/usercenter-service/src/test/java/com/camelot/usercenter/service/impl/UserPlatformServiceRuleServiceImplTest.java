package com.camelot.usercenter.service.impl;

import static org.junit.Assert.*;

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
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.domain.UserPlatformServiceRule;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.contract.UserApplyAuditInfoDTO;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserPlatformServiceRuleService;

import java.util.List;

public class UserPlatformServiceRuleServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserPlatformServiceRuleServiceImplTest.class);
	ApplicationContext ctx = null;
	UserPlatformServiceRuleService userPlatformServiceRuleService = null;
	
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userPlatformServiceRuleService = (UserPlatformServiceRuleService) ctx.getBean("userPlatformServiceRuleService");
		
		
	}
//	@Test
//	public void testGetUserPlatformServiceRuleById() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testSaveUserPlatformServiceRule() {
//		UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=new UserPlatformServiceRuleDTO();
//		userPlatformServiceRuleDTO.setCreateTime(new Date());
//		userPlatformServiceRuleDTO.setRuleId(10011L);
//		userPlatformServiceRuleDTO.setUserId(59L);
//		userPlatformServiceRuleDTO.setIsDeleted(0);
//		
//	    ExecuteResult<UserPlatformServiceRuleDTO> res=	userPlatformServiceRuleService.saveUserPlatformServiceRule(userPlatformServiceRuleDTO);
//		LOGGER.info("操作方法{}，结果信息{}","testSaveUserPlatformServiceRule", JSONObject.toJSONString(res));
//	}

//	@Test
//	public void testModifyUserPlatformServiceRule() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetUserPlatformRuleList() {
        ExecuteResult<List<UserPlatformServiceRuleDTO>> result = new ExecuteResult<List<UserPlatformServiceRuleDTO>>();
		result=userPlatformServiceRuleService.getUserPlatformRuleList(new String[]{"1000000624","1000000683"});
		LOGGER.info("操作方法{}，结果信息{}","testGetUserPlatformRuleList", JSONObject.toJSONString(result));
	}

	@Ignore
	public void testDelUserPlatformService() {
		UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=new UserPlatformServiceRuleDTO();
		userPlatformServiceRuleDTO.setUserId(59L);
		userPlatformServiceRuleDTO.setRuleId(10011L);
		ExecuteResult<Integer> res= userPlatformServiceRuleService.delUserPlatformService(userPlatformServiceRuleDTO);
		LOGGER.info("操作方法{}，结果信息{}","testDelUserPlatformService", JSONObject.toJSONString(res));
	}

	@Ignore
	public void test_getUserPlatformRuleStatistics() {
		UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=new UserPlatformServiceRuleDTO();
		userPlatformServiceRuleDTO.setUserId(59L);
		userPlatformServiceRuleDTO.setRuleId(10011L);
		UserPlatformServiceRule d=new UserPlatformServiceRule();
		
		Pager p=new Pager();
		DataGrid<UserPlatformServiceRuleDTO> res= userPlatformServiceRuleService.getUserPlatformRuleStatistics(userPlatformServiceRuleDTO, p);
		LOGGER.info("操作方法{}，结果信息{}","testDelUserPlatformService", JSONObject.toJSONString(res));
	}
	
	@Test
	public void test_getUserPlatformRuleDetail() {
		UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=new UserPlatformServiceRuleDTO();
		
		userPlatformServiceRuleDTO.setRuleId(10011L);
		
		
		Pager p=new Pager();
		DataGrid<UserPlatformServiceRuleDTO> res= userPlatformServiceRuleService.getUserPlatformRuleDetail(userPlatformServiceRuleDTO, p);
		LOGGER.info("操作方法{}，结果信息{}","testDelUserPlatformService", JSONObject.toJSONString(res));
	}
}
