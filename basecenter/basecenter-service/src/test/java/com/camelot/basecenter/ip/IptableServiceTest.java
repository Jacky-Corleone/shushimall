package com.camelot.basecenter.ip;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.annotation.Service;
import com.camelot.basecenter.dto.IptableDTO;
import com.camelot.basecenter.service.IptableService;

/** 
 * Description: [XXXX的单元测试]
 * Created on 2016年02月24日
 * @author  <a href="mailto: XXXXXX@camelotchina.com">中文名字</a>
 * @version 1.0 
 * Copyright (c) 2016年 北京柯莱特科技有限公司 交付部 
 */
@Service
public class IptableServiceTest{

	private IptableService iptableService;
    ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		iptableService = (IptableService) ctx.getBean("iptableService");
	}
	
	
//	@Test
//	public void queryList(){
//		IptableDTO iptableDTO = new IptableDTO();
//		iptableDTO.setNum(127*256*256*256L);
//		List<IptableDTO> list = iptableService.query(iptableDTO);
//		System.out.println(list.size());
//		System.out.println(list.get(0).getCountry());
//	}
}