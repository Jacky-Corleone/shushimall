package com.camelot.usercenter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;
import com.camelot.usercenter.service.FieldIdentificationPictureService;


public class FieldIdentificationPictureServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(FieldIdentificationPictureServiceTest.class);
	ApplicationContext ctx = null;
	FieldIdentificationPictureService pictureService = null;

	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		pictureService = (FieldIdentificationPictureService) ctx.getBean("fieldIdentificationPictureService");
	}

	@Ignore
	public void addPictureTest() {
    	FieldIdentificationPictureDTO pictureDTO = new FieldIdentificationPictureDTO();
    	pictureDTO.setUserId(1000000861L);
    	pictureDTO.setExtendId(1001175L);
    	pictureDTO.setPictureType(0);
    	pictureDTO.setPictureSrc("www.baidu.com");
    	pictureDTO.setUploadorId("2");
        pictureService.addPicture(pictureDTO);
	}

	@Test
	public void addfindPictureListByCondition() {
		FieldIdentificationPictureDTO pictureDTO = new FieldIdentificationPictureDTO();
		pictureDTO.setUserId(1000000856L);
		pictureDTO.setExtendId(1001172L);
		DataGrid<FieldIdentificationPictureDTO> dataGrid=pictureService.findPictureListByCondition(pictureDTO, null);
		LOGGER.info("操作方法{}，结果信息{}","addfindPictureListByCondition", dataGrid.getTotal());
	}

}
