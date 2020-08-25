package com.camelot.shop.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.impl.ShopFavouriteServiceImpl;

/** 
 * <p>Description: [店铺收藏测试类]</p>
 * Created on 2015年11月16日
 * @author  <a href="mailto: zhouzhijun@camelotchina.com">周志军</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopFavouriteServiceTest {
	private Logger LOG = LoggerFactory.getLogger(ShopFavouriteServiceTest.class);
	    private ShopFavouriteServiceImpl shopFavouriteService= null;
	    ApplicationContext ctx= null;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			shopFavouriteService = (ShopFavouriteServiceImpl) ctx.getBean("shopFavouriteService");
		}
		@Test
		public void testDatagrid(){
			ShopFavouriteDTO favourite = new ShopFavouriteDTO();
			ArrayList<Long> ids = new ArrayList<Long>();
			ids.add(1000001281L);
			favourite.setUserIdList(ids);
			Pager pager = new Pager();
			DataGrid<ShopFavouriteDTO> dgShops = shopFavouriteService.datagrid(pager, favourite);
			System.out.println(JSON.toJSONString(dgShops.getRows()));
			Assert.assertEquals(true, dgShops.getTotal()==0);
		}
}
