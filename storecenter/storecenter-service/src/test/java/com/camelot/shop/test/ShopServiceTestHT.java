package com.camelot.shop.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.storecenter.dto.ShopFareDTO;
import com.camelot.storecenter.service.ShopFareExportService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;
import com.camelot.storecenter.service.ShopExportService;

import javax.annotation.Resource;

public class ShopServiceTestHT {
	Logger LOG = LoggerFactory.getLogger(ShopServiceTestHT.class);
	ApplicationContext ctx = null;
	ShopExportService shopExportService = null;
    ShopFareExportService shopFareExportService = null;
    RedisDB redisDB = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		shopExportService = (ShopExportService) ctx.getBean("shopExportService");
        shopFareExportService = (ShopFareExportService)ctx.getBean("shopFareExportService");
        redisDB = (RedisDB) ctx.getBean("redisDB");
	}
	@Test
	public void testSaveShopInfo() throws Exception {
		ShopDTO shopDTO=new ShopDTO();
		ExecuteResult<String> result = shopExportService.saveShopInfo(shopDTO);
		Assert.assertEquals(true, result.isSuccess());
	}

    @Test
    public void testQueryShopFareList(){
        ShopFareDTO sfdto = new ShopFareDTO();
        sfdto.setFareRegion("0");
        sfdto.setShopId(2000000156L);
        DataGrid<ShopFareDTO> result = shopFareExportService.queryShopFareList(sfdto, null);
        LOG.info(result.getRows().toString());
    }

	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testFindShopInfoById() throws Exception {
		 ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(1l);
		 Assert.assertEquals(true, result.isSuccess());
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFindShopInfoByCondition() throws Exception {
		Pager pager=new Pager();
		ShopDTO shopDTO=new ShopDTO();
		Integer[] in=new Integer[]{2,5};
		shopDTO.setStatuss(in);
		shopDTO.setCollation(2);
		ExecuteResult<DataGrid<ShopDTO>> result = shopExportService.findShopInfoByCondition(shopDTO, pager);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void testmodifyShopInfostatus(){
		Long shopId=1l;
		int status=3;
		ExecuteResult<String> result = shopExportService.modifyShopInfostatus(shopId, status);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryBYIdsTest(){
	  ShopAudiinDTO ss=new ShopAudiinDTO();
/*	  Long[] ids={1L,2L,3L};
	  ss.setShopIds(ids);*/
	  ExecuteResult<List<ShopDTO>> result=shopExportService.queryShopInfoByids(ss);
	  Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopInfoUpdateTest(){
		ShopInfoModifyInDTO shopInfoModifyInDTO=new ShopInfoModifyInDTO();
		ShopDTO shopDTO=new ShopDTO();
		shopDTO.setShopId(2000000204l);
		shopDTO.setSellerId(674l);
		shopDTO.setShopName("11111");
		shopDTO.setLogoUrl("logo/url");
		shopDTO.setKeyword("铁锅，铁燥改改改");
		shopDTO.setIntroduce("我们家的商品质量很不错改改改");
		shopDTO.setMainSell("毒品、强制");
		shopDTO.setPriceMin(BigDecimal.valueOf(23.00));
		shopDTO.setMountMin(11l);
		shopDTO.setMutilPrice(3);
		shopDTO.setMutilCondition(1);
		shopDTO.setInitialPrice(BigDecimal.valueOf(12.00));
		shopDTO.setInitialMount(22l);
		shopDTO.setInitialCondition(1);
		shopDTO.setCityCode("2203");
		shopDTO.setCityName("22市");
		shopDTO.setProvinceCode("22");
		shopDTO.setProvinceName("22省");
		shopDTO.setDistrictCode("220303");
		shopDTO.setDistrictName("223区");
		shopDTO.setZcode("44");
		shopDTO.setStreetName("十八街");
		shopInfoModifyInDTO.setShopDTO(shopDTO);
		
		List<ShopBrandDTO> shopBrandList=new  ArrayList<ShopBrandDTO>();
		 ShopBrandDTO sb=new ShopBrandDTO();
		 sb.setBrandId(4l);
		 sb.setStatus(1);
		 sb.setCid(1l);
		 shopBrandList.add(sb);
		 shopInfoModifyInDTO.setShopBrandList(shopBrandList);
		 
		 List<ShopCategoryDTO> shopCategoryList=new  ArrayList<ShopCategoryDTO>();
		 ShopCategoryDTO sc=new ShopCategoryDTO();
		 sc.setStatus(1);
		 sc.setCid(3l);
		 shopCategoryList.add(sc);
		 shopInfoModifyInDTO.setShopCategoryList(shopCategoryList);
		ExecuteResult<String> result = shopExportService.modifyShopInfoUpdate(shopInfoModifyInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopInfoTest(){
		ShopInfoModifyInDTO shopInfoModifyInDTO=new ShopInfoModifyInDTO();
		ShopDTO shopDTO=new ShopDTO();
		//shopDTO.setareaCode": "",
		shopDTO.setCityCode("1101");
		shopDTO.setCityName("市辖区");
		shopDTO.setCollation(1);
		shopDTO.setComment(null);
		shopDTO.setCreated(new Date());
		shopDTO.setDistrictCode("110117");
		shopDTO.setDistrictName("平谷区");
		shopDTO.setExtensionNumber(null);
		shopDTO.setInitialCondition(1);
		shopDTO.setInitialMount(1l);
		shopDTO.setInitialPrice(BigDecimal.valueOf(1));
		shopDTO.setIntroduce("店铺信息审核");
		shopDTO.setKeyword(null);
		shopDTO.setLandline(null);
		shopDTO.setLogoUrl("");
		shopDTO.setMainSell("店铺信息审核");
		shopDTO.setMobile(13144443333l);
		shopDTO.setModified(new Date());
		shopDTO.setModifyStatus(1);
		shopDTO.setMountMin(10l);
		shopDTO.setMutilCondition(1);
		shopDTO.setMutilPrice(1);
		shopDTO.setPassTime(new Date());
		shopDTO.setPriceMin(BigDecimal.valueOf(1.5));
		shopDTO.setProvinceCode("11");
		shopDTO.setProvinceName("北京市");
		shopDTO.setRunStatus(1);
		shopDTO.setSellerId(1000000316l);
		shopDTO.setShopId(2000000297l);
		shopDTO.setShopName("我的店铺");
		shopDTO.setStatus(1);
		shopDTO.setStreetName("发货（退货）地址");
		shopDTO.setZcode("1");
		
		
		/* List<ShopBrandDTO> shopBrandList=new  ArrayList<ShopBrandDTO>();
		 ShopBrandDTO sb=new ShopBrandDTO();
		 sb.setBrandId(4l);
		 sb.setStatus(1);
		 sb.setCid(1l);
		 shopBrandList.add(sb);
		 shopInfoModifyInDTO.setShopBrandList(shopBrandList);
		 
		 List<ShopCategoryDTO> shopCategoryList=new  ArrayList<ShopCategoryDTO>();
		 ShopCategoryDTO sc=new ShopCategoryDTO();
		 sc.setStatus(1);
		 sc.setCid(3l);
		 shopCategoryList.add(sc);
		 shopInfoModifyInDTO.setShopCategoryList(shopCategoryList);*/
		 
/*		shopDTO.setShopType(2);
		shopDTO.setBrandType(2);
		shopDTO.setDisclaimer("/setDisclaimer");
		shopDTO.setTrademarkRegistCert("/setTrademarkRegistCert");
		shopDTO.setInspectionReport("/setInspectionReport");
		shopDTO.setProductionLicense("/setProductionLicense");
		shopDTO.setMarketingAuth("/setMarketingAuth");
		shopDTO.setFinancing(2);
		shopDTO.setFinancingAmt("1000");*/
		shopInfoModifyInDTO.setShopDTO(shopDTO);
		ExecuteResult<String> result = shopExportService.modifyShopInfo(shopInfoModifyInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopRunStatusTest(){
		ExecuteResult<String> result = shopExportService.modifyShopRunStatus(2000000267l,1);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopInfoAndcbstatusTest(){
		ShopDTO shopDTO=new ShopDTO();
		shopDTO.setShopId(2000000130l);
		shopDTO.setStatus(2);
		shopDTO.setComment("222");
		shopDTO.setPlatformUserId(1l);
		ExecuteResult<String> result = shopExportService.modifyShopInfoAndcbstatus(shopDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopstatusAndRunstatusTest(){
		ExecuteResult<String> result=shopExportService.modifyShopstatusAndRunstatus(2000000088l);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void modifyShopstatusAndRunstatusBackTest(){
		ExecuteResult<String> result=shopExportService.modifyShopstatusAndRunstatusBack(2000000088l);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryShopInfoByBrandIdTest(){
		Pager page=new Pager();
		ExecuteResult<DataGrid<ShopDTO>> result = shopExportService.queryShopInfoByBrandId(272l,page);
		Assert.assertEquals(true, result.isSuccess());
	}

    @Test
    public void addSecondDomainToRedisTest(){
        //ExecuteResult<String> result = shopExportService.addSecondDomainToRedis();
        //ExecuteResult<String> result = shopExportService.addSecondDomainToRedis("zll", 100001L);
        //Assert.assertEquals("success", result.getResultMessage());
        Object obj = redisDB.getObject("second_domain_key");
        if(null != obj){
            Map<String, String> secondDomainMap = (Map<String, String>)obj;
            System.out.println(secondDomainMap);
        }
        System.out.println(obj);
    }
    
    @Test
    public void findAllKeyinShopInfoTest(){
    	ExecuteResult<List<ShopDTO>> result = shopExportService.findAllKeyinShopInfo();
        Assert.assertEquals("success", result.getResultMessage());
    }
}
