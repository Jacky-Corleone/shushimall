package com.camelot.usercenter.service.impl;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.userInfo.UserPersonalInfoDTO;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.camelot.usercenter.service.UserPersonalInfoService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class UserPersonalInfoServiceImplTest extends TestCase {
    ApplicationContext ctx = null;
    UserPersonalInfoService  userPersonalInfoService=null;
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        userPersonalInfoService = (UserPersonalInfoService) ctx.getBean("userPersonalInfoService");

    }

    public void testCreateUserPersonalInfoDTO() throws Exception {
        UserPersonalInfoDTO userPersonalInfo=new UserPersonalInfoDTO();
        userPersonalInfo.setBirthday("2015-01-20");
        ExecuteResult<UserPersonalInfoDTO> res= userPersonalInfoService.createUserPersonalInfoDTO(userPersonalInfo);
        Assert.assertNotNull(res);
    }

    public void testUpdateUserPersonalInfoDTO() throws Exception {
        UserPersonalInfoDTO userPersonalInfo=new UserPersonalInfoDTO();
        userPersonalInfo.setId(1L);
        userPersonalInfo.setBirthday("2015-01-23");
        ExecuteResult<UserPersonalInfoDTO> res=userPersonalInfoService.updateSelective(userPersonalInfo);
        Assert.assertNotNull(res);
    }

    public void testDeleteById() throws Exception {

    }

    public void testDeleteAll() throws Exception {

    }

    public void testSearchUserPersonalInfoDTOs() throws Exception {
        UserPersonalInfoDTO userPersonalInfo=new UserPersonalInfoDTO();
        userPersonalInfo.setUserId(1000000226L);

        List<UserPersonalInfoDTO> res=userPersonalInfoService.searchByCondition(userPersonalInfo);
        Assert.assertNotNull(res);
    }

    public void testGetUserPersonalInfoDTOById() throws Exception {
        ExecuteResult<String>  res=userPersonalInfoService.getPersonlInfoPerfectDegree("1000000291");
        Assert.assertNotNull(res);
    }

    public void testUpdateSelective() throws Exception {

    }

    public void testUpdateSelectiveWithDateTimeCheck() throws Exception {

    }

    public void testSearchByCondition() throws Exception {

    }

    public void testUpdateSelectiveByIdList() throws Exception {

    }

    public void testUpdateAllByIdList() throws Exception {

    }

    public void testDefunctById() throws Exception {

    }

    public void testDefunctByIdList() throws Exception {

    }
}