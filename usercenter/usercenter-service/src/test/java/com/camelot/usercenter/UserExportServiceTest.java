package com.camelot.usercenter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserAuditLogService;
import com.camelot.usercenter.service.UserExportService;


public class UserExportServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserExportServiceTest.class);
	ApplicationContext ctx = null;
	UserExportService userExportService = null;
	UserAuditLogService userAuditLogService=null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userExportService = (UserExportService) ctx.getBean("userExportService");
		userAuditLogService=(UserAuditLogService) ctx.getBean("userAuditLogService");
	}

    @Ignore
	public void addUserTest() {
		RegisterInfoDTO dto = new RegisterInfoDTO();
		dto.setLoginname("sun");
		dto.setLoginpwd("221TTT");
		dto.setUserEmail("1dfsf31@qq.com");
		dto.setNickName("yyyyyyyyyyyy");
        dto.setDepartment(UserEnums.DepartMent.CGB);
        dto.setLinkPhoneNum("45445");
        Long uid=userExportService.registerUser(dto);
		LOGGER.info("操作方法{}，结果信息{}","modify", JSONObject.toJSONString(dto));
	}
//	
    @Ignore
	public void testLogin(){
        ExecuteResult<LoginResDTO>  res=userExportService.login("allen2288", "dc483e80a7a0bd9ef71d8cf973673924", "ggg");
        Assert.assertNotNull(res);
        LOGGER.info(JSON.toJSONString(res));
	}

    @Ignore
    public  void frozenUser(){
        ExecuteResult<String> res=userExportService.frozenUser("1000000182", CommonEnums.FrozenStatus.FROZEN);
        Assert.assertNotNull(res);
    }
//	@Test
//	public void testModifyPw(){
//		userExportService.modifyUserPw("hh", "322", null);
//	}
//	
//	@Test
//	public void testToken(){
//		LoginResDTO dto = userExportService.login("hh", "a22222");
//		System.out.println(dto.getNickname()+"=="+dto.getToken());
//	}
//	
//	@Test
//	public void testGetToken(){
////		RegisterDTO dto = (RegisterDTO) userExportService.getUserByRedis("654a4e91-4306-4bbb-84eb-035fa95bbd78");
////		String s = userExportService.getValueByRedis("8x1znaaypayz1pxk8tmjs9pm6");
////		System.out.println(dto.getLoginname());
//	}
//	
	@Ignore
	public void testSaveVerifyCodeToRedis(){
		userExportService.saveVerifyCodeToRedis("hh", "1234");
	}
	
