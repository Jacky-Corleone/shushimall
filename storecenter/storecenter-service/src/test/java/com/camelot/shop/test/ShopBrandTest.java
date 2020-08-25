package com.camelot.shop.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopBrandTest {
	private Logger LOG = LoggerFactory.getLogger(ShopBrandTest.class);
	    private ShopBrandExportService shopBrandExportService= null;
	    ApplicationContext ctx= null;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			shopBrandExportService = (ShopBrandExportService) ctx.getBean("shopBrandExportService");
		}
		@Test
		public void queryShopBrandListTest(){
			ExecuteResult<List<ShopBrandDTO>> result = shopBrandExportService.queryShopBrandList(1l, 1);
			Assert.assertEquals(true, result.isSuccess());
		}
		@Test
		public void addShopCategoryTest(){
			ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
			shopBrandDTO.setCid(15l);
			shopBrandDTO.setShopId(11l);
			shopBrandDTO.setBrandId(3l);
			shopBrandDTO.setSellerId(11l);
			
			ExecuteResult<String> result = shopBrandExportService.addShopBrand(shopBrandDTO);
			Assert.assertEquals(true, result.isSuccess());
			
		}
		@Test
		public void queryShopCategoryTest(){
			ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
	/*		Long[] shopIds=new Long[1];
			shopIds[0]=1l;
			shopBrandDTO.setShopIds(shopIds);*/
			shopBrandDTO.setStatus(new Integer(1));
			shopBrandDTO.setIsGroupBy(1L);
			Pager page=new Pager();
			ExecuteResult<DataGrid<ShopBrandDTO>> result = shopBrandExportService.queryShopBrand(shopBrandDTO, page);
			Assert.assertEquals(true, result.isSuccess());
			
		}
		
		@Test
		public void modifyShopCategoryStatusTest(){
			ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
			shopBrandDTO.setStatus(2);
			shopBrandDTO.setId(4l);
			shopBrandDTO.setComment("ddss");
			ExecuteResult<String> result = shopBrandExportService.modifyShopBrandStatus(shopBrandDTO);
			Assert.assertEquals(true, result.isSuccess());
			
		}
		@Test
		public void queryShopCategoryAllTest(){
			ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
			shopBrandDTO.setShopId(2000000218L);
			//shopBrandDTO.setStatus(5);
	/*		Long[] shopIds=new Long[1];
			shopIds[0]=1l;
			shopBrandDTO.setShopIds(shopIds);*/
			Pager page=new Pager();
			ExecuteResult<DataGrid<ShopBrandDTO>> result = shopBrandExportService.queryShopBrandAll(shopBrandDTO, page);
			Assert.assertEquals(true, result.isSuccess());
			
		}
		@Test
		public void selectBrandidByIdTest(){
			List<ShopBrandDTO> shopBrandDTO = shopBrandExportService.selectBrandIdById(new Long(168));
			LOG.info(shopBrandDTO.get(0).toString());
		}
		@Test
		public void updatebrandtest(){
			shopBrandExportService.updateStatusByIdAndBrandId(new Long(47));
		}
	
}
