package com.camelot.usercenter.service.impl;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.service.UserReportService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

public class UserReportServiceImplTest extends TestCase {

    ApplicationContext ctx = null;
    UserReportService userReportService = null;

    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        userReportService = (UserReportService) ctx.getBean("userReportService");

    }

    public void testGetCustomerAndShopReportSum() throws Exception {
        ExecuteResult<UserAndShopReportDTO> res= userReportService.getCustomerAndShopReportSum();
        Assert.assertNotNull(res);
    }

    public void testGetCustomerAndShopReportByCondition() throws Exception {

        ExecuteResult<List<UserAndShopReportDTO>> res= userReportService.getCustomerAndShopReportByCondition(new Date(), new Date());
        Assert.assertNotNull(res);
    }
}