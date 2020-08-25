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
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserCreditLogDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserCreditLogExportService;
import com.camelot.usercenter.service.UserExtendsService;


public class UserCreditLogExportServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCreditLogExportServiceTest.class);
	ApplicationContext ctx = null;
	UserCreditLogExportService userCreditLogExportService = null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userCreditLogExportService = (UserCreditLogExportService) ctx.getBean("userCreditLogExportService");
	}
	
	@Test
	public void queryUserCreditLogListTest(){
		Pager page=new Pager();
		ExecuteResult<DataGrid<UserCreditLogDTO>> result = userCreditLogExportService.queryUserCreditLogList(1l, page);
		Assert.assertEquals(true, result.isSuccess());
	}
}
