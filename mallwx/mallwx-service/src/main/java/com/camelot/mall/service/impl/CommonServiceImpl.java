package com.camelot.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.common.CommonService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月16日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class CommonServiceImpl implements CommonService{
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private ItemCategoryService categoryService;
	@Resource
	private MallAdExportService mallAdvertisService;
	
	@Override
	public JSONArray findCategory() {
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(9999);
		DataGrid<ItemCategoryDTO> categoryes = this.categoryService.queryItemCategoryAllList(page);
		//LOG.debug("DUBBO获取所有类目："+JSON.toJSONString(categoryes));
		DataGrid<ItemCategoryDTO> rootCategorys = this.categoryService.queryItemCategoryList(Long.valueOf(0));
		
		
		page.setRows(10);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(4);
		
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO d:rootCategorys.getRows()){
			JSONObject category = JSON.parseObject(JSON.toJSONString(d));
			category.put("children", this.buildCategoryTree(d.getCategoryCid(), categoryes.getRows()));
			
			mallAdQueryDTO.setCid(d.getCategoryCid());
			DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
			category.put("bunners", dg.getRows());
			
			array.add(category);
		}
		return array;
	}
	
	private JSONArray buildCategoryTree(Long id,List<ItemCategoryDTO> dtos){
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO dto: dtos){
			if( dto.getCategoryParentCid().compareTo(id) == 0 ){
				JSONObject category = JSON.parseObject(JSON.toJSONString(dto));
				category.put("children", this.buildCategoryTree(dto.getCategoryCid(), dtos));
				array.add(category);
			}
		}
		return array;
	}

}
