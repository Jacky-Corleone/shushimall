package com.camelot.sellercenter.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;

/**
 * 
 * <p>Description: [楼层基本信息的单元测试 ]</p>
 * Created on 2015年1月30日
 * @author  <a href="mailto: xxx@camelotchina.com">陈霞</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class MallRecServiceClientImplTest_AllTest{
	private static final Logger LOGGER = LoggerFactory.getLogger(MallRecServiceClientImplTest_AllTest.class);
	
	ApplicationContext ctx = null;
	MallRecExportService mallRecExportService = null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		mallRecExportService = (MallRecExportService) ctx.getBean("mallRecExportService");
	}

	/**
	 * 
	 * <p>Discription:[根据id查询详情]</p>
	 * Created on 2015年1月30日
	 * @author:[chenx]
	 */
	@Test
	public void testgetMallRecById() {
		//根据id查询详情
		MallRecDTO mallRecDTO = mallRecExportService.getMallRecById(113l);
		LOGGER.info(mallRecDTO.getTitleDTO());
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:单元测试 新增楼层]</p>
	 * Created on 2015年1月22日
	 * @author:[chenx]
	 */
	@Test
	public void testaddMallRec() {
		//Unknown table 'SEQ_mall_recommend_ID' in field list
		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setCategoryIdDTO(123l);
		mallRecDTO.setFloorNumDTO(121);
		mallRecDTO.setRecTypeDTO(123);
		mallRecDTO.setTitleDTO("title");
		//Data too long for column 'smalltitle' at row 1
		mallRecDTO.setSmalltitleDTO("nih");
		mallRecDTO.setThemeId(1);
		mallRecExportService.addMallRec(mallRecDTO);
		LOGGER.info(mallRecDTO.getSmalltitleDTO());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testqueryMallRecList() {
		MallRecDTO mallRecDTO = new MallRecDTO();
		//mallRec.setFloorNum(1);;
		//分页类
		Pager pager = new Pager();
		//设置当前页的起始记录
		pager.setPageOffset(2);
		//设置每页显示的记录数
		pager.setRows(10);
		//设置当前页
		pager.setPage(1);
		//mallRecDTO.setTitleDTO("test");
//		mallRecDTO.setTitleDTO("TEST");
		mallRecDTO.setRecTypeDTO(1);
		DataGrid<MallRecDTO> size = mallRecExportService.queryMallRecList(mallRecDTO, pager);
		LOGGER.info("" + size.getTotal());
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述：单元测试  根据id修改楼层的信息]</p>
	 * Created on 2015年1月22日
	 * @author:[chenx]
	 */
	@Test
	public void testmodifyMallRec() {
		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setFloorNumDTO(111);
		mallRecDTO.setIdDTO(120l);
		mallRecDTO.setRecTypeDTO(123);
		mallRecExportService.modifyMallRec(mallRecDTO);
		LOGGER.info(mallRecDTO.getSmalltitleDTO());
	}

	@Test
	public void testupdateStatusByFloorType() {
		mallRecExportService.updateStatusByFloorType("0");
	}
	
	/**
	 * 
	 * <p>Discription:[楼层上下架的单元测试]</p>
	 * Created on 2015年1月30日
	 * @author:[chenx]
	 */
	@Test
	public void testmodifyMallRecStatus() {
		ExecuteResult<String> executeResult = mallRecExportService.modifyMallRecStatus(110l, "1");
		LOGGER.info("{}" + executeResult);
	}
}
