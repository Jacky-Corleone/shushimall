package com.camelot.tradecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dao.SalesVolumeDAO;
import com.camelot.tradecenter.dto.SalesVolumeDTO;
import com.camelot.tradecenter.service.SalesVolumeExportService;

@Service("salesVolumeExportService")
public class SalesVolumeExportServiceImpl implements SalesVolumeExportService {

	@Resource
	private SalesVolumeDAO salesVolumeDAO;
	
	@Override
	public ExecuteResult<List<SalesVolumeDTO>> querySkuSalesVolume(SalesVolumeDTO inDTO) {
		ExecuteResult<List<SalesVolumeDTO>> result = new ExecuteResult<List<SalesVolumeDTO>>();
		List<SalesVolumeDTO> svList = this.salesVolumeDAO.querySkuSalesVolume(inDTO);
		result.setResult(svList);
		return result;
	}

	@Override
	public ExecuteResult<List<SalesVolumeDTO>> queryShopSalesVolume(SalesVolumeDTO inDTO) {
		ExecuteResult<List<SalesVolumeDTO>> result = new ExecuteResult<List<SalesVolumeDTO>>();
		List<SalesVolumeDTO> svList = this.salesVolumeDAO.queryShopSalesVolume(inDTO);
		result.setResult(svList);
		return result;
	}

}
