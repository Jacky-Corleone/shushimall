package com.camelot.sellercenter.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.malladvertise.dto.MallAdCountDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;

public class MallAdExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MallAdExportServiceImplTest.class);
	ApplicationContext ctx = null;
	MallAdExportService mallAdvService = null;
	public static final String DATEFORMAT = "yyyy-MM-dd";
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallAdvService = (MallAdExportService) ctx.getBean("mallAdExportService");
	}

	@Test
	public void testdeleteById() {
		 mallAdvService.deleteById(155555l);
	}
	
//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testQueryMallAdList() {
//		Pager page=new Pager();
//		MallAdQueryDTO mallAdQueryDTO=new MallAdQueryDTO();
////		mallAdQueryDTO.setStatus(0);
//		//mallAdQueryDTO.setTimeFlag(2);
//		//mallAdQueryDTO.setStartTime(this.strToDate("2015-03-24",null));
//		//mallAdQueryDTO.setEndTiem(this.strToDate("2015-03-24",null));
////		mallAdQueryDTO.setTimeFlag(1);
//		mallAdQueryDTO.setAdvType(2);
//		DataGrid<MallAdDTO> res=	mallAdvService.queryMallAdList(page, mallAdQueryDTO);
//		LOGGER.info("操作方法{}，结果信息{}","testQueryMallAdList", JSONObject.toJSONString(res));
//	}
//	
//	@Test
//	public void testGetMallAdById() {
//		MallAdDTO res = mallAdvService.getMallAdById(1l);
//		LOGGER.info("操作方法{}，结果信息{}","testGetMallAdById", JSONObject.toJSONString(res));
//	}
//
//	@Test
//	public void testAddMallAd() {
//		
//		MallAdInDTO mallAdInDTO=new MallAdInDTO();
//		mallAdInDTO.setAdType(3);
//		mallAdInDTO.setCid(2l);
//		mallAdInDTO.setAdURL("ssds");
//		mallAdInDTO.setClose(2);
//		mallAdInDTO.setThemeId(1);
//		mallAdInDTO.setAdType(1);
//		mallAdInDTO.setAdvType(1);
//		mallAdvService.addMallAd(mallAdInDTO);
//	}
//
//	@Test
//	public void testModifyMallBanner() {
//		MallAdInDTO mallAdInDTO=new MallAdInDTO();
//		mallAdInDTO.setId(54l);
//		mallAdInDTO.setAdType(3);
//		mallAdInDTO.setCid(2l);
//		mallAdInDTO.setAdURL("ssdddd.ds");
//		mallAdInDTO.setClose(1);
//		mallAdvService.modifyMallBanner(mallAdInDTO);
//	}
//
//	@Test
//	public void testModifyMallAdStatus() {
//	}
//	
//	@Test
//	public void testsaveMallAdCount(){
//		try {
//			ExecuteResult<MallAdCountDTO> result = mallAdvService.saveMallAdCount(12l,1l);
//			LOGGER.info("\n 操作结果{},信息{}", result.isSuccess(), JSONObject.toJSONString(result));
//		} catch (Exception e) {
//			LOGGER.info("\n 操作异常,信息{}",e.getMessage());
//		}
//	}
//
//	@Test
//	public void testfindMallAdCountById(){
//		MallAdCountDTO result = mallAdvService.findMallAdCountById(6l);
//		LOGGER.info("\n 操作结果{},信息{}", result!=null, JSONObject.toJSONString(result));
//	}
//	@Test
//	public void testfindAdCountList(){
//		MallAdCountDTO mallAdCountDTO =new MallAdCountDTO();
//		
////		List<Long> ids=new ArrayList<Long>();
////		ids.add(1l);
////		mallAdCountDTO.setIds(ids);
//		
//		mallAdCountDTO.setClickDateBegin("2015-05-11");
//		mallAdCountDTO.setClickDateEnd("2015-05-11");
//		mallAdCountDTO.setAdCountMin(2l);
//		mallAdCountDTO.setAdCountMax(4l);
//		DataGrid<MallAdCountDTO> result =mallAdvService.findAdCountList(mallAdCountDTO,null);
//		LOGGER.info("\n 操作结果{},信息{}", result.getTotal()>0, JSONObject.toJSONString(result));
//	}
//	
//	public static Date strToDate(String dateStr,String formatType) {
//		try {
//			if(StringUtils.isBlank(dateStr))return null;
//			if (StringUtils.isBlank(formatType))formatType=DATEFORMAT;
//			DateFormat sdf = new SimpleDateFormat(formatType);
//			return sdf.parse(dateStr);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	@Test
//	public void testqueryReportList(){
//		AdReportInDto mallAdCountDTO =new AdReportInDto();
//		mallAdCountDTO.setClickDateBegin("2015-06-01");
//		mallAdCountDTO.setClickDateEnd("2015-06-03");
//		mallAdCountDTO.setDateFormat("yyyy-MM-dd");
//		DataGrid<AdReportOutDto> result =mallAdvService.queryReportList(mallAdCountDTO, null);
//		LOGGER.info("\n 操作结果{},信息{}",result.getTotal()>0, JSONObject.toJSONString(result));
//	}
}
