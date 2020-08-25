package com.camelot.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.PayTypeEnum;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;

/**
 * 
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class PaymentServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceTest.class);
	private CiticExportService citicExportService;
	private PaymentExportService paymentExportService;
	private ApplicationContext ctx;
	
	PayReqParam param =null;
	Map<String, String> payResult=null;
	PayBankEnum payBank=PayBankEnum.CB_MOBILE;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		paymentExportService = (PaymentExportService) ctx.getBean("paymentExportService");
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
			ExecuteResult<Integer> executeResult = paymentExportService.payIndex(param);
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
	public void testPay() throws Exception {
		param =new PayReqParam();
		param.setOrderNo("2016022501965");
		param.setPayBank(PayBankEnum.CUP);
		param.setPayOrderType(PayOrderTypeEnum.Parent);
		param.setPayType(PayTypeEnum.PAY_ONLINE);
		param.setQrPayMode("2");
		ExecuteResult<Integer> executeResult = paymentExportService.pay(param);
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
	}
	@Test
	public void testRefundApply() throws Exception {
		RefundReqParam refundReqParam = new RefundReqParam();
		refundReqParam.setOrderNo("201602260198501");
//		refundReqParam.setOutTradeNo("10120151229000811");
//		refundReqParam.setOutRefundNo("50120151229000211");
		refundReqParam.setCodeNo("2000020160226000773");
		//refundReqParam.setTransactionNo("1003830315201512312415409615");
		refundReqParam.setRefundAmount(new BigDecimal("0.01"));
		refundReqParam.setTotalAmount(new BigDecimal("0.01"));
		refundReqParam.setPayBank(PayBankEnum.CUP.name());
		refundReqParam.setDesc("0.01");
		refundReqParam.setRefundReason("000000");
		refundReqParam.setId("1000000008");
		
		ExecuteResult<Integer> executeResult = paymentExportService.refundApply(refundReqParam);
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
		Assert.assertEquals(executeResult.isSuccess(), true);

	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testPayResult() throws Exception {
//		String st2r = "buyer_id=2088802215025933, trade_no=2015080700001000930002299988, use_coupon=N, notify_time=2015-08-07 17:30:17, subject=父订单, sign_type=MD5, is_total_fee_adjust=N, notify_type=trade_status_sync, out_trade_no=30120150807003473, gmt_payment=2015-08-07 17:26:13, trade_status=TRADE_SUCCESS, discount=0.00, sign=cbadee490ec51d8cd3482127e5abb5a8, buyer_email=18501252392, gmt_create=2015-08-07 17:26:11, price=0.01, total_fee=0.01, quantity=1, seller_id=2088711167686421, notify_id=67b4a3662208be9d60134818833b3aad76, seller_email=jhcwb@gog.cn, payment_type=1, isNotify=true";
		//            "buyer_id=2088802215025933, trade_no=2015080700001000930002295522, use_coupon=N, notify_time=2015-08-07 17:57:10, subject=父订单, sign_type=MD5, is_total_fee_adjust=N, notify_type=trade_status_sync, out_trade_no=30120150807003462, gmt_payment=2015-08-07 16:33:10, trade_status=TRADE_SUCCESS, discount=0.00, sign=47c5f8a65a332fc6db1dec776240128a, buyer_email=18501252392, gmt_create=2015-08-07 16:33:07, price=0.01, total_fee=0.01, quantity=1, seller_id=2088711167686421, notify_id=6019c2f8e65561fefb3b27983806c8c376, seller_email=jhcwb@gog.cn, payment_type=1, isNotify=true";
		//String str = "sign=ZjIyYjg3ODYxMGRiODA1MzRhZWI5ZjliOGE0MzcxMjc=, system=CiticPay, v_mark_code=0, v_oid=40120151117017353, v_status=2, v_totel_fee=0.02, isNotify=false, v_status_text=父单支付：支付记录【子订单号：201511170380201付款成功;】---支付成功率：1/1, v_buy_acc=1131715072221005567";
		String str="respCode=00, txnSubType=01, txnAmt=200, version=5.0.0, signMethod=01, settleAmt=200, encoding=UTF-8, traceTime=0114100737, respMsg=success, queryId=201601141007376689298, orderId=50120160113000001, signature=EywUk2dwQpe7KLA0swRfeGPHRIKmDXQKnl6v5RYa1Zh2mFUiDIfpPJHxYkEBEEeCnaS7IEtt+cMsRYL0wj8sBU9BSP6kv+Xz7o9R/d2gJJoxaSFwUFiurWM/QS/bVfte/jdePwB3jOx8QHEBSi40I+XkdDoFT0508QEhqLsGNCY1b5sfCP/25LSm9vODFC9IbxD/C1btpRZ+sVae+ByCWgVMo3azX1T9Tkg9yxfZbbpEa9Rj1/ZpqpD31ABcXwvEex8xSBz58RNrm52pN+xY4v2hB06sqAPAxRaIbyFEAehuIWZQEoSxrfOw+k0SuXUNiWUTd7Oc2+s5UoL4tFSnAw==, txnType=01, currencyCode=156, merId=777290058110048, settleDate=0113, accNo=6221********0000, certId=68759585097, settleCurrencyCode=156, bizType=000201, reqReserved=bx, traceNo=668929,  txnTime=20160114100737, accessType=0";

		String[] strArr = str.split(",");
		Map<String,String> map = new HashMap<String,String>();
		for(int i = 0 ; i <  strArr.length ;i++){
			String [] arr = strArr[i].split("=");
			map.put(arr[0].trim(), arr[1].trim());
		}
		map.put("isNotify", "false");
		ExecuteResult<OrderInfoPay> executeResult = paymentExportService.payResult(map,"CUP");
		LOGGER.info("\n 操作结果{},信息{}", executeResult.isSuccess(),	JSONObject.toJSONString(executeResult));
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testreCreatePayInfo() throws Exception {
		ExecuteResult<String> executeResult = paymentExportService.modifyOrderPrice("201511030351201");
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
		 paymentExportService.saveFactorageJournal(factorageJournalDTO);
	}
	
	@Test
	public void testfindFactorageJournal() throws Exception {
		Pager pager=new Pager();
		FactorageJournalDTO factorageJournalDTO =new FactorageJournalDTO();
//		factorageJournalDTO.setOrderNo("102");
//		factorageJournalDTO.setCreatedBegin("2015-06-25");
//		factorageJournalDTO.setCreatedEnd("2015-06-25");
		 paymentExportService.findFactorageJournal(factorageJournalDTO,pager);
	}
	
	@Test
	public void testfindChildTransByOrderNo(){
		ExecuteResult<OrderInfoPay> orderInfoPay = paymentExportService.findTransByOrderNo("201509230241101");
		LOGGER.info("\n 操作结果{},信息{}",orderInfoPay.isSuccess(),JSONObject.toJSONString(orderInfoPay.getResult()));
	}
}
