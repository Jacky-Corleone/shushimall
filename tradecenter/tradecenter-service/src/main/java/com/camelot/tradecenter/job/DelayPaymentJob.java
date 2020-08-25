package com.camelot.tradecenter.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.ConfirmTimeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.tradecenter.dao.TradeOrdersDAO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.impl.DelayPaymentServiceImpl;

/**
 * 延期支付定时处理
 * 
 * @author zhouzj
 * @since 2015-8-21
 * 
 */
public class DelayPaymentJob {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private TradeOrdersDAO tradeOrdersDAO;
	@Resource
	private PaymentExportService paymentExportService;
	@Resource
	private DelayPaymentServiceImpl delayPaymentServiceImpl;
	@Resource
	private CiticExportService citicService;
	@Resource
	private TransationsDAO transationsDAO;

	public void delayPay() {
		log.debug("延期支付定时处理启动");
		ConfirmTimeEnum confirmTimeEnum = ConfirmTimeEnum.getConfirmTime();
		String confirmTime = confirmTimeEnum.getCode();
		List<TradeOrdersDTO> tradeOrders = tradeOrdersDAO
				.queryTradeOrdersForDelayPay(confirmTime);
		try {
			for (TradeOrdersDTO tradeOrder : tradeOrders) {
				PayReqParam param = new PayReqParam();
				param.setOrderNo(tradeOrder.getOrderId());
				param.setPayOrderType(PayOrderTypeEnum.child);
				param.setPayBank(PayBankEnum.CITIC);

				ExecuteResult<Integer> er = paymentExportService.pay(param);
				if (er.isSuccess()) {
					Transations trans = transationsDAO.selectTransByOrderNo(tradeOrder.getOrderId());
					// 必填信息设置
					Map<String, String> parameterMap = new HashMap<String, String>();
					parameterMap.put("system",
							SysProperties.getProperty("transfer.system"));
					parameterMap.put("outTradeNo", trans.getOutTradeNo());
					parameterMap.put("accType", AccountType.AccBuyPay.getCode()
							+ "");
					parameterMap.put("uid", tradeOrder.getBuyerId().toString());
					parameterMap.put("sign", Signature.createSign(parameterMap,
							SysProperties.getProperty("transfer.prikey")));

					Map<String, String> payResult = this.citicService
							.payCitic(parameterMap);
					log.debug("CITIC PAY PARAMS："
							+ JSON.toJSONString(parameterMap));

					ExecuteResult<OrderInfoPay> er2 = this.paymentExportService
							.payResult(payResult, PayBankEnum.CITIC.name());
					log.debug("CITIC PAY RESULT：" + JSON.toJSONString(er));
					// 扣款结果
					delayPaymentServiceImpl.sendMessage(tradeOrder,er2.isSuccess());
				}
			}
		} catch (Exception e) {
			log.error("延期付款失败：" + e.getMessage());
		} finally {
			log.debug("延期付款定时处理结束");
		}
	}

}
