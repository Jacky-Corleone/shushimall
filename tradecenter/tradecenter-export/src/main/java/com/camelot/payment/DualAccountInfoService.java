package com.camelot.payment;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;


/**
 * 
 * @author Admin
 *
 */
public interface DualAccountInfoService {
	 /**
     * 创建金融帐号设置
     */
    public ExecuteResult<FinanceAccountInfoDto> createAccountInfoDto(FinanceAccountInfoDto financeAccountInfoDto);
    /**
     * 更新金融帐号设置
     */
    public ExecuteResult<FinanceAccountInfoDto> updateAccountInfoDTO(FinanceAccountInfoDto financeAccountInfoDto);
    /**
     * 查询金融帐号设置
     */
    public ExecuteResult<FinanceAccountInfoDto> getFinanceAccountInfoDtoById(String getAccountID);
    /**
     * 根据支付银行查询印刷家结算账户 
     * @param payBank  支付银行  必传
     * @return 银行账号
     */
    public ExecuteResult<String> getSettlementAccountByPayBank(String payBank);
    /**
     * 根据支付银行查询印刷家退款账户 
     * @param payBank  支付银行  必传
     * @return 银行账号
     */
    public ExecuteResult<String> getRetundAccountByPayBank(String payBank);
}
