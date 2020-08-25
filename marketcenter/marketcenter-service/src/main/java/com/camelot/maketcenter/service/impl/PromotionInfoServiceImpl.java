package com.camelot.maketcenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.maketcenter.dao.PromotionInfoDAO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.service.PromotionInfoExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("promotionInfoExportService")
public class PromotionInfoServiceImpl implements PromotionInfoExportService{
	private final static Logger logger = LoggerFactory.getLogger(PromotionInfoServiceImpl.class);

	@Resource 
	private PromotionInfoDAO promotionInfoDAO;
	@Override
	public ExecuteResult<DataGrid<PromotionInfo>> queryPromotionInfoList(PromotionInfo promotionInfo, Pager page) {
		
		ExecuteResult<DataGrid<PromotionInfo>> result=new ExecuteResult<DataGrid<PromotionInfo>>();
		
		try {
			DataGrid<PromotionInfo> dataGrid=new DataGrid<PromotionInfo>();
			List<PromotionInfo> list = promotionInfoDAO.queryList(promotionInfo, page);
			Long count = promotionInfoDAO.queryCount(promotionInfo);
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
	
	public boolean queryActivityCheck(Long shopId){
		if(promotionInfoDAO.queryActivityCheck(shopId)>0){
			return true;
		}
		return false;
	}
}
