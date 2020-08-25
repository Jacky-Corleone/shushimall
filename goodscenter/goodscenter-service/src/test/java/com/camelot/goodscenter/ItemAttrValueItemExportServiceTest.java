package com.camelot.goodscenter;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.fastjson.JSON;
import com.camelot.Base;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.openplatform.common.ExecuteResult;

public class ItemAttrValueItemExportServiceTest  extends Base{
	
	ApplicationContext ctx = null;
	ItemAttrValueItemExportService itemAttrValueItemExportService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemAttrValueItemExportService = (ItemAttrValueItemExportService) ctx.getBean("itemAttrValueItemExportService");
	}
	@Test
	public void addItemAttrValueItemTest(){
		List<ItemAttrValueItemDTO> itemAttrValueItemList=new ArrayList<ItemAttrValueItemDTO>();
		ItemAttrValueItemDTO itemAttrValueItemDTO=new ItemAttrValueItemDTO();
		itemAttrValueItemDTO.setAttrId(1l);//  属性ID
		itemAttrValueItemDTO.setValueId(1l);//  属性值ID
		itemAttrValueItemDTO.setItemId(1l);//  商品ID
		itemAttrValueItemDTO.setAttrType(1);//  属性类型:1:销售属性;2:非销售属性
		itemAttrValueItemDTO.setSortNumber(1);//  排序号。越小越靠前。
		itemAttrValueItemList.add(itemAttrValueItemDTO);
		ExecuteResult<String> result=itemAttrValueItemExportService.addItemAttrValueItem(itemAttrValueItemList);
		System.out.println("result------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	
	@Test
	public void modifyItemAttrValueItemTest(){
		List<ItemAttrValueItemDTO> itemAttrValueItemList=new ArrayList<ItemAttrValueItemDTO>();
		ItemAttrValueItemDTO itemAttrValueItemDTO=new ItemAttrValueItemDTO();
		itemAttrValueItemDTO.setAttrId(2l);//  属性ID
		itemAttrValueItemDTO.setValueId(2l);//  属性值ID
		itemAttrValueItemDTO.setItemId(1l);//  商品ID
		itemAttrValueItemDTO.setAttrType(1);//  属性类型:1:销售属性;2:非销售属性
		itemAttrValueItemDTO.setSortNumber(1);//  排序号。越小越靠前。
		itemAttrValueItemList.add(itemAttrValueItemDTO);
		ExecuteResult<String> result=itemAttrValueItemExportService.modifyItemAttrValueItem(itemAttrValueItemList);
		System.out.println("result------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void deleteItemAttrValueItemTest(){
		
		ExecuteResult<String> result=itemAttrValueItemExportService.deleteItemAttrValueItem(2l);
		System.out.println("result------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryItemAttrValueItemTest(){
		ItemAttrValueItemDTO itemAttrValueItemDTO=new ItemAttrValueItemDTO();
		itemAttrValueItemDTO.setItemId(1000000492l);
		itemAttrValueItemDTO.setAttrType(2);
		 ExecuteResult<List<ItemAttr>> result = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
		System.out.println("result------"+JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	
	
	
}
