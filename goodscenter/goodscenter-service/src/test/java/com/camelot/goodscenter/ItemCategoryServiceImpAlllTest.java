package com.camelot.goodscenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.Base;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 平台类目列表测试类
 * @author 周立明
 *
 */
public class ItemCategoryServiceImpAlllTest extends Base {

	private static final Logger logger = LoggerFactory.getLogger(ItemCategoryServiceImpAlllTest.class);

	private ItemCategoryService itemCategoryService ;
    
    ApplicationContext ctx = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		this.itemEvaluationService = (ItemEvaluationService)ctx.getBean("itemEvaluationService");
		this.itemExportService = (ItemExportService)ctx.getBean("itemExportService");
		this.itemCategoryService = (ItemCategoryService)ctx.getBean("itemCategoryService");
	}

	//平台类目添加方法测试类
	@Test
	public void addItemCategoryTest(){
		ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
		itemCategoryDTO.setCategoryCName("打印机ooo4444");
		itemCategoryDTO.setCategoryParentCid(0L);
		itemCategoryDTO.setCategoryStatus(1);
		itemCategoryService.addItemCategory(itemCategoryDTO);
	}
	//平台所有类目列表查询测试类
	@Test
	public void queryItemCategoryAllListTest(){
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(5);
		DataGrid<ItemCategoryDTO> dataGrid = new DataGrid<ItemCategoryDTO>();
		dataGrid = itemCategoryService.queryItemCategoryAllList(pager);
		System.out.println(dataGrid.getTotal());
	}
	//根据父id查询类目列表测试类
	@Test
	public void queryItemCategoryListTest(){
		Long parentId = 0L;
		DataGrid<ItemCategoryDTO> dataGrid = new DataGrid<ItemCategoryDTO>();
		dataGrid = itemCategoryService.queryItemCategoryList(parentId);
		System.out.println(dataGrid.getTotal());
	}
	//平台类目属性添加方法测试类
	@Test
	public void addCategoryAttrTest(){
		Long cid = 2L;
		String  attrName = "颜色";
		int attrType = 1;
//		ExecuteResult<Long> ss = itemCategoryService.addCategoryAttr(cid, attrName, attrType);
//		System.out.println(ss.getResult());
	}
	//平台类目属性值查询方法测试类
	@Test
	public void queryCategoryAttrList(){
		Long cid = 2L;
		int type = 1;
		itemCategoryService.queryCategoryAttrList(cid,type);
	}
	//平台类目属性值添加方法测试类
		@Test
		public void addCategoryAttrTest2(){
			Long cid = 2L;
			Long attrId = 3L;
			String valueName = "红色";
			itemCategoryService.addCategoryAttrValue(cid, attrId, valueName);
		}
	@Test
	public void testQueryParentCategoryList(){
		Long[] ids = new Long[]{30L};
		ExecuteResult<List<ItemCatCascadeDTO>> result = itemCategoryService.queryParentCategoryList(ids);
		logger.info("{}",result);
	}

	@Test
	public void testAddItemAttrSeller(){
		CatAttrSellerDTO inDTO = new CatAttrSellerDTO();
		ItemAttr attr = new ItemAttr();
		attr.setName("测试商品属性1");
		inDTO.setAttr(attr);
		inDTO.setCid(1L);
		inDTO.setSellerId(1L);
		inDTO.setShopId(1L);
		inDTO.setAttrType(1);
		ExecuteResult<ItemAttr> result = itemCategoryService.addItemAttrSeller(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void testAddItemAttrValueSeller(){
		CatAttrSellerDTO inDTO = new CatAttrSellerDTO();
		ItemAttrValue iav = new ItemAttrValue();
		iav.setName("测试商品属性值1");
		iav.setAttrId(113L);
		inDTO.setAttrValue(iav);
		inDTO.setCid(1L);
		inDTO.setSellerId(1L);
		inDTO.setShopId(1L);
		ExecuteResult<ItemAttrValue> result = itemCategoryService.addItemAttrValueSeller(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void testQueryCatAttrSellerList(){
		CatAttrSellerDTO inDTO = new CatAttrSellerDTO();
		inDTO.setCid(1L);
		inDTO.setSellerId(1L);
		inDTO.setShopId(1L);
		inDTO.setAttrType(1);
		ExecuteResult<List<ItemAttr>> result = itemCategoryService.queryCatAttrSellerList(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testQueryCatAttrByKeyVals(){
		//String attrStr = "113:99;";
		String attrStr = "1656:2647;1657:2649;";
		ExecuteResult<List<ItemAttr>> result = this.itemCategoryService.queryCatAttrByKeyVals(attrStr);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteCategoryAttr(){
		ExecuteResult<String> result = this.itemCategoryService.deleteCategoryAttr(388L,2169L,2);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteCategoryAttrValue(){
		ExecuteResult<String> result = this.itemCategoryService.deleteCategoryAttrValue(388L, 2169L, 3557L, 2);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteCategory(){
		ExecuteResult<String> result = this.itemCategoryService.deleteItemCategory(264L);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void testQueryChildCategory(){
		ItemCategoryDTO itemCategoryDTO=new ItemCategoryDTO();
		itemCategoryDTO.setCategoryCid(380l);
		ExecuteResult<QueryChildCategoryOutDTO> res=this.itemCategoryService.queryAllChildCategory(itemCategoryDTO);
		Assert.assertEquals(true, res.isSuccess());
	}
	@Test
	public void testQueryThirdCatsList(){
		ExecuteResult<List<ItemCategoryDTO>> res = itemCategoryService.queryThirdCatsList(453L);
		Assert.assertEquals(true, res.isSuccess());
	}
}
