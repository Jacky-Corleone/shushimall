package com.camelot.goodscenter;

import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.dto.TranslationOrderDTO;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TranslationExportServiceTest {

	TranslationExportService translationExportService = null;
	ApplicationContext ctx = null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		translationExportService = (TranslationExportService) ctx.getBean("translationExportService");
	}

	@Test
	public void queryByTranslationInfo(){
		TranslationInfoDTO dto=new TranslationInfoDTO();
		dto.setTranslationNo("11");
		ExecuteResult<TranslationInfoDTO> er= translationExportService.queryByTranslationInfo(dto);
	    System.out.println(er.getResult());
		System.out.println(er.getResult().getTranslationNo());
	   System.out.println(er.getResultMessage());
	}

	@Test
	public void queryTranslationInfoList(){
			Pager page=new Pager();
			TranslationInfoDTO dto=new TranslationInfoDTO();
		List<String> list=new ArrayList<String>();
		list.add("2");
		list.add("3");
		dto.setStatusList(list);
			ExecuteResult<DataGrid<TranslationInfoDTO>> er= translationExportService.queryTranslationInfoList(dto, page);
			for(TranslationInfoDTO d:er.getResult().getRows()){
				System.out.println(d.getTranslationName());
				System.out.println(d.getCreateBy());
				System.out.println(d.getCreateDate());
			}
			System.out.println(er.getResult().getTotal());
			System.out.println(er.getResultMessage());
	}
	@Test
	public void queryTranslationInfoPager(){
		Pager page=new Pager();
		TranslationInfoDTO dto=new TranslationInfoDTO();
		List<String> list=new ArrayList<String>();
		list.add("2");
		list.add("3");
		dto.setStatusList(list);
		ExecuteResult<DataGrid<Map>> er= translationExportService.queryTranslationInfoPager(dto, page);
		for(Map d:er.getResult().getRows()){
			System.out.println(d.get("itemName"));
		}
		System.out.println(er.getResult().getTotal());
		System.out.println(er.getResultMessage());
	}
	@Test
	public void queryByTranslationMat(){
		TranslationMatDTO dto=new TranslationMatDTO();
		dto.setTranslationNo("10001");
		ExecuteResult<TranslationMatDTO> er= translationExportService.queryByTranslationMat(dto);
		System.out.println(er.getResult());
		System.out.println(er.getResult().getMatDesc());
		System.out.println(er.getResultMessage());
	}

	@Test
	public void queryTranslationMatList(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Pager page=new Pager();
		TranslationMatDTO dto=new TranslationMatDTO();
		dto.setTranslationNo("10001");
//		try {
//			dto.setBeginDate(sdf.parse("2015-06-07"));
//			dto.setEndDate(sdf.parse("2015-06-07"));
//		}catch (Exception e){
//
//		}
		ExecuteResult<DataGrid<Map>> er= translationExportService.queryTranslationMatList(dto, page);
		for(Map d:er.getResult().getRows()){
			System.out.println(d.get("MatDesc"));
		}
		System.out.println(er.getResult().getTotal());
		System.out.println(er.getResultMessage());
	}
	@Test
	public void queryByTranslationOrder(){
		TranslationOrderDTO dto=new TranslationOrderDTO();
		dto.setTranslationNo("10001");
		ExecuteResult<TranslationOrderDTO> er= translationExportService.queryByTranslationOrder(dto);
		System.out.println(er.getResult());
		System.out.println(er.getResult().getOrderNo());
		System.out.println(er.getResultMessage());
	}

	@Test
	public void queryTranslationOrderList(){
		Pager page=new Pager();
		TranslationOrderDTO dto=new TranslationOrderDTO();
		dto.setTranslationNo("11111");
		ExecuteResult<DataGrid<TranslationOrderDTO>> er= translationExportService.queryTranslationOrderList(dto, page);
		for(TranslationOrderDTO d:er.getResult().getRows()){
			System.out.println(d.getOrderNo());
		}
		System.out.println(er.getResult().getTotal());
		System.out.println(er.getResultMessage());
	}
	@Test
	public void addTranslationInfo(){

		TranslationInfoDTO dto=new TranslationInfoDTO();
//		dto.setTranslationNo("XJ2015062300001");
//		dto.setTranslationName("徐杰来袭求购男");
//		dto.setPrinterId(1000000639);//印刷厂用户主键
//		dto.setSupplierId(1000000639);//供货方主键
//		dto.setBeginDate(new Date());//合同有效期--开始
//		dto.setEndDate(new Date());//合同有效期--结束
//		dto.setStatus("1");//合同状态
//		dto.setRemarks("徐杰来袭求购男");
//		dto.setMatCd("1000000006");
//		dto.setCreateBy("1000000639");//
//		dto.setCreateDate(new Date());//
//		dto.setUpdateBy("1000000639");//
//		dto.setUpdateDate(new Date());//
//		dto.setActiveFlag("0");//有效标记 0-有效 1-无效
//		ExecuteResult<String> str=translationExportService.addTranslationInfo(dto);
//		System.out.println(str.getResultMessage()+str.getResult());
//
//		dto.setTranslationNo("XJ2015062300002");
//		dto.setTranslationName("徐杰来袭求购女");
//		dto.setPrinterId(1000000639);//印刷厂用户主键
//		dto.setSupplierId(1000000639);//供货方主键
//		dto.setBeginDate(new Date());//合同有效期--开始
//		dto.setEndDate(new Date());//合同有效期--结束
//		dto.setStatus("1");//合同状态
//		dto.setRemarks("徐杰来袭求购男");
//		dto.setMatCd("1000000005");
//		dto.setCreateBy("1000000639");//
//		dto.setCreateDate(new Date());//
//		dto.setUpdateBy("1000000639");//
//		dto.setUpdateDate(new Date());//
//		dto.setActiveFlag("0");//有效标记 0-有效 1-无效
//		translationExportService.addTranslationInfo(dto);

		dto.setTranslationNo("QG000001");
		dto.setTranslationName("徐杰来袭求购男女");
		dto.setPrinterId(1000000639);//印刷厂用户主键
		dto.setSupplierId(1000000639);//供货方主键
		dto.setBeginDate(new Date());//合同有效期--开始
		dto.setEndDate(new Date());//合同有效期--结束
		dto.setStatus("1");//合同状态
		dto.setRemarks("徐杰来袭求购男");
		dto.setMatCd("1000000008");
		dto.setCreateBy("1000000639");//
		dto.setCreateDate(new Date());//
		dto.setUpdateBy("1000000639");//
		dto.setUpdateDate(new Date());//
		dto.setActiveFlag("1");//有效标记 0-有效 1-无效
		dto.setCategoryId(388);
		translationExportService.addTranslationInfo(dto);

	}

	@Test
	public void addTranslationMat(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TranslationMatDTO dto=new TranslationMatDTO();
		dto.setTranslationNo("10001");
		dto.setMatCd("1000001");//物料号
		dto.setMatDesc("Junit测试");//物料描述
		dto.setLable1Cd("10");//类别1
		dto.setLable1Desc("Junit测试");//类别1描述
		dto.setLable2Cd("1010");//类别2
		dto.setLable2Desc("Junit测试");//类别2描述
		dto.setLable3Cd("101010");//类别3
		dto.setLable3Desc("Junit测试");//类别3描述
		dto.setMatSpec("Junit测试");//物料规格
		dto.setMatBrand("Junit测试");//品牌
		dto.setMatDiscount("5");//折扣
		dto.setMatPrice(100.00);//价格
		dto.setMatUnit("PC");//单位
		dto.setCreateBy("1");//创建人
		dto.setPrinterId(1010);
		dto.setSupplierId(1011);
		try {
			dto.setBeginDate(sdf.parse("2015-06-07"));
			dto.setEndDate(sdf.parse("2015-06-07"));
		}catch (Exception e){

		}
		dto.setCreateDate(new Date());//创建日期
		dto.setUpdateBy("1");//修改人
		dto.setUpdateDate(new Date());//修改时间
		dto.setActiveFlag("1");//有效标记 0-有效 1-无效
		dto.setCategoryId(388);
		ExecuteResult<String> str=translationExportService.addTranslationMat(dto);
		System.out.println(str.getResultMessage()+str.getResult());
	}
	@Test
	public void addTranslationOrder(){
		TranslationOrderDTO dto=new TranslationOrderDTO();
		dto.setTranslationNo("10001");
		dto.setOrderNo("00000101");
		dto.setCreateBy("1");//创建人
		dto.setCreateDate(new Date());//创建日期
		dto.setUpdateBy("1");//修改人
		dto.setUpdateDate(new Date());//修改时间
		dto.setRemark("Junit测试");
		dto.setActiveFlag("0");//有效标记 0-有效 1-无效
		ExecuteResult<String> str=translationExportService.addTranslationOrder(dto);
		System.out.println(str.getResultMessage()+str.getResult());
	}
	@Test
	public void modifyTranslationInfo(){
		TranslationInfoDTO dto=new TranslationInfoDTO();
		dto.setId(5L);
		dto.setActiveFlag("1");
		dto.setMatAttribute("测试测试");
		ExecuteResult<String> str=translationExportService.modifyTranslationInfo(dto);
		System.out.println(str.getResult());
	}

	@Test
	public void modifyTranslationMat(){
		TranslationMatDTO dto=new TranslationMatDTO();
		dto.setId(13L);
		dto.setMatPrice(200.00);
		ExecuteResult<String> str=translationExportService.modifyTranslationMat(dto);
		System.out.println(str.getResult());
	}

	@Test
	public void modifyTranslationOrder(){
		TranslationOrderDTO dto=new TranslationOrderDTO();
		dto.setId(1L);
		dto.setActiveFlag("1");
		ExecuteResult<String> str=translationExportService.modifyTranslationOrder(dto);
		System.out.println(str.getResult());
	}
	
	@Test
	public void updateByTranslationNo(){
		TranslationInfoDTO dto=new TranslationInfoDTO();
		dto.setSupplierId(1000000004);
		dto.setCreateBy(1000000004+"");
		dto.setTranslationNo("QG201511230930113141");
		dto.setUpdateDate(new Date());
		ExecuteResult<String> str=translationExportService.updateByTranslationNo(dto);
	}

	@Test
	public void Test(){
		ExecuteResult<String> str=translationExportService.createTranslationNo();
		System.out.println(str.getResult());
	}

}
