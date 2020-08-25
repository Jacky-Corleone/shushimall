package com.camelot.goodscenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.Base;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.service.ItemAttributeExportService;
import com.camelot.openplatform.common.ExecuteResult;

public class ItemAttributeExportServiceTest  extends Base{
	
	ApplicationContext ctx = null;
	ItemAttributeExportService itemAttributeExportService = null; 

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemAttributeExportService = (ItemAttributeExportService) ctx.getBean("itemAttributeExportService");
	}
	
	@Test
	public void testAddItemAttribute(){
		ItemAttr itemAttr = new ItemAttr();
		itemAttr.setName("测试属性添加");
		ExecuteResult<ItemAttr> result = this.itemAttributeExportService.addItemAttribute(itemAttr);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void testModifyItemAttribute(){
		ItemAttr itemAttr = new ItemAttr();
		itemAttr.setId(533L);
		itemAttr.setName("测试属性修改");
		ExecuteResult<ItemAttr> result = this.itemAttributeExportService.modifyItemAttribute(itemAttr);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteItemAttribute(){
		ItemAttr itemAttr = new ItemAttr();
		itemAttr.setId(533L);
		ExecuteResult<ItemAttr> result = this.itemAttributeExportService.deleteItemAttribute(itemAttr);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testAddItemAttributeValue(){
		ItemAttrValue itemAttrValue = new ItemAttrValue();
		itemAttrValue.setAttrId(1L);
		itemAttrValue.setName("321我");
		ExecuteResult<ItemAttrValue> result = this.itemAttributeExportService.addItemAttrValue(itemAttrValue);
		Assert.assertEquals(true, result.isSuccess());
		
	}
	@Test
	public void testModifyItemAttributeValue(){
		ItemAttrValue itemAttrValue = new ItemAttrValue();
		itemAttrValue.setId(1170L);
		itemAttrValue.setName("321");
		ExecuteResult<ItemAttrValue> result = this.itemAttributeExportService.modifyItemAttrValue(itemAttrValue);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteItemAttributeValue(){
		ItemAttrValue itemAttrValue = new ItemAttrValue();
		itemAttrValue.setId(1170L);
		ExecuteResult<ItemAttrValue> result = this.itemAttributeExportService.deleteItemAttrValue(itemAttrValue);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void testaddItemAttrValueBackTest(){
		CatAttrSellerDTO inDTO = new CatAttrSellerDTO();
		inDTO.setCid(497L);
		inDTO.setSellerId(1000000294L);
		inDTO.setShopId(2000000292L);
		inDTO.setAttrType(1);
		ExecuteResult<List<ItemAttr>> result = this.itemAttributeExportService.addItemAttrValueBack(inDTO,2);
		System.out.println("result~~~~~~~~~~~~~~~"+JSON.toJSON(result));
		Assert.assertEquals(true, result.isSuccess());
	}
}
