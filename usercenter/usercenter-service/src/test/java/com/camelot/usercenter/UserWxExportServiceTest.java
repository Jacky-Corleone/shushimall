package com.camelot.usercenter;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserWxDTO;
import com.camelot.usercenter.service.UserWxExportService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserWxExportServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserWxExportServiceTest.class);
	ApplicationContext ctx = null;
	UserWxExportService userWxExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userWxExportService = (UserWxExportService) ctx.getBean("userWxExportService");
	}

	@Test
	public void test_bindingWX(){
		UserWxDTO userDTO=new UserWxDTO();
		userDTO.setWxopenid("o9OAtwZTyDSxEpY32bo4FJrT4RL8");
		userDTO.setUid(1000000620L);
		ExecuteResult<UserWxDTO> executeResult = userWxExportService.bindingWX(userDTO);
		LOGGER.info("操作方法{}，结果信息{}","test_bindingWX", JSONObject.toJSONString(executeResult));
	}
	@Test
	public void test_getUserInfoByOpenId(){
		UserWxDTO userDTO=new UserWxDTO();
		userDTO.setWxopenid("oX7xRuBsu1hTM9kIy6MydDCiTB-A");
		ExecuteResult<UserWxDTO> executeResult = userWxExportService.getUserInfoByOpenId(userDTO);
		LOGGER.info("操作方法{}，结果信息{}","test_getUserInfoByOpenId", JSONObject.toJSONString(executeResult));
	}

	@Test
	public void queryUser(){
		ExecuteResult<DataGrid<UserDTO>> executeResult = userWxExportService.queryUser(new UserDTO(), new Pager());
		LOGGER.info("操作方法{}，结果信息{}","test_getUserInfoByOpenId", JSONObject.toJSONString(executeResult));
	}
	@Test
	public void findUserListByUserIds(){
		List<String> idList=new ArrayList<String>();
		idList.add("1000000853");
		ExecuteResult<DataGrid<Map>> executeResult = userWxExportService.findUserListByUserIds(idList);
		LOGGER.info("操作方法{}，结果信息{}","test_getUserInfoByOpenId", JSONObject.toJSONString(executeResult));
	}
}
