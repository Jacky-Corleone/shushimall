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
import com.camelot.storecenter.service.ShopAudiExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;

public class ShopCategoryExportServiceTest {
	
	ApplicationContext ctx = null;
	ShopCategoryExportService shopCategoryExportService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopCategoryExportService = (ShopCategoryExportService) ctx.getBean("shopCategoryExportService");
	}
	@Test
	public void  queryShopCategoryListTest(){
		ExecuteResult<List<ShopCategoryDTO>> result = shopCategoryExportService.queryShopCategoryList(2000000100l,3);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void addShopCategoryTest(){
		ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
		shopCategoryDTO.setCid(15l);
		shopCategoryDTO.setShopId(11l);
		shopCategoryDTO.setSellerId(11l);

		ExecuteResult<String> result = shopCategoryExportService.addShopCategory(shopCategoryDTO);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void queryShopCategoryTest(){
		ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
		//Long[] shopIds=new Long[1];
		//shopIds[0]=1l;
		//shopCategoryDTO.setShopIds(shopIds);
        shopCategoryDTO.setStatus(new Integer(1));
		shopCategoryDTO.setIsGroupBy(1l);
		Pager page=new Pager();
		ExecuteResult<DataGrid<ShopCategoryDTO>> result = shopCategoryExportService.queryShopCategory(shopCategoryDTO, page);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void queryShopCategoryAllTest(){
		ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
		//Long[] shopIds=new Long[1];
		//shopIds[0]=1l;
		//shopCategoryDTO.setShopIds(shopIds);
		shopCategoryDTO.setShopId(2000000098l);
		shopCategoryDTO.setStatusLage(1);
		Pager page=new Pager();
		ExecuteResult<DataGrid<ShopCategoryDTO>> result = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO, page);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void modifyShopCategoryStatusTest(){
		ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
		shopCategoryDTO.setStatus(2);
		shopCategoryDTO.setId(4l);
		ExecuteResult<String> result = shopCategoryExportService.modifyShopCategoryStatus(shopCategoryDTO);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	
	/**
	 * 
	 * <p>Description: [测试删除店铺经营类目]</p>
	 * Created on 2015年8月26日
	 * @author:[宋文斌]
	 */
	@Test
	public void updateStatusByShopIdAndCidTest(){
		ExecuteResult<String> result = shopCategoryExportService.updateStatusByShopIdAndCid(2000000216L, 391L);
		Assert.assertEquals(true, result.isSuccess());
	}
	
}
