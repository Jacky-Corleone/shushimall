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
import com.camelot.payment.FinanceWithdrawApplyExportService;
import com.camelot.payment.dao.FinanceWithdrawApplyDAO;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;

@Service("financeWithdrawApplyService")
public class FinanceWithdrawApplyServiceImpl implements FinanceWithdrawApplyExportService {
	private final static Logger logger = LoggerFactory.getLogger(FinanceWithdrawApplyServiceImpl.class);

	@Resource
	private FinanceWithdrawApplyDAO financeWithdrawApplyDAO;
	
	@Override
	public ExecuteResult<String> addRecord(FinanceWithdrawApplyDTO entity) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			financeWithdrawApplyDAO.add(entity);
			result.setResultMessage("success");
		}catch(Exception e){
			result.addErrorMessage("申请提现异常："+e.getMessage());
			logger.info("\n 方法[{}]，出参：[{}]","FinanceWithdrawApplyServiceImpl-addRecord",JSON.toJSON(result));
		}
		return result;
	}
	
	@Override
	public DataGrid<FinanceWithdrawApplyDTO> queryFinanceWithdrawByCondition(FinanceWithdrawApplyDTO entity,  @SuppressWarnings("rawtypes") Pager page) {
		DataGrid<FinanceWithdrawApplyDTO> datagrid = new DataGrid<FinanceWithdrawApplyDTO>();
		//查询记录总数
		long totalCount = financeWithdrawApplyDAO.queryCount(entity);
		if(totalCount>0){
			List<FinanceWithdrawApplyDTO> listRecord = financeWithdrawApplyDAO.queryList(entity, page);
			datagrid.setRows(listRecord);
			datagrid.setTotal(totalCount);
		}
		return datagrid;
	}

	@Override
	public ExecuteResult<String> updateRecord(FinanceWithdrawApplyDTO entity) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(entity==null || entity.getId()==null){
			result.getErrorMessages().add("对象/主键不能为空");
			return result;
		}
		try{
			financeWithdrawApplyDAO.update(entity);
			result.setResultMessage("success");
		}catch(Exception e){
			result.addErrorMessage("处理申请提现数据异常："+e.getMessage());
			logger.info("\n 方法[{}]，出参：[{}]","FinanceWithdrawApplyServiceImpl-updateRecord",JSON.toJSON(result));
		}
		return result;
	}
}
