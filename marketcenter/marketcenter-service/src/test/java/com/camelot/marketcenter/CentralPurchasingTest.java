package com.camelot.marketcenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.CentralPurchasingDTO;
import com.camelot.maketcenter.dto.CentralPurchasingDetails;
import com.camelot.maketcenter.dto.CentralPurchasingRefEnterpriseDTO;
import com.camelot.maketcenter.dto.EnterpriseSignUpInfoDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.maketcenter.dto.SignUpRefPurchasingDetail;
import com.camelot.maketcenter.service.impl.CentralPurchasingServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class CentralPurchasingTest {
	private final static Logger logger = LoggerFactory.getLogger(CentralPurchasingTest.class);
    private CentralPurchasingServiceImpl centralPurchasingServiceImpl;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		centralPurchasingServiceImpl = (CentralPurchasingServiceImpl) ctx.getBean("centralPurchasingServiceImpl");
	}
	@Test
	public void addCentralPurchasingTest(){
		CentralPurchasingDTO centralPurchasingDTO = new CentralPurchasingDTO();
		centralPurchasingDTO.setActivityBeginTime(new Date());
		centralPurchasingDTO.setActivityEndTime(new Date());
		centralPurchasingDTO.setActivityImg("12");
		centralPurchasingDTO.setCentralPurchasingId(10L);
		centralPurchasingDTO.setActivityName("123");
		centralPurchasingDTO.setActivityNum("1234");
		centralPurchasingDTO.setActivitySignUpEndTime(new Date());
		centralPurchasingDTO.setActivitySignUpTime(new Date());
		centralPurchasingDTO.setActivityStatus(0);
		centralPurchasingDTO.setActivityType(0);
		CentralPurchasingDetails detail = new CentralPurchasingDetails();
		detail.setCentralPurchasingPrice(new BigDecimal(0));
		detail.setPaidNum(1);
		List<CentralPurchasingDetails> l = new ArrayList<CentralPurchasingDetails>();
		l.add(detail);
		centralPurchasingDTO.setDetails(l);
		ExecuteResult<Boolean> result =centralPurchasingServiceImpl.addCentralPurchasingActivityDTO(centralPurchasingDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void updateCentralPurchasingTest(){
		CentralPurchasingDTO centralPurchasingDTO = new CentralPurchasingDTO();
		centralPurchasingDTO.setActivityBeginTime(new Date());
		centralPurchasingDTO.setActivityEndTime(new Date());
		centralPurchasingDTO.setActivityImg("12");
		centralPurchasingDTO.setActivityName("123");
		centralPurchasingDTO.setActivityNum("1234");
		centralPurchasingDTO.setActivitySignUpEndTime(new Date());
		centralPurchasingDTO.setActivitySignUpTime(new Date());
		centralPurchasingDTO.setActivityStatus(0);
		centralPurchasingDTO.setActivityType(0);
		CentralPurchasingDetails detail = new CentralPurchasingDetails();
		detail.setCentralPurchasingPrice(new BigDecimal(0));
		detail.setPaidNum(1);
		detail.setActivitesDetailsId(1L);
		List<CentralPurchasingDetails> l = new ArrayList<CentralPurchasingDetails>();
		l.add(detail);
		centralPurchasingDTO.setDetails(l);
		ExecuteResult<Boolean> result =centralPurchasingServiceImpl.updateCentralPurchasingActivityDTO(centralPurchasingDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void queryCentralPurchasingTest(){
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		Pager page = new Pager();
		page.setRows(28);
		queryCentralPurchasingDTO.setActivitesDetailsId(1L);
		
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = centralPurchasingServiceImpl.queryCentralPurchasingActivityDTO(queryCentralPurchasingDTO, page);
		logger.info("\n 方法[{}]，结果：[{}]","queryCentralPurchasingTest",JSON.toJSONString(er));
		Assert.assertEquals(true, er.isSuccess());
	}
	
	@Test
	public void dubboStartTest(){
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testQueryCentralPurchasingActivity(){
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		Pager page = new Pager();
		page.setRows(28);
		queryCentralPurchasingDTO.setActivitesDetailsId(1L);
		queryCentralPurchasingDTO.setActivityName("01");
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = centralPurchasingServiceImpl.queryCentralPurchasingActivity(queryCentralPurchasingDTO, page);
		logger.info("\n 方法[{}]，结果：[{}]","queryCentralPurchasingTest",JSON.toJSONString(er));
		Assert.assertEquals(true, er.isSuccess());
	}
	@Test
	public void testAddSignUpInfo(){
		// 基本信息
		EnterpriseSignUpInfoDTO enterpriseSignUpInfoDTO = new EnterpriseSignUpInfoDTO();
		enterpriseSignUpInfoDTO.setEnterpriseName("柯莱特");
		enterpriseSignUpInfoDTO.setEnterpriseLinkman("宋文斌");
		enterpriseSignUpInfoDTO.setPhoneNo("13501133340");
		enterpriseSignUpInfoDTO.setEmail("593024214@qq.com");
		enterpriseSignUpInfoDTO.setAddress("朝阳区");
		enterpriseSignUpInfoDTO.setQqWx("hebei_song");
		enterpriseSignUpInfoDTO.setBusinessType("1");
		// 采购详情
		List<SignUpRefPurchasingDetail> details = new ArrayList<SignUpRefPurchasingDetail>();
		SignUpRefPurchasingDetail detail = new SignUpRefPurchasingDetail();
		detail.setEnterpriseEstimateNum(100);
		detail.setEnterpriseEstimatePrice(BigDecimal.valueOf(0.01));
		detail.setActivitesDetailsId(1L);
		details.add(detail);
		enterpriseSignUpInfoDTO.setSignUpRefPurchasingDetail(details);
		// 添加
		ExecuteResult<Boolean> result = centralPurchasingServiceImpl.addSignUpInfo(enterpriseSignUpInfoDTO);
		logger.info("\n 方法[{}]，结果：[{}]","queryCentralPurchasingTest",JSON.toJSONString(result));
		Assert.assertEquals(true, result.isSuccess());
	}
	
	/**
	 * 
	 * <p>Description: [集采详情查询]</p>
	 * Created on 2015年12月1日
	 * @author:[宋文斌]
	 */
	@Test
	public void testQueryCentralPurchasingRefEnterprise(){
		CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO = new CentralPurchasingRefEnterpriseDTO();
		centralPurchasingRefEnterpriseDTO.setActivitesDetailsId(1L);
		centralPurchasingRefEnterpriseDTO.setInsertBy(1L);
		ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> executeResult = centralPurchasingServiceImpl.queryCentralPurchasingRefEnterprise(centralPurchasingRefEnterpriseDTO);
		logger.info("\n 方法[{}]，结果：[{}]","testQueryCentralPurchasingRefEnterprise",JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testQuerySignUpInfo(){
		QuerySignUpInfoDTO querySignUpInfoDTO = new QuerySignUpInfoDTO();
		querySignUpInfoDTO.setActivitesDetailsId(1L);
		Pager page = new Pager();
		page.setRows(28);
		ExecuteResult<DataGrid<QuerySignUpInfoDTO>> executeResult = centralPurchasingServiceImpl.querySignUpInfo(querySignUpInfoDTO, page);
		logger.info("\n 方法[{}]，结果：[{}]","testQuerySignUpInfo",JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testPlusSignUpNum(){
		ExecuteResult<Boolean> executeResult = centralPurchasingServiceImpl.plusSignUpNum(1L, 1);
		logger.info("\n 方法[{}]，结果：[{}]","testUpdateSignUpNum",JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testPlusPlaceOrderNum(){
		ExecuteResult<Boolean> executeResult = centralPurchasingServiceImpl.plusPlaceOrderNum(1L, 1);
		logger.info("\n 方法[{}]，结果：[{}]","testUpdatePlaceOrderNum",JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testPlusPaidNum(){
		ExecuteResult<Boolean> executeResult = centralPurchasingServiceImpl.plusPaidNum(1L, 1);
		logger.info("\n 方法[{}]，结果：[{}]","testUpdatePaidNum",JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testCheckSkuId(){
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		queryCentralPurchasingDTO.setSkuId(1000000002L);
		queryCentralPurchasingDTO.setActivityEndTime(DateUtils.addMonths(new Date(), 2));
		queryCentralPurchasingDTO.setActivitySignUpTime(DateUtils.addMonths(new Date(), 1));
		ExecuteResult<Boolean> executeResult = centralPurchasingServiceImpl.checkUniqueSku(queryCentralPurchasingDTO);
		logger.info(JSON.toJSONString(executeResult));
		Assert.assertEquals(true, executeResult.isSuccess());
	}
	@Test
	public void testDeleteBatch(){
		String ids = "89,87,86";
		ExecuteResult<Boolean> er = centralPurchasingServiceImpl.deleteBatch(ids);
		Assert.assertEquals(true, er.isSuccess());
	}
}
