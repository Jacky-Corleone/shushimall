package com.camelot.payment.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;

public interface FinanceWithdrawApplyDAO extends BaseDAO<FinanceWithdrawApplyDTO>{
	
	/**
	 * 获取提现单号
	 * 
	 * @return
	 */
	String getWithdrawNo();
}