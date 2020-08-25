package com.camelot.usercenter;

import com.camelot.usercenter.dto.userInfo.UserManageDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
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
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExtendsService;


public class UserExtendsServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserExtendsServiceTest.class);
	ApplicationContext ctx = null;
	UserExtendsService userExtendsService = null;
	UserInfoDTO userInfoDTO= null;
			
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userExtendsService = (UserExtendsService) ctx.getBean("userExtendsService");

//		userInfoDTO.setUserType(UserType.SELLER);
//		userInfoDTO.setUserBusinessDTO(userBusinessDTO);
	}
	
/*	@Test
	public void saveTest() {
        userInfoDTO=new UserInfoDTO();

        UserDTO userDTO=new UserDTO();
        userDTO.setUsertype(UserType.SELLER.getCode());
        userInfoDTO.setUserDTO(userDTO);
        userInfoDTO.setUserId(99999L);
        userInfoDTO.setUserType(UserType.BUYER);
        UserBusinessDTO b=new UserBusinessDTO();
        b.setBusinessLicencAddressDetail("6666666");
        b.setCompanyPeoNum(UserEnums.CompanyPeopleNum.THAN1000);
        b.setCompanyQualt(UserEnums.CompanyQualt.QT);

        UserManageDTO m=new UserManageDTO();
        m.setDealerType(UserEnums.DealerType.DL);
        m.setErpType(UserEnums.ERPType.QT);
        userInfoDTO.setUserManageDTO(m);
        userInfoDTO.setUserBusinessDTO(b);



		ExecuteResult<UserInfoDTO> result=userExtendsService.createUserExtends(userInfoDTO);
		LOGGER.info("操作方法{}，结果信息{}","saveTest", JSONObject.toJSONString(result));
	}
	@Test
	public void testmodifyTest() {
        userInfoDTO=new UserInfoDTO();
		userInfoDTO.setUserId(99999L);
		userInfoDTO.setExtendId(638L);

        UserBusinessDTO b=new UserBusinessDTO();
        b.setBusinessLicencAddressDetail("66666668888");
        b.setCompanyPeoNum(UserEnums.CompanyPeopleNum.TO49);
        b.setCompanyQualt(UserEnums.CompanyQualt.GY);
//		UserCiticDTO cti=new UserCiticDTO();
//		cti.setBuyerFinancingAccount("22222222222222222");
//		cti.setSellerFrozenAccount("5555555555555555555");
//		userInfoDTO.setIsHaveBuyerPaysAccount("1");
//		userInfoDTO.setUserCiticDTO(cti);
//		userInfoDTO.setUserBusinessDTO(userBusinessDTO);

        userInfoDTO.setUserBusinessDTO(b);

        UserManageDTO m=new UserManageDTO();
        m.setDealerType(UserEnums.DealerType.PP);
        m.setErpType(UserEnums.ERPType.DSFERP);
        userInfoDTO.setUserManageDTO(m);
		ExecuteResult<UserInfoDTO> result=userExtendsService.modifyUserExtends(userInfoDTO, UserExtendsType.CTIBANK);
		LOGGER.info("操作方法{}，结果信息{}","modify", JSONObject.toJSONString(result));
	}
	
	@Test
	public void testFind(){
		ExecuteResult<UserInfoDTO> result=userExtendsService.findUserInfo(1000001027L);
		LOGGER.info("操作方法{}，结果信息{}","testFind", JSONObject.toJSONString(result));
	}
	
	@Test
	public void testUpdateStatusByType(){
		ExecuteResult<String> dataGrid=userExtendsService.modifyStatusByType(500l, CommonEnums.ComStatus.PASS, UserExtendsType.CTIBANK);
		LOGGER.info("操作方法{}，结果信息{}","testFind", JSONObject.toJSONString(dataGrid));
	}

	@Test
	public void testfindUserInfoList(){
		userInfoDTO=new UserInfoDTO();
        userInfoDTO.setUserId(1045L);
		DataGrid<UserInfoDTO> dataGrid=userExtendsService.findUserInfoList(userInfoDTO, null);
		LOGGER.info("操作方法{}，结果信息{}","testFind", JSONObject.toJSONString(dataGrid));
	}
	
	@Test
	public void test_addUserExtend(){
		userInfoDTO=new UserInfoDTO();
		userInfoDTO.setUserType(UserType.BUYER);
		userInfoDTO.setUserId(515L);
		UserCiticDTO cti=new UserCiticDTO();
		cti.setBuyerFinancingAccount("22222222222222222");
		cti.setSellerFrozenAccount("5555555555555555555");

		userInfoDTO.setUserCiticDTO(cti);
		ExecuteResult<UserInfoDTO> res=userExtendsService.createUserExtends(userInfoDTO);
		LOGGER.info("操作方法{}，结果信息{}","testFind", JSONObject.toJSONString(res));
	}
    @Test
    public void test_somte(){
        UserBusinessDTO u=new UserBusinessDTO();
                u.setCompanyPeoNum(UserEnums.CompanyPeopleNum.THAN1000);
        System.out.println(u.getCompanyPeoNum().toString()+"======================");
    }*/
    @Test
    public void test_query(){
    	ExecuteResult<UserInfoDTO> byname = userExtendsService.queryUserExtendsByname("周乐111");
    	LOGGER.info("操作方法{}，结果信息{}","test_query", JSONObject.toJSONString(byname));
    	System.out.println("1111111111111111111");
    }
    
    
    
}
