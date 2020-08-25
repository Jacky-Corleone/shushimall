package com.camelot.sellercenter.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
import com.camelot.sellercenter.domain.MallTheme;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;

public class MallThemeServiceImplTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MallThemeServiceImplTest.class);
	ApplicationContext ctx = null;
	MallThemeService mallThemeService = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallThemeService = (MallThemeService) ctx.getBean("mallThemeService");
	}
	
	@Test
	public void queryGroupCityCode() {
		String[] code = mallThemeService.queryGroupCityCode();
		System.out.println(code.length);
	}

	@Test
	public void queryMallThemeListTest1() {
		MallThemeDTO dto = new MallThemeDTO();
		dto.setId(1);
		Pager p = new Pager();
		p.setRows(5);
		p.setPage(1);
		mallThemeService.queryMallThemeList(dto, "1", p);
		System.out.println("queryMallThemeList.......");
	}

	@Test
	public void queryMallThemeListTest() {
		Pager p = new Pager();
		p.setRows(5);
		p.setPage(1);
		MallThemeDTO dto = new MallThemeDTO();
		//dto.setAddressId(2L);
		//dto.setcId(3L);
		dto.setType(1);
		//dto.setClev(5);
		//dto.setColorb("#123211");
		//dto.setCreated(Date.valueOf("2015-10-30"));
		//dto.setUserId("1");
		DataGrid<MallThemeDTO> dg = mallThemeService.queryMallThemeList(dto,"1",p);
		
		System.out.println("queryMallThemeList.......get(0).getId():"+dg.getRows().size());
	}

	@Test
	public void getMallThemeByIdTest() {
		MallThemeDTO dto = mallThemeService.getMallThemeById(1);
		System.out.println("queryMallThemeList.......dto:"+dto.toString());
	}
	
	@Test
	public void updateStatusByIdTest() {
		MallThemeDTO dto = new MallThemeDTO();
		dto.setAddressId(2L);
		dto.setcId(5L);
		//MallTheme mt = new MallTheme();
		dto.setType(1);
		dto.setId(1);
		dto.setColorb("111212");
		dto.setThemeName("修改测试11-11");
		ExecuteResult<String> er= mallThemeService.motifyMallTheme(dto);
		System.out.println("queryMallThemeList.......dto:"+er.getResultMessage());
	}

	@Test
	public void addMallThemeTest() {
		MallThemeDTO dto = new MallThemeDTO();
		dto.setThemeName("测试主题1");
		dto.setAddressId(2L);
		dto.setcId(3L);
		dto.setPrimaryCid(1l);
		dto.setClev(5);
		dto.setStatus(1);
		dto.setColor("#123211");
		dto.setColorb("22211");
		dto.setType(1);
		dto.setCreated(Date.valueOf("2015-10-30"));
		dto.setSortNum(1);
		mallThemeService.addMallTheme(dto);
		System.out.println("addMallTheme.......");
	}
	
	@Test
	public void testmotifyMallThemeStatus(){
		mallThemeService.motifyMallThemeStatus(2L, "0");
	}
	
}
