package com.camelot.payment;



import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;

public interface FinanceWithdrawRecordExportService {
	
	/**
	 * 新增提现日志记录 
	 * 
	 * @param entity
	 * @return
	 */
	public ExecuteResult<String> addRecord(FinanceWithdrawRecordDTO entity);
	
	/**
	 * 根据条件查询提现记录
	 * 
	 * @param entity 查询条件，可空
	 * @param page 当前页码，可空
	 * @return
	 */
	public DataGrid<FinanceWithdrawRecordDTO> queryFinanceWithdrawByCondition(FinanceWithdrawRecordDTO entity, @SuppressWarnings("rawtypes") Pager page);
	
	/**
	 * 
	 * <p>Discription:[通过交易号修改状态信息]</p>
	 * Created on 2015年12月29日
	 * @param financeWithdrawRecordDTO
	 * @return
	 * @author:[化亚会]
	 */
	public boolean updateForTradeNo(FinanceWithdrawRecordDTO financeWithdrawRecordDTO);
	
}
