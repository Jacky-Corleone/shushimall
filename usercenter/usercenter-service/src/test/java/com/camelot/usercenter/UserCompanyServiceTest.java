package com.camelot.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.device.PostPressDeviceDTO;
import com.camelot.usercenter.dto.device.PrePressDeviceDTO;
import com.camelot.usercenter.dto.device.PrintingDeviceDTO;
import com.camelot.usercenter.dto.device.UserComDeviceDTO;
import com.camelot.usercenter.enums.UserEnums.DeviceType;
import com.camelot.usercenter.service.UserCompanyService;


public class UserCompanyServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCompanyServiceTest.class);
	ApplicationContext ctx = null;
	UserCompanyService userCompanyService = null;
	UserCompanyDTO userCompanyDTO= null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userCompanyService = (UserCompanyService) ctx.getBean("userCompanyService");
		userCompanyDTO=new UserCompanyDTO();
        userCompanyDTO.setCompanyScale("tttttttttttttttt");
		userCompanyDTO.setId(151l);
        userCompanyDTO.setUserId(686l);
		
	}
	
//	@Test
//	public void saveTest() {
//		ExecuteResult<UserCompanyDTO> result=userCompanyService.saveUserCompany(userCompanyDTO);
//		LOGGER.info("操作方法{}，结果信息{}","saveTest", JSONObject.toJSONString(result));
//	}
//	
	@Test
	public void testmodifyTest() {
		ExecuteResult<UserCompanyDTO> result=userCompanyService.modifyUserCompany(userCompanyDTO);
		LOGGER.info("操作方法{}，结果信息{}","modify", JSONObject.toJSONString(result));
	}
//	
//	@Test
//	public void testFind(){
//		ExecuteResult<UserCompanyDTO> dataGrid=userCompanyService.findUserCompanyByUId(34l);
//		LOGGER.info("操作方法{}，结果信息{}","testFind", JSONObject.toJSONString(dataGrid));
//	}
//	
//	@Test
//	public void testAddComDevice() {
//		UserComDeviceDTO userComDeviceDTO = new UserComDeviceDTO();
//		
//		List<PrePressDeviceDTO> listPrintFront=new ArrayList<PrePressDeviceDTO>();
//		PrePressDeviceDTO printFrontDeviceDTO=new PrePressDeviceDTO();
//		printFrontDeviceDTO.setPrepressBrand("印前品牌");
//		printFrontDeviceDTO.setCompanyinfoId(2l);
//		listPrintFront.add(printFrontDeviceDTO);
//		userComDeviceDTO.setListPrePress(listPrintFront);
//		
//		List<PrintingDeviceDTO> listPrinting=new ArrayList<PrintingDeviceDTO>();
//		PrintingDeviceDTO printingDeviceDTO=new PrintingDeviceDTO();
//		printingDeviceDTO.setPrintingBrand("印刷品牌");
//		printingDeviceDTO.setCompanyinfoId(2l);
//		listPrinting.add(printingDeviceDTO);
//		userComDeviceDTO.setListPrinting(listPrinting);
//		
//		List<PostPressDeviceDTO> listPrintRear=new ArrayList<PostPressDeviceDTO>();
//		PostPressDeviceDTO printRearDeviceDTO=new PostPressDeviceDTO();
//		printRearDeviceDTO.setPostpressBrand("印后品牌");
//		printRearDeviceDTO.setCompanyinfoId(2l);
//		listPrintRear.add(printRearDeviceDTO);
//		userComDeviceDTO.setListPostPress(listPrintRear);
//		
//		ExecuteResult<UserComDeviceDTO> result=userCompanyService.saveComDevice(userComDeviceDTO);
//		LOGGER.info("操作方法{}，结果信息{}","testAddComDevice", JSONObject.toJSONString(result));
//	}
//	
//	@Test
//	public void testFindComDevice(){
//		ExecuteResult<UserComDeviceDTO> dataGrid=userCompanyService.findComDeviceListByUId(1l, DeviceType.PRINTING);
//		LOGGER.info("操作方法{}，结果信息{}","testFindComDevice", JSONObject.toJSONString(dataGrid));
//	}
	@Test
	public void testmodifyStatusTest() {
		ExecuteResult<Integer> result=userCompanyService.modifyUserCompanyStatus(userCompanyDTO);
		LOGGER.info("操作方法{}，结果信息{}","modify", JSONObject.toJSONString(result));
	}
}
