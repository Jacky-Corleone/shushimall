package com.camelot.sattlecenter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dao.StatementDetailDAO;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.dto.indto.SettlementInDTO;
import com.camelot.settlecenter.job.ConfirmSettleJob;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 *
 * @author <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class StatementExportServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementExportServiceTest.class);
    private StatementExportService statementExportService;
    private ApplicationContext ctx;
    private ConfirmSettleJob confirmSettleJob;
    private TradeOrderExportService tradeOrderExportService = null;
	private StatementDetailDAO statementDetailDAO;
    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("test.xml");
        statementExportService = (StatementExportService) ctx.getBean("statementExportService");
        confirmSettleJob = (ConfirmSettleJob) ctx.getBean("confirmSettleJob");
        tradeOrderExportService = (TradeOrderExportService) ctx.getBean("tradeOrderExportService");
        statementDetailDAO = (StatementDetailDAO)ctx.getBean("statementDetailDAO");
    }

    @Test
    public void testconfirmSettleJob() {
    	confirmSettleJob.createBill();
    }
    
    @Test
    public void testcreateSettleDetail() {
    	
//        for (int i = 4; i < 5; i++) {
        	TradeOrdersQueryInDTO tradeOrder = new TradeOrdersQueryInDTO();
        	tradeOrder.setOrderId("201512300102202");
            ExecuteResult<DataGrid<TradeOrdersDTO>> results = this.tradeOrderExportService.queryOrders(tradeOrder, null);
            TradeOrdersDTO tradeOrdersDTO =results.getResult().getRows().get(0);
            try {
                ExecuteResult<String> result = statementExportService.createSettleDetail(tradeOrdersDTO,null);
                LOGGER.info("\n 操作结果{},信息{}", result.isSuccess(), JSONObject.toJSONString(result));
            } catch (Exception e) {
                LOGGER.info("\n 操作异常：{}", e.getMessage());
            }
//        }

    }

    @Test
    public void testproceedSettle() {
        try {
            ExecuteResult<String> result = statementExportService.proceedSettle(584l);
            LOGGER.info("\n 操作结果{},信息{}", result.isSuccess(), JSONObject.toJSONString(result));
        } catch (Exception e) {
            LOGGER.info("\n 操作异常：{}", e.getMessage());
        }
    }

    @Test
    public void testfreezeSettleDetail() {
        try {
        	SettlementDetailDTO settlementDetailDTO = new SettlementDetailDTO();
        	settlementDetailDTO.setStatus(0);
        	statementDetailDAO.queryList(settlementDetailDTO, null);
        	
            ExecuteResult<String> result = statementExportService.freezeSettleDetail("20150327031");
            LOGGER.info("\n 操作结果{},信息{}", result.isSuccess(), JSONObject.toJSONString(result));
        } catch (Exception e) {
            LOGGER.info("\n 操作异常：{}", e.getMessage());
        }
    }
    

    @Test
    public void querySavesettlementDTO() {
        SettlementDTO settlementDTO = new SettlementDTO();
        settlementDTO.setShopId(7777l);
        
        try {
//				statementExportService.save(settlementDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("rawtypes")
    @Test
    public void querySettlementListTest() {
        SettlementInDTO settlementInDTO = new SettlementInDTO();
//        Long[] shopIds = new Long[10];
//
//        shopIds[0] = 2000000207l;
//        shopIds[1] = 2000000232l;
//        shopIds[2] = 2000000231l;
//        shopIds[3] = 2000000230l;
//        shopIds[4] = 2000000223l;
//        shopIds[5] = 2000000229l;
//        shopIds[6] = 2000000228l;
//        shopIds[7] = 2000000227l;
//
//        settlementInDTO.setShopIds(shopIds);
        settlementInDTO.setStatus(0);
//			settlementInDTO.setShopId(1l);
        Pager page = new Pager();
        ExecuteResult<DataGrid<SettlementCombinDTO>> result = statementExportService.querySettlementList(settlementInDTO, page);
        System.out.println("result---------" + JSON.toJSONString(result.getResult()));
        Assert.assertEquals(true, result.isSuccess());
    }

    @Test
    public void modifySettlementStatesTest() {

        ExecuteResult<String> result = statementExportService.modifySettlementStates(1581l);
        LOGGER.info("\n 操作结果{},信息{}", result.isSuccess(), JSONObject.toJSONString(result));
    }

    /**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testsaveBankSettle() {
		List<BankSettleDetailDTO> listBankSettleDetailDTO =new ArrayList<BankSettleDetailDTO>();
		for (int i = 1; i < 3; i++) {
			BankSettleDetailDTO bankSettleDetailDTO= new BankSettleDetailDTO();
			bankSettleDetailDTO.setOutTradeNo("30120150716003230");
			bankSettleDetailDTO.setOrderAmount(new BigDecimal("0.01"));
			bankSettleDetailDTO.setBankType(PayBankEnum.CB.name());
			bankSettleDetailDTO.setFactorage(new BigDecimal("0.10"));
			listBankSettleDetailDTO.add(bankSettleDetailDTO);
		}	
		
		
		ExecuteResult<String> executeResult;
		try {
			executeResult = statementExportService.saveBankSettle(listBankSettleDetailDTO);
			LOGGER.info("\n 操作结果{},信息{}", "",	JSONObject.toJSONString(executeResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 /**
		 * @throws Exception 
		 * 
		 */
		@Test
	public void testfindBankSettle() {
		BankSettleDetailDTO bankSettleDetailDTO= new BankSettleDetailDTO();
//		bankSettleDetailDTO.setOutTradeNo("30120150630002310");
//		bankSettleDetailDTO.setOrderAmount(new BigDecimal("30.00"));
//		bankSettleDetailDTO.setBankType(PayBankEnum.CB.name());
//		bankSettleDetailDTO.setFactorage(new BigDecimal("0.50"));
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sf.parse("2015-07-01 00:00:00");
			endDate = sf.parse("2015-07-03 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bankSettleDetailDTO.setComletedTimeStart(startDate);
		bankSettleDetailDTO.setComletedTimeEnd(endDate);
		DataGrid<BankSettleDetailDTO> executeResult = statementExportService.findBankSettle(bankSettleDetailDTO, null);
		LOGGER.info("\n 操作结果{},信息{}", "",	JSONObject.toJSONString(executeResult));
	}
		
		 /**
		 * @throws Exception 
		 * 
		 */
		@Test
	public void testmodifySettleDetailByOrderId() {
		BigDecimal childOrderFactorage =new BigDecimal("2.00");
		int executeResult = statementExportService
				.modifySettleDetailByOrderId("2221",SettleDetailStatusEnum.PayAffirm, childOrderFactorage);
		LOGGER.info("\n 操作结果{},信息{}", "",	JSONObject.toJSONString(executeResult));
	}
}