//	@SuppressWarnings("rawtypes")
    @Ignore
	public void testFindUserListByCondition(){
		
		UserDTO userDTO =new UserDTO();
//		userDTO.setUid(34l);
//		userDTO.setAuditStatus(5);
//		userDTO.setCompanyName("aaaa");
//		userDTO.setShopId(38L);
		Pager pager =new Pager();
        userDTO.setNickname("test01卖家");
		DataGrid<UserDTO> dataGrid=userExportService.findUserListByCondition(userDTO, null, pager);

		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(dataGrid));
	}
	
	@Ignore
	public void testmodifyAuditStatusByUId(){
//		ExecuteResult<String> dataGrid=userExportService.modifyAuditStatusByUId(46l, ComStatus.PASS);
//		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(dataGrid));
	}
	
	@Ignore
	public void testmodifyPayStatusByUId(){
//		ExecuteResult<String> dataGrid=userExportService.modifyPayStatusByUId(46l, ComStatus.PASS);
//		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(dataGrid));
	}
	@Test
	public void testmodifyUserinfo(){
		UserDTO userDTO=new UserDTO();
		userDTO.setUid(1000001080L);
		userDTO.setUserstatus(1);
		List l = new ArrayList<Long>();
		l.add(1000001080L);
		userDTO.setUidList(l);
		boolean res=userExportService.modifyUserInfo(userDTO);
		Assert.assertEquals(true,res);

		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}
	//审核
    @Ignore
	public void testmodifyUserAudit(){
		UserDTO u=new UserDTO();
		u.setUid(9999999L);
		u.setUserstatus(6);
		u.setAuditStatus(2);
		u.setUsertype(3);
		ExecuteResult<UserDTO> res=userExportService.modifyUserAuditStatusByUserIdAndAuditId(u,"1","2222222222");
		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}

    //快速审核
    @Ignore
    public void quickAudti(){
        UserDTO u=new UserDTO();
        u.setUid(1000000201L);
        u.setUserstatus(4);
        u.setAuditStatus(2);
        u.setUsertype(2);
        ExecuteResult<UserDTO> res=userExportService.quickAuditUser(u, "1", "2222222222");
        LOGGER.info("操作方法{}，结果信息{}","quickAudti", JSONObject.toJSONString(res));
    }
	@Ignore
	public void test(){
		UserAuditLogDTO u=new UserAuditLogDTO();
		u.setAuditId(1L);
		u.setAuditDate(new Date());
		u.setAuditLogType("1");
		u.setCreateDt(new Date());
		u.setCreateDt(new Date());
		u.setDeletedFlag("1");
		u.setLastUpdDt(new Date());
		u.setRemark("     ");
		u.setUserId(22L);
		ExecuteResult<UserAuditLogDTO> res=userAuditLogService.saveUserAuditLogDTO(u);
		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}
	
	@Ignore
	public void testModifyUserInfoAndAuditStatus(){
		UserDTO userDTO=new UserDTO();
		userDTO.setUid(38L);
		userDTO.setUsertype(2);
		userDTO.setAuditStatus(2);
		ExecuteResult<String> res=userExportService.modifyUserInfoAndAuditStatus(userDTO);
		Assert.assertNotNull(res);
		
	}
	@Ignore
	public void test_queryUserById(){
		UserDTO u=userExportService.queryUserById(266L);
		Assert.assertNotNull(u);
	}
	
	@Ignore
	public void test_findUserListByUserIds(){
		List<String> testList=new ArrayList<String>();
		testList.add("34");
		testList.add("37");
		ExecuteResult<List<UserDTO>> u=userExportService.findUserListByUserIds(testList);
		Assert.assertNotNull(u);
	}
	
//	@Test
//	public void test_Audit(){
//		UserDTO userDTO=new UserDTO();
//		userDTO.setUid(200L);
//		userDTO.setUsertype(2);
//		boolean res=userExportService.modifyUserAuditStatusByUserIdAndAuditId(userDto, auditUserId, remark)(userDTO);
//		Assert.assertEquals(true,res);
//		 LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
//	}
	@Ignore
	public void test_modifyPaypwdById(){
		UserDTO userDTO=new UserDTO();
		userDTO.setUid(200L);

		ExecuteResult<String>  res;
        res = userExportService.modifyPaypwdById(432L,"96e79218965eb72c92a549dd5a330112","96e79218965eb72c92a549dd5a330112","3");
        LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}
	@Ignore
	public void test_finduserByid(){
		

		UserDTO  res=userExportService.queryUserById(487L);
		LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}
	@Ignore
	public void test_restPwdByid(){
		

		ExecuteResult<String> res=userExportService.resetUserPassword(1000788L, "111111");
		LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
	}
    @Ignore
    public void test_queryByConditon(){
        UserDTO userDTO=new UserDTO();
        List<String> idList=new ArrayList<String>();
        idList.add("650");
        DataGrid<UserDTO> res= userExportService.queryUserListByCondition(userDTO, UserType.SELLER, idList, new Pager());
        LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(res));
    }
    @Test
    public void test_verifyRegisterName(){
        boolean ret = userExportService.verifyRegisterName("qiye02");
        LOGGER.info("操作方法{}，结果信息{}","test_verifyRegisterName", JSONObject.toJSONString(ret));
    }


	//审核
	@Test
	public void modifyUserInfoByMobile(){
		UserDTO u=new UserDTO();
		u.setUid((long) 1000000891);
		u.setUmobile("");
		 boolean res=userExportService.modifyUserInfoByMobile(u);
		LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", res);
	}
	
	@Test
	public void testValidatePayPassword(){
		ExecuteResult<String> executeResult = userExportService.validatePayPassword(1000000856, encipher("a123456"));
		LOGGER.info(executeResult.getResult() + "---" + executeResult.getResultMessage());
	}
	
	private static String encipher(String str) {
        MessageDigest messageDigest = null;  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {
        	LOGGER.info("异常信息：{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
        	LOGGER.info("异常信息：{}", e.getMessage());
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
}
