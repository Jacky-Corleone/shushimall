package com.camelot.payment.service;

import java.util.HashMap;
import java.util.Map;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;


public interface PayService {

	/**
	 * 获取网关支付form
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ExecuteResult<Integer> buildPayForm(PayReqParam param);
	/**
	 * 获取网关退款form
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam);
	
	/**
	 * [接收支付结果通知]根据不同的type解析银行返回的参数，生成交易流程控制对象,包含验证解析等处理
	 * 解析银行返回结果参数，判断签名是否正确，获取订单支付状态，封装返回对象，用于接收支付结果通知的处理
	 * @param map - 银行返回参数
	 * @param type - 取值范围：PayJournayStatusEnum.code值  目前包含同步[2]/异步[3]/查询[4]
	 * @return 必须参数：outTradeNo,status,realAmount，CompletedTime<br>
	 *                  选传参数：TransactionNo、Buyer、FromAccount
	 */
	public ExecuteResult<Transations> buildTransatoins(Map<String, String> map,String type);
	
	/**
	 * 调用银行接口查询订单 
	 * 
	 * @param transations
	 * @return
	 * @throws Exception 
	 */
	public Transations queryTradeInfo(Transations transations);
	
}
