package com.camelot.payment;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.domain.FactorageJournal;

public interface FactorageJournalExportService {
	/**
	 * 根据订单编号和状态查询手续费
	 * @param orderNo
	 * @param status
	 * @return
	 */
	public ExecuteResult<List<FactorageJournal>> selectByOrderNoAndStatus(String orderNo , Integer status);
	/**
	 * 根据ID和状态查询手续费
	 * @param id
	 * @param status
	 * @return
	 */
	public ExecuteResult<Integer> updateStatusById(Long id,int status);
}
