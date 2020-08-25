package com.camelot.sellercenter.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;

public class MallBannerExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MallBannerExportServiceImplTest.class);
	ApplicationContext ctx = null;
	MallBannerExportService mallBannerExportService = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallBannerExportService = (MallBannerExportService) ctx.getBean("mallBannerExportService");
	}

	@Test
	public void queryMallBannerListTest(){
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(6);
		MallBannerDTO mallBannerDTO = new MallBannerDTO();
		mallBannerDTO.setBannerType(2);
		DataGrid<MallBannerDTO> banners =mallBannerExportService.queryMallBannerList(mallBannerDTO,"1", page);
		System.out.println(JSON.toJSONString(banners));
	}
	@Test
	public void addMallBannerListTest(){
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(6);
		MallBannerInDTO mallBannerInDTO = new MallBannerInDTO();
		mallBannerInDTO.setTitle("轮播图1");
		mallBannerInDTO.setBannerType(1);
		mallBannerInDTO.setBannerLink("www.baidu.com");
		mallBannerInDTO.setBannerUrl("1234543");
		mallBannerInDTO.setSortNumber(222);
		mallBannerInDTO.setTimeFlag("213123");
		mallBannerInDTO.setThemeId(111);
		ExecuteResult<String> result =mallBannerExportService.addMallBanner(mallBannerInDTO);//.queryMallBannerList("1", page);
		System.out.println(JSON.toJSONString(result));
	}
	@Test
   public void testqueryReportList(){
		AdReportInDto mallAdCountDTO=new AdReportInDto();
		//mallBannerDTO.setMallAdName("轮播");
		mallAdCountDTO.setClickDateBegin("2015-06-01");
		mallAdCountDTO.setClickDateEnd("2015-06-03");
		mallAdCountDTO.setDateFormat("yyyy-MM-dd");
		DataGrid<AdReportOutDto> result=mallBannerExportService.queryReportList(mallAdCountDTO,null);
		LOGGER.info("\n 操作结果{},信息{}",result.getTotal()>0, JSONObject.toJSONString(result));
   }
	@Test
   public void testmotifyMallBannerStatus(){
		Long[] ids = {(long) 32,(long) 33};
		ExecuteResult<String> result=mallBannerExportService.motifyMallBannerStatusBatch("0",ids);
		LOGGER.info("\n 操作结果{},信息{}",result, JSONObject.toJSONString(result));
   }
	@Test
	public void testautoBatchOffShelves(){
		ExecuteResult<Integer> result=mallBannerExportService.autoBatchOffShelves();
		LOGGER.info("\n 操作结果{},信息{}",result, JSONObject.toJSONString(result));
	}
}
