package com.camelot.payment.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.accountinfo.dao.FinanceAccountDAO;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;

@Service("dualAccountInfoService")
public class DualAccountInfoServiceImpl implements DualAccountInfoService {
	private static final Logger logger = LoggerFactory.getLogger(DualAccountInfoServiceImpl.class);
	@Resource
	private FinanceAccountDAO financeAccountDAO;

	@Override
	public ExecuteResult<FinanceAccountInfoDto> createAccountInfoDto(FinanceAccountInfoDto financeAccountInfoDto) {
		ExecuteResult<FinanceAccountInfoDto> executeResult = new ExecuteResult<FinanceAccountInfoDto>();
		logger.info("==============开始创建金融帐号设置===================");

		if (financeAccountInfoDto == null) {
			executeResult.addErrorMessage("参数为空！");
			return executeResult;
		}
		financeAccountInfoDto.setAccountId("1");
		financeAccountDAO.add(financeAccountInfoDto);
		executeResult.setResult(financeAccountInfoDto);

		return executeResult;
	}

	@Override
	public ExecuteResult<FinanceAccountInfoDto> getFinanceAccountInfoDtoById(String accountID) {
		logger.info("==============开始更新金融帐号设置===================");
		ExecuteResult<FinanceAccountInfoDto> executeResult = new ExecuteResult<FinanceAccountInfoDto>();
		if(accountID == null){
			executeResult.addErrorMessage("参数为空！");
			return executeResult;
		}
		FinanceAccountInfoDto financeAccountInfoDTO = financeAccountDAO.queryById(accountID);
		executeResult.setResult(financeAccountInfoDTO);
		return executeResult;
		

	}

	@Override
	public ExecuteResult<FinanceAccountInfoDto> updateAccountInfoDTO(FinanceAccountInfoDto financeAccountInfoDto) {
		logger.info("==============开始更新金融帐号设置===================");
		ExecuteResult<FinanceAccountInfoDto> executeResult = new ExecuteResult<FinanceAccountInfoDto>();
		if(financeAccountInfoDto == null){
			executeResult.addErrorMessage("参数为空！");
			return executeResult;
		}
		financeAccountDAO.updateFinanceAccountInfoDto(financeAccountInfoDto);
		executeResult.setResult(financeAccountInfoDto);
		return executeResult;

	}

	@Override
	public ExecuteResult<String> getSettlementAccountByPayBank(String payBank) {
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
    	if(financeAccountInfoDto == null){
    		logger.error("查询金融账号失败，无法查询结算账户");
    		result.addErrorMessage("查询金融账号失败，无法查询结算账户");
    		return result;
    	}
    	//网银在线、网银在线移动端
    	if(PayBankEnum.CB.name().equals(payBank) || PayBankEnum.CB_MOBILE.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getSettlementAccountNumber());
    	}
    	//支付宝、支付宝移动端
    	else if(PayBankEnum.AP.name().equals(payBank) || PayBankEnum.AP_MOBILE.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getSettlementAlipayAccountNumber());
    	}
    	//微信、微信移动端
    	else if(PayBankEnum.WX.name().equals(payBank) || PayBankEnum.WXPC.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getSettlementWXAccountNumber());
    	}
    	return result;
    }

	@Override
	public ExecuteResult<String> getRetundAccountByPayBank(String payBank) {
		ExecuteResult<String> result = new ExecuteResult<String>();
    	FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
    	if(financeAccountInfoDto == null){
    		logger.error("查询金融账号失败，无法查询退款账户");
    		result.addErrorMessage("查询金融账号失败，无法查询退款账户");
    		return result;
    	}
    	//网银在线、网银在线移动端
    	if(PayBankEnum.CB.name().equals(payBank) || PayBankEnum.CB_MOBILE.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getRefundAccountNumber());
    	}
    	//支付宝、支付宝移动端
    	else if(PayBankEnum.AP.name().equals(payBank) || PayBankEnum.AP_MOBILE.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getRefundAlipayAccountNumber());
    	}
    	//微信、微信移动端
    	else if(PayBankEnum.WX.name().equals(payBank) || PayBankEnum.WXPC.name().equals(payBank)){
    		result.setResult(financeAccountInfoDto.getRefundWXAccountNumber());
    	}
    	return result;
	}

}
