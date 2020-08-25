package com.camelot.shop.test;

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
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopDomainExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopDomainExportServiceTest {
	
	    private ShopDomainExportService shopDomainExportService= null;
	    ApplicationContext ctx= null;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			shopDomainExportService = (ShopDomainExportService) ctx.getBean("shopDomainExportService");
		}
		@Test
		public void existShopUrlTest(){
			ExecuteResult<Boolean> result = shopDomainExportService.existShopUrl("nbs.shgreen.printhome.com",2000000011l);
			Assert.assertEquals(true, result.isSuccess());
		}
	
}
