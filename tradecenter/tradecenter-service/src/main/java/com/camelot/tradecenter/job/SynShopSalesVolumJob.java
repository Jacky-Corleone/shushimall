package com.camelot.tradecenter.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.camelot.storecenter.dto.ShopSalesVolumeDTO;
import com.camelot.storecenter.service.ShopSalesVolumeExportService;
import com.camelot.tradecenter.dto.SalesVolumeDTO;
import com.camelot.tradecenter.service.SalesVolumeExportService;

public class SynShopSalesVolumJob {

	private static final Logger logger = LoggerFactory.getLogger(SynShopSalesVolumJob.class);
	
	@Resource
	private ShopSalesVolumeExportService shopSalesVolumeExportService;
	@Resource
	private SalesVolumeExportService salesVolumeExportService;
	
	public void updateShopSalesVolume(){
		logger.info("=============开始更新商品销量===============");
		List<SalesVolumeDTO> salesVolumeList = this.salesVolumeExportService.queryShopSalesVolume(new SalesVolumeDTO()).getResult();
		logger.info("======查询出的销量：====="+JSON.toJSONString(salesVolumeList));
		List<ShopSalesVolumeDTO> resultList = this.getShopSaleVolumeList(salesVolumeList);
		logger.info("======组合后的销量：====="+JSON.toJSONString(resultList));
		this.shopSalesVolumeExportService.updateShopVolume(resultList);
		logger.info("============结束更新商品销量================");
	}

	private List<ShopSalesVolumeDTO> getShopSaleVolumeList(List<SalesVolumeDTO> salesVolumeList) {
		List<ShopSalesVolumeDTO> resultList = new ArrayList<ShopSalesVolumeDTO>();
		ShopSalesVolumeDTO shopSv = null;
		for (SalesVolumeDTO inDTO : salesVolumeList) {
			shopSv = new ShopSalesVolumeDTO();
			shopSv.setSalesVolume(inDTO.getSalesVolume());
			shopSv.setSellerId(inDTO.getSellerId());
			shopSv.setShopId(inDTO.getShopId());
			resultList.add(shopSv);
		}
		return resultList;
	}
	
}
