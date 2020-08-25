package com.camelot.tradecenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.MonthlyStatementDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.MonthlyStatementExportService;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MonthlyStatementExportServiceTest {

	MonthlyStatementExportService monthlyStatementExportService = null;
	ApplicationContext ctx = null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		monthlyStatementExportService = (MonthlyStatementExportService) ctx.getBean("monthlyStatementExportService");
	}

	@Test
	public void queryByMonthlyStatement(){
		MonthlyStatementDTO dto=new MonthlyStatementDTO();
		dto.setOrderCode("100001");
		ExecuteResult<MonthlyStatementDTO> er= monthlyStatementExportService.queryByMonthlyStatement(dto);
	    System.out.println(er.getResult());
		System.out.println(er.getResult().getTradeName());
	   System.out.println(er.getResultMessage());
	}

	@Test
	public void queryMonthlyStatementList(){
			Pager page=new Pager();
			MonthlyStatementDTO dto=new MonthlyStatementDTO();
			dto.setOrderCode("100001");
			dto.setCreateDate(new Date());
			dto.setId(100000021L);
		ExecuteResult<DataGrid<MonthlyStatementDTO>> er= monthlyStatementExportService.queryMonthlyStatementList(dto, page);
			for(MonthlyStatementDTO d:er.getResult().getRows()){
				System.out.println(d.getTradeName());
				System.out.println(d.getCreateBy());
				System.out.println(d.getCreateDate());
			}
			System.out.println(er.getResult().getTotal());
			System.out.println(er.getResultMessage());
	}
	@Test
	public void addMonthlyStatement(){

		MonthlyStatementDTO dto=new MonthlyStatementDTO();
		dto.setTradeName("JUINT测试");//交易名称
		dto.setOrderCode("100001");//订单号
		dto.setOrderStates(1);//订单状态
		dto.setPaidAmount(11.11);//已付金额
		dto.setNpaidAmount(11.11);//未付金额
		dto.setStatementId("yeyeyeyeyeyeyeye");
		dto.setAmount(22.22);//总金额
		dto.setCreateBy("1000000639");//
		dto.setCreateDate(new Date());//
		dto.setUpdateBy("1000000639");//
		dto.setUpdateDate(new Date());//
		dto.setActiveFlag(0);//有效标记 0-有效 1-无效
		ExecuteResult<String> str=monthlyStatementExportService.addMonthlyStatement(dto);
		System.out.println(str.getResultMessage()+str.getResult());


	}
	@Test
	public void modifyTranslationOrder(){
		MonthlyStatementDTO dto=new MonthlyStatementDTO();
		dto.setId(1L);
		dto.setActiveFlag(1);
		ExecuteResult<String> str=monthlyStatementExportService.modifyMonthlyStatement(dto);
		System.out.println(str.getResult());
	}
	@Test
	public void queryPageGroupByUid(){
		Pager pager=new Pager();
		pager.setRows(9);
		pager.setPage(1);
		MonthlyStatementDTO queryParam=new MonthlyStatementDTO();
		queryParam.setCreateBy("1000000654");

		Calendar calendar=Calendar.getInstance();
		calendar.set(2015,7,29,0,0,0);

			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

		try {
			queryParam.setCreateDate(simpleDateFormat.parse("2015-08-29"));
		} catch (ParseException e) {
			e.printStackTrace();
		}


		ExecuteResult<DataGrid<JSONObject>> er= monthlyStatementExportService.queryPageGroupByUid(queryParam,pager);
		System.out.println(er.getResultMessage());
		if("success".equals(er.getResultMessage())){
			DataGrid<JSONObject> dataGrid=er.getResult();
			System.out.println("总数据条数:"+dataGrid.getTotal());
			System.out.println("本页数据条数:"+dataGrid.getRows().size());
			System.out.println("以下是数据内容");
			for(JSONObject jo : dataGrid.getRows()){
				System.out.println(jo.toJSONString());
			}
		}
	}
	@Test
public void tttt(){
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	String dateStr=simpleDateFormat.format(new Date());
	System.out.println(dateStr);
		String uuid=UUID.randomUUID().toString();
	System.out.println(dateStr + uuid.substring(0,5));
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
}
public static void main(){
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssms");
	String dateStr=simpleDateFormat.format(new Date());
	System.out.println(dateStr);
	String uuid=UUID.randomUUID().toString();
	System.out.println(uuid);
	System.out.println(dateStr + uuid.substring(uuid.length() - 5));
	}
@Test
	public void rrr(){
		TradeOrdersQueryInDTO inDTO=new TradeOrdersQueryInDTO();
		Pager<TradeOrdersQueryInDTO> pager =new Pager<TradeOrdersQueryInDTO>();
	ExecuteResult<DataGrid<TradeOrdersDTO>> result=monthlyStatementExportService.queryOrders(inDTO, pager);
	System.out.println(JSON.toJSONString(result));
	}
}
