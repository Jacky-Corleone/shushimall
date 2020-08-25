package com.camelot.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.FinanceWithdrawRecordExportService;
import com.camelot.payment.dao.FinanceWithdrawRecordDAO;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;

@Service("financeWithdrawRecordService")
public class FinanceWithdrawRecordServiceImpl implements FinanceWithdrawRecordExportService {
	private final static Logger logger = LoggerFactory.getLogger(FinanceWithdrawRecordServiceImpl.class);
	
	@Resource
	private FinanceWithdrawRecordDAO financeWithdrawRecordDAO;
	
	@Override
	public ExecuteResult<String> addRecord(FinanceWithdrawRecordDTO entity) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			financeWithdrawRecordDAO.add(entity);
			result.setResultMessage("success");
		}catch(Exception e){
			result.addErrorMessage("提现记录添加异常："+e.getMessage());
			logger.info("\n 方法[{}]，出参：[{}]","FinanceWithdrawRecordServiceImpl-addRecord",JSON.toJSON(result));
		}
		return result;
	}
	
	@Override
	public DataGrid<FinanceWithdrawRecordDTO> queryFinanceWithdrawByCondition(FinanceWithdrawRecordDTO entity, @SuppressWarnings("rawtypes") Pager page) {
		
		DataGrid<FinanceWithdrawRecordDTO> datagrid = new DataGrid<FinanceWithdrawRecordDTO>();
		long totalCount = financeWithdrawRecordDAO.queryCount(entity);
		if(totalCount>0){
			List<FinanceWithdrawRecordDTO> listRecord = financeWithdrawRecordDAO.queryList(entity, page);
			datagrid.setRows(listRecord);
			datagrid.setTotal(totalCount);
		}
		return datagrid;
	}
	
	/**
	 * 
	 * <p>Discription:[通过交易号修改状态信息]</p>
	 * Created on 2015年12月29日
	 * @param financeWithdrawRecordDTO
	 * @return
	 * @author:[化亚会]
	 */
	@Override
	public boolean updateForTradeNo(FinanceWithdrawRecordDTO financeWithdrawRecordDTO){
		boolean bol = false;
		try {
			financeWithdrawRecordDAO.update(financeWithdrawRecordDTO);
			bol = true;
		} catch (Exception e) {
			bol = false;
			e.printStackTrace();
		}
		return bol;
	}

}
