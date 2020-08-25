package com.camelot.settlecenter.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dao.SettleItemExpenseDAO;
import com.camelot.settlecenter.dao.SettlementCategoryExpenseMybatisDAO;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;

@Service("settleItemExpenseExportService")
public class SettleItemExpenseServiceImpl implements SettleItemExpenseExportService{
	private final static Logger logger = LoggerFactory.getLogger(StatementExportServiceImpl.class);
	@Resource 
	private SettleItemExpenseDAO settleItemExpenseDAO;
	@Resource
	private SettlementCategoryExpenseMybatisDAO settlementCategoryExpenseMybatisDAO;

	@Override
	public ExecuteResult<DataGrid<SettleItemExpenseDTO>> querySettleItemExpense(SettleItemExpenseDTO sieDto, Pager page) {
		ExecuteResult<DataGrid<SettleItemExpenseDTO>> result=new ExecuteResult<DataGrid<SettleItemExpenseDTO>>();
		
		try {
			DataGrid<SettleItemExpenseDTO> dataGrid=new DataGrid<SettleItemExpenseDTO>();
			List<SettleItemExpenseDTO> list=settleItemExpenseDAO.queryList( sieDto, page);
			Long count=settleItemExpenseDAO.queryCount(sieDto);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<SettleItemExpenseDTO> getSettleItemExpense(Long id) {
		
		ExecuteResult<SettleItemExpenseDTO> result=new ExecuteResult<SettleItemExpenseDTO>();
		
		try {
			SettleItemExpenseDTO sieDTO=settleItemExpenseDAO.queryById(id);
			result.setResult(sieDTO);
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		return result;
	}

	@Override
	public ExecuteResult<String> addSettleItemExpense(SettleItemExpenseDTO sieDto) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			settleItemExpenseDAO.add(sieDto);
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<String> modifySettleItemExpense(SettleItemExpenseDTO sieDto) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			settleItemExpenseDAO.update(sieDto);
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<SettlementInfoOutDTO> getSettlementInfo(SettlementInfoInDTO settlementInfoInDTO) {
		ExecuteResult<SettlementInfoOutDTO> result=new ExecuteResult<SettlementInfoOutDTO>();
		
		try {
			SettlementInfoOutDTO sioDTO=new SettlementInfoOutDTO();
			
			SettleItemExpenseDTO sieDTO=settleItemExpenseDAO.queryByItemId(settlementInfoInDTO.getItemId());
			if(sieDTO!=null && sieDTO.getRebateRate() != null){
				sioDTO.setRebateRate(sieDTO.getRebateRate());
			}else{
				List<SettleCatExpenseDTO> list = settlementCategoryExpenseMybatisDAO.queryByIds(settlementInfoInDTO.getCid());
				if(list.size()>0){
					sioDTO.setRebateRate(list.get(0).getRebateRate());
				}else{
					sioDTO.setRebateRate(BigDecimal.valueOf(0));
				}
			}
			result.setResult(sioDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		return result;
	}

	
}
