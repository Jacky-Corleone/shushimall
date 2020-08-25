package com.camelot.tradecenter.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>Description: [商品发布时设置上架时间定时任务]</p>
 * Created on 2015-10-27
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemShelvesJob {
	private static final Logger logger = LoggerFactory.getLogger(PayBuyerReportJob.class);
	@Resource
	private ItemExportService itemExportService;
	
	public void updateItemShelves(){
		ItemQueryInDTO itemQueryInDTO=new ItemQueryInDTO();
		itemQueryInDTO.setItemStatus(ItemStatusEnum.SHELVING.getCode());//查询待上架的商品
		DataGrid<ItemQueryOutDTO> drg=itemExportService.queryItemList(itemQueryInDTO, null);
		Date date=new Date();
		if(drg.getRows()!=null&&drg.getTotal()>0){
			for(ItemQueryOutDTO dto:drg.getRows()){
				if(dto.getTimingListing()!=null&&dto.getTimingListing().getTime()<=date.getTime()){
					ItemStatusModifyDTO itemStatusModifyDTO=new ItemStatusModifyDTO();
					List<Long> itemIds = new ArrayList<Long>();
					itemIds.add(dto.getItemId());
					itemStatusModifyDTO.setItemIds(itemIds);
					itemStatusModifyDTO.setOperator(dto.getOperator());
					itemStatusModifyDTO.setItemStatus(ItemStatusEnum.SALING.getCode());//改为上架状态
					ExecuteResult<String> res=itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
				}
			}
		}
	}
}
