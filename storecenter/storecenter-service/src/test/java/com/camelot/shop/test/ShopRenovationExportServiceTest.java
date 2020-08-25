package com.camelot.shop.test;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.dto.combin.ShopTemplatesCombinDTO;
import com.camelot.storecenter.service.ShopRenovationExportService;

public class ShopRenovationExportServiceTest {
	
	ApplicationContext ctx = null;
	ShopRenovationExportService shopRenovationExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopRenovationExportService = (ShopRenovationExportService) ctx.getBean("shopRenovationExportService");
	}
	@Test
	public void  queryShopRenovationTest(){
		ShopRenovationDTO shopRenovationDTO=new ShopRenovationDTO();
		shopRenovationDTO.setTemplatesId(1l);
		ExecuteResult<ShopTemplatesCombinDTO> result = shopRenovationExportService.queryShopRenovation(shopRenovationDTO);
		System.out.println(JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void queryShopRenovationByShopId(){
		ExecuteResult<ShopTemplatesCombinDTO> result = shopRenovationExportService.queryShopRenovationByShopId(2000000209l);
		System.out.println(JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void addShopRenovationTest(){
		ShopRenovationDTO shopRenovationDTO=new ShopRenovationDTO();
		shopRenovationDTO.setModuleName("banner广告");
		shopRenovationDTO.setPrice(BigDecimal.valueOf(55.55));
		shopRenovationDTO.setPictureUrl("http://www.baidu.com");
		shopRenovationDTO.setModuleGroup(1);
		shopRenovationDTO.setModultType(1);
		shopRenovationDTO.setSkuId(111000l);
		shopRenovationDTO.setPosition("a");
		shopRenovationDTO.setTemplatesId(1l);
		shopRenovationDTO.setChainUrl("WWW.SSDFF.COM");
		ExecuteResult<String> result = shopRenovationExportService.addShopRenovation(shopRenovationDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test 
	public void modifyShopRenovationTest(){
		ShopRenovationDTO shopRenovationDTO=new ShopRenovationDTO();
		shopRenovationDTO.setModuleName("bannerDD广告1");
		shopRenovationDTO.setPrice(BigDecimal.valueOf(66.55));
		shopRenovationDTO.setPictureUrl("http://www.baidu.com");
		shopRenovationDTO.setModuleGroup(1);
		shopRenovationDTO.setModultType(1);
		shopRenovationDTO.setSkuId(111000l);
		shopRenovationDTO.setPosition("a");
		shopRenovationDTO.setTemplatesId(1l);
		shopRenovationDTO.setChainUrl("WWW.SSDFF.COM");
		shopRenovationDTO.setId(2L);
		ExecuteResult<String> result = shopRenovationExportService.modifyShopRenovation(shopRenovationDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test 
	public void queryShopRenovationByItemIdTest(){
		DataGrid<ShopRenovationDTO> result = shopRenovationExportService.queryShopRenovationByItemId(1000000333L);
	}
}
