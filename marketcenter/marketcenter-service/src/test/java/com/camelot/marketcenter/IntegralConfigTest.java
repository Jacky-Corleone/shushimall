package com.camelot.marketcenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class IntegralConfigTest {
	private final static Logger logger = LoggerFactory.getLogger(IntegralConfigTest.class);
    private IntegralConfigExportService integralConfigService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		integralConfigService = (IntegralConfigExportService) ctx.getBean("integralConfigService");
	}
	@Test
	public void testAddIntegralConfigDTO(){
		List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
		IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
		list.add(integralConfigDTO);
		integralConfigDTO.setEndPrice(new BigDecimal(1));
		ExecuteResult<Boolean> result = integralConfigService.addIntegralConfigDTO(list);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testUpdateIntegralConfigDTO(){
		List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
		IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
		list.add(integralConfigDTO);
		integralConfigDTO.setIntegralType(1);
		integralConfigDTO.setConfigId((long)62);
		ExecuteResult<Boolean> result = integralConfigService.updateIntegralConfigDTO(list);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void testQueryIntegralConfigDTO(){
		IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
		integralConfigDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_USING.getCode());
		integralConfigDTO.setPlatformId(0);
		ExecuteResult<DataGrid<IntegralConfigDTO>> er = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null);
		logger.info(JSON.toJSONString(er));
		Assert.assertTrue(er.isSuccess());
	}
	
	@Test
	public void testQueryIntegralConfigDTOByMoney(){
		ExecuteResult<List<IntegralConfigDTO>> er = integralConfigService.queryIntegralConfigDTOByMoney(new BigDecimal(1),0L);
		logger.info(JSON.toJSONString(er));
		Assert.assertTrue(er.isSuccess());
	}
	@Test
	public void testDelete(){
		IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
		integralConfigDTO.setIntegralType(3);
		integralConfigDTO.setPlatformId(2);
		ExecuteResult<Integer> er = integralConfigService.deleteByType(integralConfigDTO);
		logger.info(JSON.toJSONString(er));
		Assert.assertTrue(er.isSuccess());
	}
}
