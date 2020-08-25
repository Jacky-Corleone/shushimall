package com.camelot.goodscenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;



public class ItemBrandServiceImpTest{
	ApplicationContext ctx = null;
	ItemBrandExportService itemBrandService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemBrandService = (ItemBrandExportService) ctx.getBean("itemBrandService");
	}
 
	@Test
	public void testGetSmsConfig() {
		DataGrid<ItemBrandDTO> data  = new DataGrid<ItemBrandDTO>();
		Pager page  = new Pager();
		page.setPage(2);
		page.setRows(20);
		data = itemBrandService.queryItemBrandAllList(page);
		List<ItemBrandDTO> datas = data.getRows();
		for (ItemBrandDTO ss : datas) {
			System.out.println("----------------"+ss.getBrandId()+"---------------");
		}
	}
	
	@Test
	public void testAddItemBrand(){
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		ItemBrandDTO brand = new ItemBrandDTO();
		brand.setBrandName("df是是非非的");
		brand.setBrandLogoUrl("/d/d");
		brand.setSecondLevCid(3L);
		brand.setThirdLevCid(9L);
		result = itemBrandService.addItemBrand(brand);
		System.out.println(result);
	}
	
	@Test
	public void testAddCategoryBrandBatch(){
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		ItemBrandDTO brand = new ItemBrandDTO();
		brand.setSecondLevCid(3L);
		brand.setThirdLevCid(9L);
		Long[] ids = new Long[]{1L,2L};
		brand.setBrandIds(ids);
		result = itemBrandService.addCategoryBrandBatch(brand);
		System.out.println(result);
	}
	@Test
	public void queryBrandListTest(){
		ItemBrandDTO itemBrandDTO=new ItemBrandDTO();
		itemBrandDTO.setBrandKey("其它");
		Pager page=new Pager();
		ExecuteResult<DataGrid<ItemBrandDTO>> result = itemBrandService.queryBrandList(itemBrandDTO, page);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void deleteBrandByIdTest(){
		
		ExecuteResult<String> result = itemBrandService.deleteBrandById(20l);
		System.out.println(JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void deleteBrandTest(){
		
		ExecuteResult<String> result = itemBrandService.deleteBrand(7l);
		System.out.println(JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
		
	}
	
	@Test
	public void deleteCategoryBrandTest(){
		
		ExecuteResult<String> result = itemBrandService.deleteCategoryBrand(1L,303L);
		System.out.println(JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
		
	}
}
