package com.camelot.shop.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopModifyInfoExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopModifyInfoExportServiceTest {
	
	    private ShopModifyInfoExportService shopModifyInfoExportService= null;
	    ApplicationContext ctx= null;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			shopModifyInfoExportService = (ShopModifyInfoExportService) ctx.getBean("shopModifyInfoExportService");
		}
		@Test
		public void queryShopModifyInfoTest(){
			ShopModifyInfoDTO shopModifyInfoDTO=new ShopModifyInfoDTO();
			Pager page=new Pager();
			ExecuteResult<DataGrid<ShopModifyInfoDTO>> result = shopModifyInfoExportService.queryShopModifyInfo(shopModifyInfoDTO, page);
			Assert.assertEquals(true, result.isSuccess());
		}
		@Test
		public void modifyShopModifyStatusTest(){
			ShopModifyInfoDTO shopModifyInfoDTO=new ShopModifyInfoDTO();
			shopModifyInfoDTO.setApplyStatus(1);
			shopModifyInfoDTO.setShopId(1l);
			shopModifyInfoDTO.setId(1l);
			ExecuteResult<String> result = shopModifyInfoExportService.modifyShopModifyStatus(shopModifyInfoDTO);
			Assert.assertEquals(true, result.isSuccess());
		}
		
		@Test
		public void modifySettlementStatesTest(){
			ExecuteResult<List<ShopModifyInfoDTO>> result = shopModifyInfoExportService.queryShopModifyInfoById(1l);
			Assert.assertEquals(true, result.isSuccess());
		}
	
}
