package com.camelot.basecenter.tdk;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.basecenter.service.BaseTDKConfigService;
import com.camelot.openplatform.common.ExecuteResult;

public class BaseTDKConfigServiceImplTest {
	
	ApplicationContext ctx = null;
	BaseTDKConfigService baseTDKConfigService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		baseTDKConfigService = (BaseTDKConfigService) ctx.getBean("baseTDKConfigService");
	}

	@Test
	public void testSaveBaseTDKConfig() {
		BaseTDKConfigDTO baseTDKConfigDTO = new BaseTDKConfigDTO();
		baseTDKConfigDTO.setTitle("张志强测试");
		baseTDKConfigDTO.setDescription("描述");
		baseTDKConfigDTO.setKeywords("a,b,c");
		ExecuteResult<BaseTDKConfigDTO> result = baseTDKConfigService.saveBaseTDKConfig(baseTDKConfigDTO);
		baseTDKConfigDTO = result.getResult();
		Assert.assertEquals(result.isSuccess(), true);
	}

	@Test
	public void testQueryBaseTDKConfig() {
		ExecuteResult<BaseTDKConfigDTO> result = baseTDKConfigService.queryBaseTDKConfig(null);
		Assert.assertEquals(result.isSuccess(), true);
	}

	@Test
	public void testModifyBaseTDKConfig() {
		BaseTDKConfigDTO baseTDKConfigDTO = new BaseTDKConfigDTO();
		baseTDKConfigDTO.setTitle("张志强测试111");
		baseTDKConfigDTO.setDescription("描述111");
		baseTDKConfigDTO.setKeywords("a,b,c,2");
		baseTDKConfigDTO.setId(7L);
		ExecuteResult<BaseTDKConfigDTO> result = baseTDKConfigService.modifyBaseTDKConfig(baseTDKConfigDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

}
