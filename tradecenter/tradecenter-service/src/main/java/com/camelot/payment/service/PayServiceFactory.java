package com.camelot.payment.service;

import com.camelot.common.enums.PayBankEnum;
import com.camelot.openplatform.util.SpringApplicationContextHolder;

public class PayServiceFactory {

	/**
	 * 获取指定银行的service对象
	 * 
	 * @param payBank
	 * @return
	 */
	public static PayService getPayServiceInstance(String payBank) {
		PayService payService = null;
		
		if (PayBankEnum.CB.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("chinaBankGateService");
		}else if (PayBankEnum.CUP.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("unionpayService");
		}else if (PayBankEnum.CITIC.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("citicGateService");
		}else if (PayBankEnum.CB_MOBILE.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("cbMobileGateService");
		}else if (PayBankEnum.AP.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("alipayGateService");
		}else if (PayBankEnum.AP_MOBILE.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("apMobileGateService");
		}else if (PayBankEnum.WX.name().equals(payBank) || PayBankEnum.WXPC.name().equals(payBank)) {
			payService = SpringApplicationContextHolder.getBean("wxGateService");
		}
		return payService;
	}
}
