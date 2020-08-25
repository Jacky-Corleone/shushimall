package com.camelot.goodscenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.Base;
import com.camelot.goodscenter.dto.ItemAdDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemShopCidDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.dto.enums.ItemPlatLinkStatusEnum;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品信息的单元测试]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemExportServiceImplAllTest extends Base {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemExportServiceImplAllTest.class);

	ApplicationContext ctx = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		this.itemEvaluationService = (ItemEvaluationService)ctx.getBean("itemEvaluationService");
		this.itemExportService = (ItemExportService)ctx.getBean("itemExportService");
	}
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:商品信息的查询列表]</p>
	 * Created on 2015年2月4日
	 * @author:[chenx]
	 */
	@Test
	public void testQueryItemList() throws Exception{
		//入参数
		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
//		Long[] a={1l,2l,3l};
//		itemInDTO.setShopIds(a);
//		itemInDTO.setCid(1l);
		itemInDTO.setItemName("test01自上传");
//		itemInDTO.setItemStatus(5);
//		//平台
//		itemInDTO.setOperator(1);
//		itemInDTO.setProductId(123l);
//		itemInDTO.setSkuId(123l);
//		itemInDTO.setShopCid(1L);
//		itemInDTO.setMaxInvetory(10);
//		itemInDTO.setMinInvetory(1);
//		itemInDTO.setMinPrice(new BigDecimal(1));
//		itemInDTO.setMaxPrice(new BigDecimal(10000));
//		itemInDTO.setStartTime(new SimpleDateFormat("yyyymmdd").parse("20150301"));
//		itemInDTO.setEndTime(new SimpleDateFormat("yyyymmdd").parse("20150321"));
//		List<Integer> itemStatusList = new ArrayList<Integer>();
//		itemStatusList.add(4);
//		itemInDTO.setItemStatusList(itemStatusList);
		String market_price="item_name";
        String order="desc";
        
		//分页类
		Pager pager = new Pager();
		//设置每页显示的记录数
		pager.setRows(10);
		//设置当前页
		pager.setPage(1);
		pager.setOrder("desc");
		pager.setSort("item_name");
		
		
		DataGrid<ItemQueryOutDTO> size = itemExportService.queryItemList(itemInDTO, pager);
		LOGGER.info("" + size.getTotal());
	}
    /**
     * 
     * <p>Discription:[方法功能中文描述:商品信息的批量操作]</p>
     * Created on 2015年2月4日
     * @author:[chenx]
     */
