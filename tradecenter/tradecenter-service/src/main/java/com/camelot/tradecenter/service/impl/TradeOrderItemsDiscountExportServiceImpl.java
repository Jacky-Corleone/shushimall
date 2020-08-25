package com.camelot.tradecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dao.TradeOrderItemsDiscountExportDAO;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.tradecenter.service.TradeOrderItemsDiscountExportService;

@Service("tradeOrderItemsDiscountExportService")
public class TradeOrderItemsDiscountExportServiceImpl implements
		TradeOrderItemsDiscountExportService {
	
	@Resource
	TradeOrderItemsDiscountExportDAO tradeOrderItemsDiscountExportDAO;

	@Override
	public ExecuteResult<String> createOrderItemsDiscount(TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO) {
		ExecuteResult<String> ex = new ExecuteResult<String>();
		try {
			this.tradeOrderItemsDiscountExportDAO.add(tradeOrderItemsDiscountDTO);
			ex.setResultMessage("添加成功");
		} catch (Exception e) {
			ex.addErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return ex;
	}

	@Override
	public ExecuteResult<TradeOrderItemsDiscountDTO> queryOrderItemsDiscountById(Integer orderItemsId) {
		ExecuteResult<TradeOrderItemsDiscountDTO> executeResult = new ExecuteResult<TradeOrderItemsDiscountDTO>();
		try {
			TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO = this.tradeOrderItemsDiscountExportDAO.queryByOrderItemsId(orderItemsId);
			executeResult.setResult(tradeOrderItemsDiscountDTO);
		} catch (Exception e) {
			executeResult.addErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return executeResult;
	}

	@Override
	public ExecuteResult<DataGrid<TradeOrderItemsDiscountDTO>> queryOrderItemsDiscountByCondition(
			TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO,
			Pager<TradeOrderItemsDiscountDTO> pager) {
		ExecuteResult<DataGrid<TradeOrderItemsDiscountDTO>> executeResult = new ExecuteResult<DataGrid<TradeOrderItemsDiscountDTO>>();
		DataGrid<TradeOrderItemsDiscountDTO> resultPager = new DataGrid<TradeOrderItemsDiscountDTO>();
		long size =  this.tradeOrderItemsDiscountExportDAO.queryCount(tradeOrderItemsDiscountDTO);
		if(size<=0){
			return executeResult;
		}
		List<TradeOrderItemsDiscountDTO> trDTOs = this.tradeOrderItemsDiscountExportDAO.queryList(tradeOrderItemsDiscountDTO, pager);
		resultPager.setRows(trDTOs);
		resultPager.setTotal(size);
		executeResult.setResult(resultPager);
		return executeResult;
	}

}
