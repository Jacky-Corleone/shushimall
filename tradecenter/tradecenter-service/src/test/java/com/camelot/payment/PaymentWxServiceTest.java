package com.camelot.payment;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.util.Signature;
import com.camelot.common.util.TestDataUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.service.alipay.util.mobile.MD5;
import com.camelot.payment.service.alipay.util.pc.AlipayConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class PaymentWxServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWxServiceTest.class);
	private CiticExportService citicExportService;
	private PaymentWxExportService paymentWxExportService;
	private ApplicationContext ctx;
	
	PayReqParam param =null;
	Map<String, String> payResult=null;
	PayBankEnum payBank=PayBankEnum.AP_MOBILE;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		paymentWxExportService = (PaymentWxExportService) ctx.getBean("paymentWxExportService");
		citicExportService = (CiticExportService) ctx.getBean("citicExportService");
	}

	/**
	 * @throws Exception
	 *
	 */
	@Test
	public void testPayIndex() {
		param =new PayReqParam();
		param.setSystem("MALL");
		param.setOrderNo("2015062401194");
		param.setSubject("parent");
		param.setTotalFee(new BigDecimal("0.02"));
		param.setOrderDetails("{detail:0328测试订单}");

		List<OrderItemPay> list= new ArrayList<OrderItemPay>();
		for (int i = 5; i < 6; i++) {
			OrderItemPay orderItemPay=new OrderItemPay();
			orderItemPay.setSubOrderId("201506240119401");
			orderItemPay.setSubOrderSubject("2000000165");
			orderItemPay.setSellerId(1000000639l);
			orderItemPay.setSubOrderPrice(new BigDecimal("0.02"));
			list.add(orderItemPay);
		}
		param.setOrderItemPays(list);

		String keyPri="123456";
		try {
			Map<String, String> params =new HashMap<String, String>();
			params.put("system",param.getSystem());
			params.put("orderNo", param.getOrderNo());
			params.put("totalFee", param.getTotalFee().toString());
			param.setSign(Signature.createSign(params, keyPri));
			ExecuteResult<Integer> executeResult = paymentWxExportService.payIndex(param);
			LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 *
	 */
	@Test
	public void testPayResult() throws Exception {
		payResult=TestDataUtils.getPayResultData(payBank.name());
		payResult=new HashMap<String, String>();
		payResult.put("service","alipay.wap.trade.create.direct");
		payResult.put("sign","4ac44230af4fd93e00c4cb50922b739a");
		payResult.put("sec_id","MD5");
		payResult.put("v","1.0");
		payResult.put("notify_data","<notify><payment_type>1</payment_type><subject>null</subject><trade_no>2015090100001000930004419103</trade_no><buyer_email>chu_dejun@163.com</buyer_email><gmt_create>2015-09-01 15:03:53</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>30120150901003168</out_trade_no><notify_time>2015-09-01 15:03:54</notify_time><seller_id>2088911503294873</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.04</total_fee><gmt_payment>2015-09-01 15:03:54</gmt_payment><seller_email>printhome@printhome.com</seller_email><price>0.04</price><buyer_id>2088102018660932</buyer_id><notify_id>e850f4ebf466fd1fd133313aedaf5d0676</notify_id><use_coupon>N</use_coupon></notify>");
		payResult.put("isNotify","true");

		ExecuteResult<OrderInfoPay> executeResult = paymentWxExportService.payResult(payResult,payBank.name());
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
	}

	/**
	 * @throws Exception
	 *
	 */
	@Test
	public void testPay() throws Exception {
		param =new PayReqParam();
		param.setOrderNo("201507090123701");
		param.setPayBank(payBank);
		ExecuteResult<Integer> executeResult = paymentWxExportService.pay(param);
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
	}
	/**
	 * @throws Exception
	 *
	 */
	@Test
	public void testPayResultMB() throws Exception {
		Map<String, String> params=new HashMap<String, String>();
		params.put("success","true");
		params.put("isNotify","false");
		params.put("tradeNum","30120150731002410");
		ExecuteResult<OrderInfoPay> executeResult =paymentWxExportService.payResult(params,"CB_MOBILE");
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(), JSONObject.toJSONString(executeResult));
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testreCreatePayInfo() throws Exception {
		ExecuteResult<String> executeResult = paymentWxExportService.modifyOrderPrice("20150327031");
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testSaveFactorageJournal() throws Exception {
		FactorageJournalDTO factorageJournalDTO =new FactorageJournalDTO();
		factorageJournalDTO.setOrderNo("104");
		factorageJournalDTO.setFactorage(new BigDecimal("0.01"));
		factorageJournalDTO.setStatus(FactorageStatus.Initial);
		factorageJournalDTO.setRemark1("ccc");
		factorageJournalDTO.setRemark2("ccc");
		 paymentWxExportService.saveFactorageJournal(factorageJournalDTO);
	}
	
	@Test
	public void testfindFactorageJournal() throws Exception {
		Pager pager=new Pager();
		FactorageJournalDTO factorageJournalDTO =new FactorageJournalDTO();
//		factorageJournalDTO.setOrderNo("102");
//		factorageJournalDTO.setCreatedBegin("2015-06-25");
//		factorageJournalDTO.setCreatedEnd("2015-06-25");
		 paymentWxExportService.findFactorageJournal(factorageJournalDTO,pager);
	}
@Test
	public void aaaa(){
		boolean isSign = MD5.verify("notify_data=<notify><payment_type>1</payment_type><subject>null</subject><trade_no>2015083100001000930004333942</trade_no><buyer_email>chu_dejun@163.com</buyer_email><gmt_create>2015-08-31 20:31:25</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>30120150831003152</out_trade_no><notify_time>2015-08-31 20:31:26</notify_time><seller_id>2088911503294873</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.02</total_fee><gmt_payment>2015-08-31 20:31:26</gmt_payment><seller_email>printhome@printhome.com</seller_email><price>0.02</price><buyer_id>2088102018660932</buyer_id><notify_id>1ef981c160599d01c833d0dd518c226276</notify_id><use_coupon>N</use_coupon></notify>&sec_id=MD5&service=alipay.wap.trade.create.direct&v=1.0",
				"6d552cde86588c1d1b9e2a560b73f383",
				AlipayConfig.KEY_MOBILE,
				AlipayConfig.INPUT_CHARSET);
	System.out.println("测试得到的结果++++++++++++++++++++++++++"+isSign);
	}
	
}