//	@Test
//	public void testModifyItemStatusById() {
//		Long[] ids = { 1l, 2l, 3l };
//		String changeReason = "changeReason";
//		int itemStatus = 5;
//		ExecuteResult<String> executeResult = itemExportService.modifyItemStatusById(ids, changeReason, itemStatus);
//		LOGGER.info("{}" + executeResult);
//	}
	@Test
	public void testGetItemById() {
		Long id = 1000000286L;
		ExecuteResult<ItemDTO> result = this.itemExportService.getItemById(id);
		LOGGER.info(JSON.toJSONString(result.getResult()));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testGetSkuShopCart(){
		ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setItemId(1000000436L);
		dto.setAreaCode("37");
		dto.setShopId(2000000288L);
		dto.setSkuId(1000000741L);
		dto.setQty(1);
//		dto.setSellerId(1000000286L);
//		dto.setBuyerId(1000000284L);
		ExecuteResult<ItemShopCartDTO> result = this.itemExportService.getSkuShopCart(dto);
		LOGGER.info("{}",result);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyItemAdBatch(){
		List<ItemAdDTO> params = new ArrayList<ItemAdDTO>();
		ItemAdDTO dto = new ItemAdDTO();
		dto.setItemId(1L);
		dto.setAd("测试广告1");
		params.add(dto);
		ExecuteResult<String> result = this.itemExportService.modifyItemAdBatch(params);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void testModifyItemShopCidBatch(){
		List<ItemShopCidDTO> params = new ArrayList<ItemShopCidDTO>();
		ItemShopCidDTO dto = new ItemShopCidDTO();
		dto.setItemId(1L);
		dto.setShopCid(1L);
		params.add(dto);
		dto = new ItemShopCidDTO();
		dto.setItemId(2L);
		dto.setShopCid(2L);
		params.add(dto);
		ExecuteResult<String> result = this.itemExportService.modifyItemShopCidBatch(params);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void testGetItemBaseInfoById(){
		ExecuteResult<ItemBaseDTO> result = this.itemExportService.getItemBaseInfoById(1000000002l);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void testAddItemInfo(){
		Long[] shopIds  = new Long[] {2000000348L,
				2000000349L,
				2000000350L,
				2000000351L,
				2000000352L,
				2000000353L,
				2000000354L,
				2000000355L,
				2000000356L,
				2000000357L,
				2000000358L,
				2000000359L,
				2000000360L,
				2000000361L,
				2000000362L,
				2000000363L,
				2000000364L,
				2000000365L,
				2000000366L,
				2000000367L,
				2000000368L,
				2000000369L,
				2000000370L,
				2000000371L,
				2000000372L,
				2000000373L,
				2000000374L,
				2000000375L,
				2000000376L,
				2000000377L,
				2000000378L,
				2000000379L,
				2000000380L,
				2000000381L,
				2000000382L,
				2000000383L,
				2000000384L,
				2000000385L,
				2000000386L,
				2000000387L,
				2000000388L,
				2000000389L,
				2000000390L,
				2000000391L,
				2000000392L,
				2000000393L,
				2000000394L,
				2000000395L,
				2000000396L,
				2000000397L,
				2000000398L,
				2000000399L}	;
		
		long[] userIds = new long[]{
				1000000289L,
				1000000812L,
				1000000813L,
				1000000814L,
				1000000815L,
				1000000816L,
				1000000817L,
				1000000818L,
				1000000819L,
				1000000820L,
				1000000821L,
				1000000822L,
				1000000823L,
				1000000824L,
				1000000825L,
				1000000826L,
				1000000827L,
				1000000828L,
				1000000829L,
				1000000830L,
				1000000831L,
				1000000832L,
				1000000833L,
				1000000834L,
				1000000835L,
				1000000836L,
				1000000837L,
				1000000838L,
				1000000839L,
				1000000840L,
				1000000841L,
				1000000842L,
				1000000843L,
				1000000844L,
				1000000845L,
				1000000846L,
				1000000847L,
				1000000848L,
				1000000849L,
				1000000850L,
				1000000851L,
				1000000852L,
				1000000853L,
				1000000854L,
				1000000855L,
				1000000856L,
				1000000857L,
				1000000858L,
				1000000859L,
				1000000860L,
				1000000861L,
				1000000862		};
		
		for(int j=0;j<2;j++){
			long userId = userIds[j];
			long shopId = shopIds[j];
			for(int i=0;i<2000;i++){
				ItemDTO item = new ItemDTO();
				item.setOperator(1);//操作者
				item.setAd("柯莱特测试商品");
				item.setAddSource(1);//自定义添加
				item.setAfterService("售后服务测试");
				item.setAttributesStr("574:1228;574:1229;");
				item.setAttrSaleStr("575:1230;575:1231;");
				item.setBrand(306L);
				item.setCid(480L);
				item.setDescribeUrl("商品描述测试");
				item.setGuidePrice(new BigDecimal("12300"));
				item.setHasPrice(1);
				item.setInventory(99999999);
				item.setItemName("柯莱特测试商品"+i);
				item.setItemStatus(ItemStatusEnum.SALING.getCode());
				item.setMarketPrice(new BigDecimal(1000));
				item.setMarketPrice2(new BigDecimal(899));
				item.setOrigin("北京");
				item.setPackingList("商品包装");
				String[] picUrls = new String[]{"replaceUrl","replaceUrl"};
				item.setPicUrls(picUrls);
				item.setPlatLinkStatus(null);
				item.setPlstItemId(null);
				item.setProductId(123456L);
				item.setSellerId(userId);
				List<SellPrice> sellPrices = new ArrayList<SellPrice>();
				SellPrice price = new SellPrice();
				price.setAreaId("0");
				price.setAreaName("全国");
				price.setCostPrice(new BigDecimal(1));
				price.setMaketPirce(new BigDecimal(1));
				price.setMaxNum(1000);
				price.setMinNum(1);
				price.setSellerId(userId);
				price.setSellPrice(new BigDecimal(1));
				price.setShopId(shopId);
				price.setStepIndex(0);
				sellPrices.add(price);
				item.setSellPrices(sellPrices);
				item.setShopCid(1L);
				item.setShopId(shopId);
				List<SkuInfo> skuInfos = new ArrayList<SkuInfo>();
				SkuInfo sku = new SkuInfo();
				sku.setAttributes("575:1231;");
				sku.setSellPrices(sellPrices);
				sku.setSkuInventory(99999999);
				List<SkuPictureDTO> skuPics = new ArrayList<SkuPictureDTO>();
				SkuPictureDTO skuPic = new SkuPictureDTO();
				skuPic.setPicUrl("replaceUrl");
				skuPics.add(skuPic);
				sku.setSkuPics(skuPics);
				sku.setSkuType(1L);
				skuInfos.add(sku);
				sku = new SkuInfo();
				sku.setAttributes("575:1230;");
				sku.setSellPrices(sellPrices);
				sku.setSkuInventory(99999999);
				skuPics = new ArrayList<SkuPictureDTO>();
				skuPic = new SkuPictureDTO();
				skuPic.setPicUrl("replaceUrl");
				skuPics.add(skuPic);
				sku.setSkuPics(skuPics);
				sku.setSkuType(1L);
				skuInfos.add(sku);
				item.setSkuInfos(skuInfos);
				item.setStatusChangeReason(null);
				item.setWeight(new BigDecimal(10000)); 
				
				item.setPlatLinkStatus(3);
				
				item.setWeightUnit("克");
				
				List<ItemAttr> attrs = new ArrayList<ItemAttr>();
				ItemAttr itemAttr = new ItemAttr();
				itemAttr.setId(574L);
				itemAttr.setName("年份");
				List<ItemAttrValue> values = new ArrayList<ItemAttrValue>();
				ItemAttrValue val = new ItemAttrValue();
				val.setAttrId(574L);
				val.setId(1228L);
				val.setName("1988");
				values.add(val);
				val = new ItemAttrValue();
				val.setAttrId(574L);
				val.setId(1229L);
				val.setName("2000");
				values.add(val);
				itemAttr.setValues(values);
				attrs.add(itemAttr);
				item.setAttributes(attrs);
				
				attrs = new ArrayList<ItemAttr>();
				itemAttr = new ItemAttr();
				itemAttr.setId(575L);
				itemAttr.setName("颜色");
				values = new ArrayList<ItemAttrValue>();
				val = new ItemAttrValue();
				val.setAttrId(575L);
				val.setId(1230L);
				val.setName("黑");
				values.add(val);
				val = new ItemAttrValue();
				val.setAttrId(575L);
				val.setId(1231L);
				val.setName("白");
				values.add(val);
				itemAttr.setValues(values);
				attrs.add(itemAttr);
				
				item.setAttrSale(attrs);
				ExecuteResult<ItemDTO> result = this.itemExportService.addItemInfo(item);
			}
		}
	}
	
	@Test
	public void queryItemByCid(){
		Pager page=new  Pager();
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = itemExportService.queryItemByCid(347l, page);
		System.out.println("result----------"+JSON.toJSONString(result.getResult()));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyItemStatusBatch(){
		ItemStatusModifyDTO dto = new ItemStatusModifyDTO();
		dto.setItemStatus(ItemStatusEnum.SHELVING.getCode());
		dto.setOperator(1);//操作商家商品
		dto.setUserId(1L);
		List<Long> list = new ArrayList<Long>();
		list.add(1000000318L);
		dto.setItemIds(list);
		dto.setCreatePlatItem(true);
		ExecuteResult<String> result = this.itemExportService.modifyItemStatusBatch(dto);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyItemById(){
		ItemDTO item = new ItemDTO();
		item = this.itemExportService.getItemById(1000000493L).getResult();
		item.setWeightUnit("gg");
		
		List<ItemAttr> attrs = new ArrayList<ItemAttr>();
		ItemAttr itemAttr = new ItemAttr();
		itemAttr.setId(2L);
		itemAttr.setName("商品属性1");
		attrs.add(itemAttr);
		List<ItemAttrValue> values = new ArrayList<ItemAttrValue>();
		ItemAttrValue val = new ItemAttrValue();
		val.setAttrId(2L);
		val.setId(2L);
		val.setName("商品属性值11");
		values.add(val);
		itemAttr.setValues(values);
		attrs.add(itemAttr);
		item.setAttributes(attrs);
		item.setAttrSale(attrs);
		
		ExecuteResult<ItemDTO> result = this.itemExportService.modifyItemById(item);
		Assert.assertEquals(true, result.isSuccess());
	}
	
//	@Test
//	public void testQueryItemDTOList(){
//		SearchInDTO dto = new SearchInDTO();
//		dto.setKeyword("飞");
//		dto.setCid(1L);
//		DataGrid<ItemDTO> dg = this.itemExportService.queryItemDTOList(dto, null);
//		Assert.assertNotNull( dg);
//	}
	
	@Test
	public void testQueryShopIdByPlatItemId(){
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = this.itemExportService.queryItemByPlatItemId(123L, new Pager<Long>());
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyItemPlatStatus(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(1000000170L);
		ExecuteResult<String> result = this.itemExportService.modifyItemPlatStatus(ids, ItemPlatLinkStatusEnum.STORING.getCode());
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyItemStatus(){
		ItemStatusModifyDTO inDTO = new ItemStatusModifyDTO();
		inDTO.setShopId(1L);
		inDTO.setChangeReason("关闭店铺下架");
		inDTO.setItemStatus(ItemStatusEnum.UNSHELVED.getCode());
		ExecuteResult<String> result = itemExportService.modifyShopItemStatus(inDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testDeleteItem(){
		ExecuteResult<String> result = itemExportService.deleteItem(10000247L);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testAddItemToPlat(){
		ExecuteResult<String> result = itemExportService.addItemToPlat(1000000011l);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	/**
	 * 
	 * <p>Description: [获取销售属性]</p>
	 * Created on 2015年8月13日
	 * @author:[宋文斌]
	 */
	@Test
	public void testQueryAttrBySkuId(){
		ExecuteResult<String> result = itemExportService.queryAttrBySkuId(1000000551L);
		Assert.assertEquals(true, result.isSuccess());
		LOGGER.info(result.getResult());
	}
	
	/**
	 * 
	 * <p>Description: [根据平台的非销售属性（attributes）查询商品信息]</p>
	 * Created on 2015年8月16日
	 * @author:[宋文斌]
	 */
//	@Test
//	public void testQueryItemByAttr(){
//		ExecuteResult<List<ItemDTO>> result = itemExportService.queryItemByAttr("1814:2858");
//		Assert.assertEquals(true, result.isSuccess());
//	}
	
	@Test
	public void testQueryItemByItemNameAndShopId(){
		ExecuteResult<List<ItemDTO>> result = itemExportService.queryItemByItemNameAndShopId("维达抽纸", 1000000353L);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void testGetItemBySkuId(){
		ExecuteResult<ItemDTO> result = itemExportService.getItemBySkuId(1000000002l);
		Assert.assertEquals(true, result.isSuccess());
	}
}
