package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopSalesVolumeDTO;

public interface ShopSalesVolumeExportService {

	ExecuteResult<String>  updateShopVolume(List<ShopSalesVolumeDTO> inList);
	
}
