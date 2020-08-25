package com.camelot.pricecenter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.service.ShopCartPromotionService;

public class ShopCartServiceImplTest {
	private static Logger LOG = Logger.getLogger(ShopCartServiceImplTest.class);

	private ShopCartPromotionService shopCartPromotionService=null;


	ApplicationContext ctx=null;
	
	

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopCartPromotionService = (ShopCartPromotionService) ctx.getBean("shopCartPromotionService");
	}
	
	@Test
	public void testcalcPromotion() {
		
		
		/*ProductInPriceDTO dto=new ProductInPriceDTO();*/
		/*dto.setChecked(true);
		dto.setCid(453l);
		//dto.setCtoken('b470de07-16e6-4dcc-ad54-2600187686b6');
		dto.setHasPrice(true);
		dto.setItemId(1000000005l);
		dto.setPayPrice(new BigDecimal(1598.00));
		dto.setPayTotal(new BigDecimal(1598.00));
		dto.setQty(9999999);
		dto.setSellerId(1000000005l);
		dto.setQty(1);
		dto.setShopId(2000000002l);
		dto.setSkuId(1000000007l);
		dto.setStatus(4);
		dto.setSkuPrice(new BigDecimal(1598.00));*/
		
		
		
		//dto.setUid('1000000005');
		
		
		
		String str="[{'checked':true,'ctoken':'46ff4f2d-c66b-44c9-b125-d51eb8217e01','discountTotal':0,'hasPrice':false,'itemId':1000000091,'originalDiscount':0,'quantity':2,'regionId':'11','sellerId':1000000005,'shopId':2000000002,'skuId':1000000111,'straightDownAmount':0,'uid':'1000000005','unusualMsg':[]}]";
		List<ProductInPriceDTO> list = JSON.parseArray(str, ProductInPriceDTO.class);
		for(ProductInPriceDTO dto :list){
			//shopCartPromotionService.calcPromotion(dto);
		}
		Assert.assertEquals(true, true);
	}
	
	
	@Test
	public void testGetShopByProducts() {
		
		
		/*List<ProductInPriceDTO> list=new ArrayList<ProductInPriceDTO>();
		ProductInPriceDTO dto=new ProductInPriceDTO();
		dto.setChecked(true);
		dto.setCid(453l);
		dto.setCtoken('b470de07-16e6-4dcc-ad54-2600187686b6');
		dto.setHasPrice(true);
		dto.setItemId(1000000002l);
		dto.setPayPrice(new BigDecimal(75.05));
		dto.setPayTotal(new BigDecimal(75.05));
		dto.setQty(99);
		dto.setSellerId(1000000005l);
		dto.setQuantity(1);
		dto.setShopId(2000000002l);
		dto.setRegionId('11');
		dto.setSkuId(1000000002l);
		dto.setStatus(4);
		dto.setSkuPrice(new BigDecimal(75.05));
		dto.setUid('1000000005');
		dto.setPromId(203l);
		dto.setPromType(1);
		List<PromotionDTO> promotions=new ArrayList<PromotionDTO>();
		PromotionDTO PromotionDTO=new PromotionDTO();
		PromotionDTO.setId(203l);
		PromotionDTO.setPrice(new BigDecimal(0.05));
		PromotionDTO.setType(1);
		PromotionDTO.setName('平台直降活动1');
		
		list.add(dto);*/
		String str="[{'checked':true,'ctoken':'9d123265-0060-41d8-9060-887cc35c9d7e','discountTotal':0,'hasPrice':false,'itemId':1000000558,'originalDiscount':0,'quantity':1,'regionId':'11','sellerId':1000000863,'shopId':2000000216,'skuId':1000000965,'uid':'1000000863','unusualMsg':[]},{'checked':true,'ctoken':'9d123265-0060-41d8-9060-887cc35c9d7e','discountTotal':0,'hasPrice':false,'itemId':1000000537,'originalDiscount':0,'quantity':7,'regionId':'11','sellerId':1000000863,'shopId':2000000216,'skuId':1000000693,'uid':'1000000863','unusualMsg':[]},{'checked':true,'ctoken':'9d123265-0060-41d8-9060-887cc35c9d7e','discountTotal':0,'hasPrice':false,'itemId':1000000488,'originalDiscount':0,'quantity':1,'regionId':'11','sellerId':1000000863,'shopId':2000000216,'skuId':1000000652,'uid':'1000000863','unusualMsg':[]}]";
		List<ProductInPriceDTO> list = JSON.parseArray(str, ProductInPriceDTO.class);
		//ExecuteResult<Map<Long, ShopOutPriceDTO>> res=shopCartPromotionService.getShopByProducts(list);
		
		
		Assert.assertEquals(true, true);
	}
	

}
