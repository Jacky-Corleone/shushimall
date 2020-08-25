package com.camelot.usercenter;

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
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.indto.UserCreditAddIn;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserCreditExportService;
import com.camelot.usercenter.service.UserExtendsService;


public class UserCreditExportServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCreditExportServiceTest.class);
	ApplicationContext ctx = null;
	UserCreditExportService userCreditExportService = null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userCreditExportService = (UserCreditExportService) ctx.getBean("userCreditExportService");
	}
	@Test
	public void getUserCreditByUserIdTest(){
		ExecuteResult<UserCreditDTO> result = userCreditExportService.getUserCreditByUserId(1l);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void addUserCreditTest(){
		UserCreditAddIn uc=new UserCreditAddIn();
		uc.setUserId(1l);
		uc.setSorceType(1);
		uc.setCredit(5l);
		uc.setDescription("购买商品");
		ExecuteResult<String> result = userCreditExportService.addUserCredit(uc);
		Assert.assertEquals(true, result.isSuccess());
	}
	
}
