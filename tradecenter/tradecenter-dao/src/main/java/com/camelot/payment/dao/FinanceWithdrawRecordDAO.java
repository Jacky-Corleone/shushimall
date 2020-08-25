package com.camelot.payment.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;

public interface FinanceWithdrawRecordDAO extends BaseDAO<FinanceWithdrawRecordDTO>{
	
	/**
	 * 
	 * <p>Discription:[通过交易号修改状态信息]</p>
	 * Created on 2015年12月29日
	 * @param financeWithdrawRecordDTO
	 * @return
	 * @author:[化亚会]
	 */
	public boolean updateForTradeNo(@Param("entity")FinanceWithdrawRecordDTO financeWithdrawRecordDTO);
	
}