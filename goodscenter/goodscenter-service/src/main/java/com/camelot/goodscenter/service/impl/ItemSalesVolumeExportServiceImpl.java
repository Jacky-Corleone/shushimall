package com.camelot.goodscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemSalesVolumeDAO;
import com.camelot.goodscenter.dto.ItemSalesVolumeDTO;
import com.camelot.goodscenter.service.ItemSalesVolumeExportService;
import com.camelot.openplatform.common.ExecuteResult;

@Service("itemSalesVolumeExportService")
public class ItemSalesVolumeExportServiceImpl implements ItemSalesVolumeExportService {

	@Resource
	private ItemSalesVolumeDAO itemSalesVolumeDAO;
	
	@Override
	public ExecuteResult<String> updateItemSalesVolume(List<ItemSalesVolumeDTO> inList) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		//TODO 更新销量
		if(inList==null || inList.size()<=0){
			result.addErrorMessage("入参为空！");
			return result;
		}
		this.itemSalesVolumeDAO.deleteAll();
		this.itemSalesVolumeDAO.addList(inList);
		return result;
	}

}
