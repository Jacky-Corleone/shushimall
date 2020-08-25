package com.camelot.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.PayTypeEnum;
import com.camelot.common.enums.SystemKey;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticPayJournalDto;
import com.camelot.payment.dto.CiticTradeInDTO;
import com.camelot.payment.dto.CiticTradeOutDTO;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.camelot.payment.job.ConfirmCiticJob;
import com.camelot.payment.service.CiticService;

/**
 * 
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CiticServiceTest{
	private static final Logger LOGGER = LoggerFactory.getLogger(CiticServiceTest.class);
	private CiticExportService citicExportService;
	private ApplicationContext ctx;
	
	private ConfirmCiticJob confirmCiticJob;
	
	private CiticService citicService;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		citicExportService = (CiticExportService) ctx.getBean("citicExportService");
		confirmCiticJob = (ConfirmCiticJob) ctx.getBean("confirmCiticJob");
		citicService = (CiticService) ctx.getBean("citicGateService");
	}
	
	@Test
	public void testConfirmCiticJob(){
		confirmCiticJob.confirmCompanyPayJob();
	}
	
	@Test
	public void testmodifyComPayJobById(){
		List<CompanyPayJobDTO> listComPayJobDto = citicExportService.findUnDealComPayJob();
		for (CompanyPayJobDTO companyPayJobDTO:listComPayJobDto) {
			CompanyPayJobDTO comPayJobDTO = new CompanyPayJobDTO();
			comPayJobDTO.setId(companyPayJobDTO.getId());
			comPayJobDTO.setBatch(companyPayJobDTO.getBatch()+1);
			citicExportService.modifyComPayJob(comPayJobDTO);
		}
	}
	
	@Test
	public void testqueryTotelBalance(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("system", SystemKey.MainBalance.name());
		map.put("sign", Signature.createSign(map, "234561"));
		ExecuteResult<MainBalanceDto> executeResult = citicExportService.queryBalance(map);
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
	}
	
	@Test
	public void testaddAffiliated(){
		try {
			AccountInfoDto accountInfoDto=new AccountInfoDto();
			accountInfoDto.setUserId(3l);
			accountInfoDto.setSubAccNm("科印测试20150318");
			accountInfoDto.setAccType(AccountType.AccSellFreeze);
			ExecuteResult<AccountInfoDto> executeResult;
			executeResult = citicExportService.addAffiliated(accountInfoDto,null);
			LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
		} catch (Exception e) {
			LOGGER.info("操作异常{}", JSON.toJSON(e.getMessage()));
		}
	}
	@Test
	public void testqueAffiliatedStatus(){
		
		ExecuteResult<AccountInfoDto> executeResult;
		try {
			executeResult = citicExportService.queAffiliatedStatus("1298520150316000032");
			LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
		} catch (Exception e) {
			LOGGER.info("操作异常{}", e.getMessage());
		}
	}
	
	@Test
	public void testquerySubBalance(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("system", "MALL");
		map.put("subAccNo", "1154515061021001095");
		map.put("sign", "NWIyMmMyMmYwNWRkMjI0NGQxOWU1MTcxZDRlMDM5MWI=");
		//map.put("system", SystemKey.MALL.name());
		//map.put("subAccNo", "1154515062231005237");
		//map.put("sign", Signature.createSign(map, "123456"));

		ExecuteResult<AffiliatedBalanceDto> executeResult = citicExportService.querySubBalance(map);
		AffiliatedBalance affiliatedBalance=executeResult.getResult().getList().get(0);
		System.out.println("附属账号："+affiliatedBalance.getSubAccNo());
		System.out.println("附属账户名称："+affiliatedBalance.getSUBACCNM());
		System.out.println("透支额度(TZAMT)："+affiliatedBalance.getTZAMT());
		System.out.println("可用余额(KYAMT)："+affiliatedBalance.getKYAMT());
		System.out.println("实际余额(SJAMT)："+affiliatedBalance.getSJAMT());
		System.out.println("冻结金额(DJAMT)："+affiliatedBalance.getDJAMT());
		
		LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
	}
	@Test
	public void testpayCitic() throws Exception {

		Map<String, String> parameterMap=new HashMap<String,String>();
		parameterMap.put("system", "MALL");
		parameterMap.put("outTradeNo", "401201512240005891");
		parameterMap.put("uid", "1000000032");
		parameterMap.put("accType", AccountType.AccBuyPay.getCode()+"");
		parameterMap.put("sign",Signature.createSign(parameterMap, "123456") );
		Map<String, String> payResult=citicExportService.payCitic(parameterMap);
		LOGGER.info("\n 操作结果{},信息{}", "",	JSONObject.toJSONString(payResult));
	}
	@Test
	public void testQueryTransfer(){
		String  ueryTransferNo= "401201512300009171";
		try {
			QueryTransferDto queryTransferDto = citicExportService.queryTransfer(ueryTransferNo);
			LOGGER.info("操作结果：{}", JSON.toJSON(queryTransferDto));
		} catch (Exception e) {
			LOGGER.info("操作异常：{}", e.getMessage());
		} 
	}
	@Test
	public void testoutTransfer(){
		String withdrawPrice="300";
		Long userId=1000000882l;
		AccountType accType=AccountType.AccSellReceipt;
		String system="MALL";
		String priKey="123456";
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("system", system);
		map.put("uid", userId.toString());
		map.put("accType", accType.name());
		map.put("withdrawPrice",withdrawPrice );
		String sign=Signature.createSign(map, priKey);
		
		AccountInfoDto accountInfoDto =new AccountInfoDto();
		accountInfoDto.setUserId(userId);
		accountInfoDto.setAccType(accType);
		accountInfoDto.setWithdrawPrice(new BigDecimal(withdrawPrice));
		accountInfoDto.setSystem(system);
		accountInfoDto.setSign(sign);
		ExecuteResult<String> executeResult;
		try {
			executeResult = citicExportService.outPlatformTransfer(accountInfoDto);
			LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
		} catch (Exception e) {
			LOGGER.info("操作异常{}", e.getMessage());
		}
	}
	
	@Test
	public void testfindAccByStatus(){
		List<AccountInfoDto> result=citicExportService.findAccByStatus(AccStatus.AuditPass);
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
	
	@Test
	public void testsaveAffiliated(){
		List<AccountInfoDto> listAcc=citicExportService.findAccByStatus(AccStatus.UnCreate);
		for (AccountInfoDto accountInfoDto:listAcc) {
			boolean result=citicExportService.saveAffiliated(accountInfoDto);
			LOGGER.info("操作结果：{}", result);
		}
		
	}
	
	@Test
	public void testmodifyAffiliatedStatus(){
		AccountInfoDto accountInfoDto = new AccountInfoDto();
		boolean result=citicExportService.modifyAffiliatedStatus(accountInfoDto);
		LOGGER.info("操作结果：{}", result);
	}
	
	@Test
	public void testfindByUIds(){
		List<Long> listUId=new ArrayList<Long>();
		listUId.add(1l);
		Integer[] accountTypes ={21};
		List<AccountInfoDto> result=citicExportService.findByUIds(listUId,accountTypes);
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
	
	@Test
	public void testfindCiticPayJournalList(){
		CiticPayJournalDto citicPayJournalDto =new CiticPayJournalDto();
		List<CiticPayJournalDto> result=citicExportService.findCiticPayJournalList(citicPayJournalDto);
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
	
	@Test
	public void testmodifyCiticPayJournalDeal(){
		CiticPayJournalDto citicPayJournalDto =new CiticPayJournalDto();
		int result=citicExportService.modifyCiticPayJournalDeal(citicPayJournalDto);
		LOGGER.info("操作结果：{}", result);
	}
	
	@Test
	public void testfindUnDealComPayJob(){
		List<CompanyPayJobDTO> result=citicExportService.findUnDealComPayJob();
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
	
	@Test
	public void testdealById(){
		int result=citicExportService.dealComPayJobById(1l);
		LOGGER.info("操作结果：{}", result);
	}
	
	@Test
	public void testsaveComPayJob(){
		CompanyPayJobDTO companyPayJobDTO =new CompanyPayJobDTO();
		companyPayJobDTO.setOutTradeNo("30120150626002113");
		companyPayJobDTO.setOrderNo("22");
		companyPayJobDTO.setUserId(3l);
		companyPayJobDTO.setAccType(AccountType.AccBuyPay);
		try {
			ExecuteResult<String> result=citicExportService.saveComPayJob(companyPayJobDTO);
			LOGGER.info("操作结果：{}", JSON.toJSON(result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testgetAccountInfoByUserIdAndAccountType(){
		try {
			ExecuteResult<AccountInfoDto> result=citicExportService.getAccountInfoByUserIdAndAccountType(1000000856l,21);
			LOGGER.info("操作结果：{}", JSON.toJSON(result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testqueryCiticTradeList(){
		CiticTradeInDTO citicTradeInDTO = new CiticTradeInDTO();
//		citicTradeInDTO.setAction("DLPTDTQY");
//		citicTradeInDTO.setUserName("kyzl01");
//		citicTradeInDTO.setMainAccNo("7117510182600010251");
//		citicTradeInDTO.setSubAccNo("1131715080631005645");
//		citicTradeInDTO.setStartDate("20151117");
//		citicTradeInDTO.setEndDate("20151117");
		citicTradeInDTO.setStartRecord(1);
		citicTradeInDTO.setPageNumber(10);
		ExecuteResult<CiticTradeOutDTO> result = citicExportService.queryCiticTradeList(citicTradeInDTO);
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
	
	@Test
	public void testTransferAffiliated(){
		PayReqParam payReqParam = new PayReqParam();
		payReqParam.setExtraParam("未结算资金平台个人支付结算账户退款部分");
		payReqParam.setFromAccount("1131715080631005631");
		payReqParam.setOutTradeNo("20120151229000814");
		payReqParam.setPayOrderType(PayOrderTypeEnum.Parent);
		payReqParam.setPayType(PayTypeEnum.PAY_ONLINE);
		payReqParam.setSeller("北京科印传媒文化股份有限公司退款账户支付宝支付");
		payReqParam.setToAccount("1131715082131005687");
		payReqParam.setTotalFee(BigDecimal.valueOf(0.9));
		ExecuteResult<String> result = citicService.transferAffiliated(payReqParam, "0", CiticPayTypeCode.refund);
		LOGGER.info("操作结果：{}", JSON.toJSON(result));
	}
}
