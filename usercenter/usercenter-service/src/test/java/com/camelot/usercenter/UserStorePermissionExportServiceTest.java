package com.camelot.usercenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.UserPermissionDTO;
import com.camelot.usercenter.dto.indto.StoreUserResourceInDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserStorePermissionExportService;


public class UserStorePermissionExportServiceTest {
	ApplicationContext ctx = null;
	UserStorePermissionExportService userStorePermissionExportService = null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userStorePermissionExportService = (UserStorePermissionExportService) ctx.getBean("userStorePermissionExportService");
	}

	@Test
	public void queryParentResourceListTest(){
		//sss
		ExecuteResult<List<UserMallResourceDTO>> result = userStorePermissionExportService.queryParentResourceList(2,2);
		System.out.println("result----------"+JSON.toJSONString(result.getResult()));
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void addStoreUserResourceTest(){
		StoreUserResourceInDTO storeUserResourceInDTO=new StoreUserResourceInDTO();
		storeUserResourceInDTO.setParentId(73l);
		storeUserResourceInDTO.setShopId(1l);
		Integer[] resourceIds=new Integer[]{5};
		storeUserResourceInDTO.setResourceIds(resourceIds);
		storeUserResourceInDTO.setPassword("password");
		storeUserResourceInDTO.setUsername("18678923032");
		ExecuteResult<String> result = userStorePermissionExportService.addStoreUserResource(storeUserResourceInDTO);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	
	
	@Test
	public void queryChildUserListTest(){
		//sss
		Pager pager=new Pager();
		ExecuteResult<DataGrid<ChildUserDTO>> result = userStorePermissionExportService.queryChildUserList(291l,2,pager);
		System.out.println("result--------------"+JSON.toJSONString(result.getResult()));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void getChildUserTest(){
		//h
		ExecuteResult<ChildUserDTO> result = userStorePermissionExportService.getChildUser(291l,2);
		System.out.println("result--------------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryUserMallResourceByIdTest(){
		
		ExecuteResult<List<UserMallResourceDTO>> result = userStorePermissionExportService.queryUserMallResourceById(495l,2);
		System.out.println("result--------------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void modifyUserResourceByIdTest(){
		UserPermissionDTO userPermissionDTO=new UserPermissionDTO();
		userPermissionDTO.setUserId(1000000128l);
		Integer[] ids=new Integer[]{1,3,4,5,12};
		userPermissionDTO.setResourceIds(ids);
		userPermissionDTO.setShopId(2000000242l);
		userPermissionDTO.setUserName("lele");
		ExecuteResult<String> result = userStorePermissionExportService.modifyUserResourceById(userPermissionDTO);
		System.out.println("result--------------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void deleteUserByIdTest(){
		ExecuteResult<String> result = userStorePermissionExportService.deleteUserById(1,453l);
		Assert.assertEquals(true, result.isSuccess());
	}
}
