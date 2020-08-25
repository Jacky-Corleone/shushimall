package com.camelot.payment;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;

/**
 * 供客户端调用的提现远程接口
 * 
 * @Description -
 */
public interface FinanceWithdrawApplyExportService {
	
	/**
	 * 申请提现 
	 * 
	 * @param entity
	 * @return
	 */
	public ExecuteResult<String> addRecord(FinanceWithdrawApplyDTO entity);
	
	/**
	 * 根据条件查询提现申请
	 * 
	 * @param entity 查询条件，可空
	 * @param page 当前页码，可空
	 * @return
	 */
	public DataGrid<FinanceWithdrawApplyDTO> queryFinanceWithdrawByCondition(FinanceWithdrawApplyDTO entity, @SuppressWarnings("rawtypes") Pager page);

	/**
	 * 提现申请通过/不通过
	 * 
	 * @param entity  id -主键  content -理由  status - 2不通过 10：提现处理中
	 * @return
	 */
	public ExecuteResult<String> updateRecord(FinanceWithdrawApplyDTO entity);
}
