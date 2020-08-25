package com.camelot.tradecenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;

/**
 * 
 * @author yangsaibei
 * 
 * @version 2015年4月17日
 * 
 */
public class DualAccountInfoServiceTest {

	private DualAccountInfoService dualAccountInfoService = null;
	private ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		dualAccountInfoService = (DualAccountInfoService) ctx.getBean("dualAccountInfoService");
	}

	@Test
	public void createAccountInfoDtoTest() {
		FinanceAccountInfoDto financeAccountInfoDto = new FinanceAccountInfoDto();
		financeAccountInfoDto.setAccountId("1");
		financeAccountInfoDto.setFinancialCloudPaymentAccountId("1");
		financeAccountInfoDto.setFullName("印刷家");
		financeAccountInfoDto.setOwnedBank("中信银行");
		financeAccountInfoDto.setInitializationFundsAccountName("科印自己初始化");
		financeAccountInfoDto.setInitializationFundsAccountNumber("12343t534508");
		financeAccountInfoDto.setMasterAccountName("科印传媒");
		financeAccountInfoDto.setMasterAccountNumber("4346256");
		financeAccountInfoDto.setPublicAccountName("科印公共调账");
		financeAccountInfoDto.setPublicAccountNumber("34536265");
		financeAccountInfoDto.setPublicChargingFeesAccountName("科印公共计息收费");
		financeAccountInfoDto.setPublicChargingFeesAccountNumber("4565345345");
		financeAccountInfoDto.setSettlementAccountName("科印结算");
		financeAccountInfoDto.setSettlementAccountNumber("23432453453");
//		ExecuteResult<FinanceAccountInfoDto> createAccountInfoDto = dualAccountInfoService.createAccountInfoDto(financeAccountInfoDto);
//		Assert.assertEquals(true, createAccountInfoDto.isSuccess());
	}

	@Test
	public void updateAccountInfoDTOTest() {
		FinanceAccountInfoDto financeAccountInfoDto = new FinanceAccountInfoDto();
		financeAccountInfoDto.setAccountId("1");
		financeAccountInfoDto.setFullName("测试数据");
		financeAccountInfoDto.setFinancialCloudPaymentAccountId("12131231");
		financeAccountInfoDto.setFullName("1222222");
		financeAccountInfoDto.setOwnedBank("1222222");
		financeAccountInfoDto.setBankLoginUsername("ZYZL");
		financeAccountInfoDto.setCiticFrontEndProcessor("http://218.249.200.177:6789");
		financeAccountInfoDto.setMasterAccountName("中信主体账户");
		financeAccountInfoDto.setMasterAccountNumber("7111010182600297921");
		financeAccountInfoDto.setClusterId("1");
		financeAccountInfoDto.setSettlementAccountName("中信网银在线账户");
		financeAccountInfoDto.setSettlementAccountNumber("1298500000000010961");
		financeAccountInfoDto.setCommissionAccountName("中信佣金账户");
		financeAccountInfoDto.setCommissionAccountNumber("1298500000000010960");
		financeAccountInfoDto.setRefundAccountName("科印测试网银在线退款账户");
		financeAccountInfoDto.setRefundAccountNumber("1298520150316000031");
		financeAccountInfoDto.setInitializationFundsAccountNumber("1");
//		ExecuteResult<FinanceAccountInfoDto> result = dualAccountInfoService.updateAccountInfoDTO(financeAccountInfoDto);
//		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void getFinanceAccountInfoDtoByIdTest() {
		ExecuteResult<FinanceAccountInfoDto> result = dualAccountInfoService.getFinanceAccountInfoDtoById("1");
		Assert.assertEquals(true, result.isSuccess());
	}

}
