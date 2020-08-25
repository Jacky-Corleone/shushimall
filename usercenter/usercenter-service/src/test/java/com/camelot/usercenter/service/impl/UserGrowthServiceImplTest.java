package com.camelot.usercenter.service.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.camelot.usercenter.service.UserGrowthService;
import com.camelot.usercenter.service.UserReportService;

import junit.framework.TestCase;

public class UserGrowthServiceImplTest{
	
	ApplicationContext ctx = null;
    UserGrowthService userGrowthService = null;
    
    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        userGrowthService = (UserGrowthService) ctx.getBean("userGrowthService");
    }
    
    @Test
    public void add(){
    	UserGrowthDTO userGrowthDTO  = new UserGrowthDTO();
    	userGrowthDTO.setGrowthValue(new BigDecimal("1000"));
    	userGrowthDTO.setUserId(1000000047);
    	userGrowthDTO.setType("1");
    	
    	this.userGrowthService.addUserGrowthInfo(userGrowthDTO);
    	System.out.println("111");
    }
    
    @Test
    public void select(){
    	Pager<UserGrowthDTO> pager = new Pager<UserGrowthDTO>();
    	userGrowthService.selectList(1000000047, pager);
    }
    	
    	

}
