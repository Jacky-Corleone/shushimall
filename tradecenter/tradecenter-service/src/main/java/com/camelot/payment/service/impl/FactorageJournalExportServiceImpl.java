package com.camelot.payment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.FactorageJournalExportService;
import com.camelot.payment.dao.FactorageJournalDAO;
import com.camelot.payment.domain.FactorageJournal;
@Service("factorageJournalExportService")
public class FactorageJournalExportServiceImpl implements FactorageJournalExportService{
	@Resource
	private FactorageJournalDAO factorageJournalDAO;
	@Override
	public ExecuteResult<List<FactorageJournal>> selectByOrderNoAndStatus(
			String orderNo, Integer status) {
		ExecuteResult<List<FactorageJournal>> result = new ExecuteResult<List<FactorageJournal>>();
		List<FactorageJournal> factorageJournalList = factorageJournalDAO.selectByOrderNoAndStatus(orderNo, status);
		result.setResult(factorageJournalList);
		return result;
	}

	@Override
	public ExecuteResult<Integer> updateStatusById(Long id, int status) {
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		int count = factorageJournalDAO.updateStatusById(id, status);
		result.setResult(count);
		return result;
	}

}
