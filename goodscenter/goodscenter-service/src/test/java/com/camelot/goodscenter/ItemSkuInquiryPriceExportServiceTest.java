package com.camelot.goodscenter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月12日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ItemSkuInquiryPriceExportServiceTest {
	
	ItemSkuInquiryPriceExportService itemSkuInquiryPriceExportService = null; 
	ApplicationContext ctx = null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemSkuInquiryPriceExportService = (ItemSkuInquiryPriceExportService) ctx.getBean("itemSkuInquiryPriceExportService");
	}

	@Test
	public void queryListTest(){
		Pager page=new Pager();
		ItemSkuInquiryPriceDTO dto=new ItemSkuInquiryPriceDTO();
		//dto.setShopId(L);
		//dto.setBuyerId(574L);
//		dto.setSellerId(507L);
	//	dto.setItemName("ss");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
////			dto.setStartTime(sdf.parse("2015-03-26"));
////			dto.setEndTime(sdf.parse("2015-04-31"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> er= itemSkuInquiryPriceExportService.queryList(dto, page);
	//	System.out.println(i.getSkuId()+"////");
		for(ItemSkuInquiryPriceDTO d:er.getResult().getRows()){
			 System.out.println(d.getItemName()+d.getCreated());
		}
	    System.out.println(er.getResult().getTotal());
	   System.out.println(er.getResultMessage());
	}
	@Test
	public void addTest(){
	  ItemSkuInquiryPriceDTO dto=new ItemSkuInquiryPriceDTO();
	  dto.setSkuId(23L);
	  dto.setBuyerId(11L);
	  dto.setSellerId(11L);
	  dto.setShopId(11L);
	  dto.setItemId(211L);
	  dto.setInquiryQty(100);
	  dto.setInquiryPrice(new BigDecimal(56));
	  dto.setComment("备注");
	  dto.setInquiryRemark("新增测试");
	  dto.setCellphone("18034677890");
	  dto.setEmail("234567@qq.com");
	  ExecuteResult<String> str=itemSkuInquiryPriceExportService.addItemSkuInquiryPrice(dto);
	 System.out.println(str.getResultMessage()+str.getResult());
	}
	
	@Test
	public void modifyTest(){
	  ItemSkuInquiryPriceDTO dto=new ItemSkuInquiryPriceDTO();
	  dto.setId(17L);
	  dto.setInquiryPrice(new BigDecimal(13500));
	  dto.setInquiryRemark("modifyItemSkuInquiryPrice测试");
	  dto.setStartTime(new Date());
	  dto.setEndTime(new Date());
	  ExecuteResult<String> str=itemSkuInquiryPriceExportService.modifyItemSkuInquiryPrice(dto);
	  System.out.println(str.getResult());
	}
	@Test
	public void queryTest(){
		ItemSkuInquiryPriceDTO dto=new ItemSkuInquiryPriceDTO();
		dto.setBuyerId(507L);
		dto.setSellerId(507L);
		dto.setSkuId(1000000253L);
		dto.setItemId(1000000194L);
		ExecuteResult<ItemSkuInquiryPriceDTO> er=itemSkuInquiryPriceExportService.queryByIdsAndNumber(dto);
	  System.out.println(er.getResult());
	}
}
