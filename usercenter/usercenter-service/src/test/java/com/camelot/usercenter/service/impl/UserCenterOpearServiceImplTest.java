package com.camelot.usercenter.service.impl;

import static org.junit.Assert.*;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserAuditLogService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.usercenter.dto.contract.UserApplyAuditInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserExtendsService;

import java.util.List;

public class UserCenterOpearServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCenterOpearServiceImplTest.class);
	ApplicationContext ctx = null;
	UserCenterOperaService userCenterOperaService = null;
	UserExtendsService userExtendsService=null;
	UserAuditLogService userAuditLogService=null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		userCenterOperaService = (UserCenterOperaService) ctx.getBean("userCenterOperaService");
		userExtendsService=(UserExtendsService) ctx.getBean("userExtendsService");
        userAuditLogService=(UserAuditLogService) ctx.getBean("userAuditLogService");
	}

	@Test
	public void testInsertModifyDetailByUserInfo() {
		UserInfoDTO userInfoDTO=new UserInfoDTO();
		userInfoDTO.setUserId(315L);
		userInfoDTO.setExtendId(282L);
		UserBusinessDTO userBusinessDTO =new UserBusinessDTO();
		userBusinessDTO.setCompanyName("公司修改信息TE50");
		userBusinessDTO.setRegisteredCapital("66");
		userInfoDTO.setUserBusinessDTO(userBusinessDTO);
		userExtendsService.modifyUserExtends(userInfoDTO, UserExtendsType.BUSINESS);
	}




    @Test
    public void testSaveUserAuditLogDTO() throws Exception {
        UserAuditLogDTO userAuditLog=new UserAuditLogDTO();
        userAuditLog.setUserId(1000000182L);
        userAuditLog.setAuditId(1000000183L);
        ExecuteResult<UserAuditLogDTO> res= userCenterOperaService.saveUserAuditLogDTO(userAuditLog, UserEnums.UserOperaType.ZFZHSH);
        Assert.assertNotNull(res);


    }
    @Test
    public void testFindListByCondition() throws Exception {
        UserAuditLogDTO userAuditLog=new UserAuditLogDTO();

        userAuditLog.setAuditId(1000000183L);
        List<UserAuditLogDTO> res= userCenterOperaService.findListByCondition(userAuditLog, UserEnums.UserOperaType.ZFZHSH);
        Assert.assertNotNull(res);
    }
    @Test
    public void testChangeParentAndSonAccount() throws Exception {
        UserDTO sonUser=new UserDTO();
        sonUser.setUid(1000000182L);
        sonUser.setParentId(1000000184L);
        ExecuteResult<UserDTO> res= userCenterOperaService.changeParentAndSonAccount( sonUser);
        Assert.assertNotNull(res);
    }
}
