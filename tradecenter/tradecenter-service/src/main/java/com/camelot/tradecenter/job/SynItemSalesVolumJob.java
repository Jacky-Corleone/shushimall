package com.camelot.tradecenter.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.ItemSalesVolumeDTO;
import com.camelot.goodscenter.service.ItemSalesVolumeExportService;
import com.camelot.tradecenter.dto.SalesVolumeDTO;
import com.camelot.tradecenter.service.SalesVolumeExportService;

public class SynItemSalesVolumJob {

	private static final Logger logger = LoggerFactory.getLogger(SynItemSalesVolumJob.class);
	
	@Resource
	private ItemSalesVolumeExportService itemSalesVolumeExportService;
	@Resource
	private SalesVolumeExportService salesVolumeExportService;
	
	public void updateItemSalesVolume(){
		logger.info("=============开始更新商品销量===============");
		List<SalesVolumeDTO> salesVolumeList = this.salesVolumeExportService.querySkuSalesVolume(new SalesVolumeDTO()).getResult();
		logger.info("======查询出的销量：====="+JSON.toJSONString(salesVolumeList));
		List<ItemSalesVolumeDTO> resultList = this.getItemSaleVolumeList(salesVolumeList);
		logger.info("======组合后的销量：====="+JSON.toJSONString(resultList));
		this.itemSalesVolumeExportService.updateItemSalesVolume(resultList);
		logger.info("============结束更新商品销量================");
	}

	private List<ItemSalesVolumeDTO> getItemSaleVolumeList(List<SalesVolumeDTO> salesVolumeList) {
		List<ItemSalesVolumeDTO> resultList = new ArrayList<ItemSalesVolumeDTO>();
		ItemSalesVolumeDTO itemSv = null;
		for (SalesVolumeDTO inDTO : salesVolumeList) {
			itemSv = new ItemSalesVolumeDTO();
			BeanUtils.copyProperties(inDTO, itemSv);
			resultList.add(itemSv);
		}
		return resultList;
	}
	
}
