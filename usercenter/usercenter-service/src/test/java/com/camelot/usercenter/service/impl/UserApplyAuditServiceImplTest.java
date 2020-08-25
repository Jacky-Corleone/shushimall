package com.camelot.usercenter.service.impl;

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
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.contract.UserApplyAuditInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserApplyAuditService;


public class UserApplyAuditServiceImplTest  {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserApplyAuditServiceImplTest.class);
	ApplicationContext ctx = null;
	UserApplyAuditService userApplyAuditService = null;
	UserApplyAuditInfoDTO userApplyAuditInfoDTO= null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userApplyAuditService = (UserApplyAuditService) ctx.getBean("userApplyAuditService");
		
	}
	
	@Ignore
	public void testFindUserAuditListByCondition() {
		Pager pager =new Pager();
		DataGrid<UserModifyInfoDTO> dataGrid=userApplyAuditService.findUserAuditListByCondition(null, pager);
		LOGGER.info("操作方法{}，结果信息{}","testFindUserAuditListByCondition", JSONObject.toJSONString(dataGrid));
		Assert.assertNotNull(dataGrid);
		
	}
//	@Test
//	public void testFindUserModifyDetailByCondition() {
//		Pager pager =new Pager();
//		UserModifyInfoDTO userModifyInfoDTO=new UserModifyInfoDTO();
//		userModifyInfoDTO.setId(1L);
//		DataGrid<UserModifyDetailDTO> dataGrid=userApplyAuditService.findUserModifyDetailByCondition(userModifyInfoDTO, pager);
//		
//		LOGGER.info("操作方法{}，结果信息{}","testFindUserAuditListByCondition", JSONObject.toJSONString(dataGrid));
//		Assert.assertNotNull(dataGrid);
//		
//	}
	
	@Test
	public void testMoifyUserApplyAudit() {
		
		// ExecuteResult<Integer> res=userApplyAuditService.moifyUserApplyAudit("199",1L,ComStatus.PASS,"通过");
		
		//LOGGER.info("操作方法{}，结果信息{}","testFindUserAuditListByCondition", JSONObject.toJSONString(res));
	//	Assert.assertNotNull(res);
		
	}
}
