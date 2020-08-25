package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.ItemSalesVolumeDTO;
import com.camelot.openplatform.common.ExecuteResult;

public interface ItemSalesVolumeExportService {

	public ExecuteResult<String> updateItemSalesVolume(List<ItemSalesVolumeDTO> inList);
	
}
