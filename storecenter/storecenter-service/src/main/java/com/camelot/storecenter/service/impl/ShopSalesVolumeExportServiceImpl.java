package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dao.ShopSalesVolumeDAO;
import com.camelot.storecenter.dto.ShopSalesVolumeDTO;
import com.camelot.storecenter.service.ShopSalesVolumeExportService;

@Service("shopSalesVolumeExportService")
public class ShopSalesVolumeExportServiceImpl implements ShopSalesVolumeExportService {

	private static final Logger logger = LoggerFactory.getLogger(ShopSalesVolumeExportServiceImpl.class);
	
	@Resource
	private ShopSalesVolumeDAO shopSalesVolumeDAO;
	
	@Override
	public ExecuteResult<String> updateShopVolume(List<ShopSalesVolumeDTO> inList) {
		logger.info("============开始更新店铺销量==============");
		ExecuteResult<String> result = new ExecuteResult<String>();
		this.shopSalesVolumeDAO.deleteAll();
		this.shopSalesVolumeDAO.insertSaleVolume(inList);
		logger.info("============结束更新店铺销量==============");
		return result;
	}

}
