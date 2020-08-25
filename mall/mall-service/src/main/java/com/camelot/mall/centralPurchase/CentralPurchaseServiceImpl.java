package com.camelot.mall.centralPurchase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityStatusEnum;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.mall.service.ItemInfoService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [用户积分]</p>
 * Created on 2015-12-9
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class CentralPurchaseServiceImpl implements
		CentralPurchaseService {
	
	private final static Logger log = LoggerFactory.getLogger(CentralPurchaseServiceImpl.class);
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	@Resource
	private ItemInfoService itemInfoService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Override
	/**
	 * 
	 * <p>Description: [获取集采活动]</p>
	 * Created on 2015-12-18
	 * @author 周志军
	 * @param page
	 * @return 
	 */
	public Pager<QueryCentralPurchasingDTO> getCentralPurchase(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page){
		// 不需要查询的状态
		List<Integer> notActivityStatusList = new ArrayList<Integer>();
		// 未发布
		notActivityStatusList.add(CentralPurchasingActivityStatusEnum.UNPUBLISHED.getCode());
		queryCentralPurchasingDTO.setNotActivityStatusList(notActivityStatusList);
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> result = centralPurchasingExportService
				.queryCentralPurchasingActivity(queryCentralPurchasingDTO, page);
		List<JSONObject> centralPurchasingActivitys = new ArrayList<JSONObject>();
		if (result.isSuccess() 
				&& result.getResult() != null 
				&& result.getResult().getRows() != null
				&& result.getResult().getRows().size() > 0) {
			for (QueryCentralPurchasingDTO centralPurchasingDTO : result.getResult().getRows()) {
				JSONObject centralPurchasing = this.convertToJSONObject(centralPurchasingDTO);
				centralPurchasingActivitys.add(centralPurchasing);
			}
			page.setTotalCount(result.getResult().getTotal().intValue());
			page.setRecords(centralPurchasingActivitys);
		}
		return page;
	}
	
	/**
	 * 
	 * <p>Description: [将集采活动DTO转换成JSONObject，同时查询出商品的销售属性]</p>
	 * Created on 2015年12月2日
	 * @param centralPurchasingDTO
	 * @return
	 * @author:[宋文斌]
	 */
	private JSONObject convertToJSONObject(QueryCentralPurchasingDTO centralPurchasingDTO) {
		JSONObject centralPurchasing = (JSONObject) JSON.toJSON(centralPurchasingDTO);
		// 查询销售属性
		ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(centralPurchasingDTO.getSkuId());
		if (result_attr != null && result_attr.getResult() != null) {
			String attrStr = result_attr.getResult();
			// 根据sku的销售属性keyId:valueId查询商品属性
			ExecuteResult<List<ItemAttr>> attributes = itemCategoryService.queryCatAttrByKeyVals(attrStr);
			if (attributes != null && attributes.getResult() != null && attributes.getResult().size() > 0) {
				centralPurchasing.put("attributes", attributes.getResult());
			}
		}
		if(centralPurchasingDTO.getItemId() != null){
			ItemDTO itemDTO = itemInfoService.getItemInfoById(centralPurchasingDTO.getItemId() + "");
			if(itemDTO != null){
				centralPurchasing.put("originalPrice", itemDTO.getMarketPrice());
			}
		}
		return centralPurchasing;
	}

}
