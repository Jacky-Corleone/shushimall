package com.camelot.maketcenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.maketcenter.dao.PromotionFullReductionDAO;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("promotionFrExportService")
public class PromotionFullReductionServiceImpl implements PromotionFrExportService{
	private final static Logger logger = LoggerFactory.getLogger(PromotionFullReductionServiceImpl.class);
	@Resource
	private PromotionFullReductionDAO promotionFullReductionDAO;
	@Override
	public ExecuteResult<DataGrid<PromotionFullReduction>> queryPromotionFrList(PromotionFullReduction promotionFullReduction, Pager page)
	{
		ExecuteResult<DataGrid<PromotionFullReduction>> result=new ExecuteResult<DataGrid<PromotionFullReduction>>();
		try {
			DataGrid<PromotionFullReduction> dataGrid=new DataGrid<PromotionFullReduction>();
			List<PromotionFullReduction> list = promotionFullReductionDAO.queryList(promotionFullReduction, page);
			Long count = promotionFullReductionDAO.queryCount(promotionFullReduction);
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

	/**
	 * 删除满减
	 * @param promotionInfoId
	 * @return
	 */
	public ExecuteResult<String> deleteFrByPromotionId(Long promotionInfoId){
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			Integer num = promotionFullReductionDAO.deleteByPromotionId(promotionInfoId);
			if(num>0){
				result.setResultMessage("success");
			}else{
				result.setResultMessage("error");
			}
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 删除满减
	 * @param promotionFullReduction
	 * @return
	 */
	public ExecuteResult<String> addFrByPromotionId(PromotionFullReduction promotionFullReduction){
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			promotionFullReductionDAO.add(promotionFullReduction);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public boolean queryActivityCheck(Long shopId,Long[] itemIds){
		if(promotionFullReductionDAO.queryActivityCheck(shopId,itemIds)>0){
			return true;
		}
		return false;
	}

}
