package com.camelot.shop.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopCategorySellerTest {
	    private ShopCategorySellerExportService shopCategorySellerExportService;
	    ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			shopCategorySellerExportService = (ShopCategorySellerExportService) ctx.getBean("shopCategorySellerExportService");
		}
		@Test
		public void queryShoCategoryTest(){
			ShopCategorySellerDTO dto=new ShopCategorySellerDTO();
			Pager pager=new Pager();
			dto.setCid(1);
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> result=shopCategorySellerExportService.queryShopCategoryList(dto, pager);
			Assert.assertEquals(true, result.isSuccess());
		}
		
		@Test
		public void deleteTest(){
	      long cid=2L;
	      ExecuteResult<String> result=shopCategorySellerExportService.deleteById(cid);
	      Assert.assertEquals(true, result.isSuccess());
		}
		
		@Test
		public void addTest(){
			ShopCategorySellerDTO dto=new ShopCategorySellerDTO();
			dto.setCid(2L);
			dto.setCname("2");
		 ExecuteResult<Long> result= shopCategorySellerExportService.addShopCategory(dto);
		 Assert.assertEquals(true, result.isSuccess());
		}
		
	@Test
	public void deletes(){
	Long[] lg={1L,2L};
	 ExecuteResult<String> result=shopCategorySellerExportService.deletes(lg);
	 Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void modifyTest(){
		ShopCategorySellerDTO dto=new ShopCategorySellerDTO();
	  dto.setCid(14L);
	  dto.setCname("4");
	  dto.setStatus(2);
	  ExecuteResult<String> result=shopCategorySellerExportService.update(dto);
	  Assert.assertEquals(true, result.isSuccess());
	}
	
}
