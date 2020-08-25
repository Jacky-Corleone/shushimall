package com.camelot.goodscenter;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.FavouriteCountDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;



public class ItemFavouriteExportServiceTest{
	ApplicationContext ctx = null;
	ItemFavouriteExportService itemFavouriteService = null; 
	private static final Logger logger = LoggerFactory.getLogger(ItemFavouriteExportServiceTest.class);
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemFavouriteService = (ItemFavouriteExportService) ctx.getBean("ItemFavouriteService");
	}
 
	@Test
	public void queryFavouriteCountTest() {
		Pager pager=new Pager();
		ExecuteResult<DataGrid<FavouriteCountDTO>> result = itemFavouriteService.queryFavouriteCount(2000000264l, pager);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryFavouriteTest() {
		Pager pager=new Pager();
		ItemFavouriteDTO favourite = new ItemFavouriteDTO();
		favourite.setUserId(Integer.valueOf("1000000863"));
		DataGrid<ItemFavouriteDTO> result = itemFavouriteService.datagrid(pager, favourite);
		// logger.info(JSON.toJSONString(result));
	}

}
