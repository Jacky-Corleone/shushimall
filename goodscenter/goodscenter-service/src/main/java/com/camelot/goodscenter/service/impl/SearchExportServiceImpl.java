package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemCategoryDAO;
import com.camelot.goodscenter.domain.ItemCategory;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.goodscenter.service.SearchService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [搜索功能实现类]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("searchItemExportService")
public class SearchExportServiceImpl implements SearchItemExportService{
	private static final Logger LOGGER=LoggerFactory
			.getLogger(SearchExportServiceImpl.class);
	
	@Resource
	private ItemCategoryDAO itemCategoryDAO;
	@Resource
	private SearchService searchService;
	

	@SuppressWarnings("rawtypes")
	@Override
	public SearchOutDTO searchItem(SearchInDTO inDTO,Pager pager) {
		SearchOutDTO result = new SearchOutDTO();
		try {
			List<Long> catIdSet = new ArrayList<Long>();//类目ID
			Long cid = inDTO.getCid();
			if(cid!=null){//类目ID 转换为三级类目ID组
				List<ItemCategoryDTO> catsList = this.itemCategoryDAO.queryThirdCatsList(cid);
				for (ItemCategoryDTO itemCategory : catsList) {
					catIdSet.add(itemCategory.getCategoryCid());
				}
				if(catsList.size() == 0)
				{
					catIdSet.add(-1L);
				}
			}
			inDTO.setCategoryIds(catIdSet);
			if(StringUtils.isBlank(inDTO.getAreaCode())){
				inDTO.setAreaCode("0");
			}
			
			//商品属性分解
			List<String> attrList = this.getItemAttrStrList(inDTO.getAttributes());
			inDTO.setAttrList(attrList);
			//分页查询商品
			DataGrid<ItemSkuDTO> resultDg = this.searchService.queryItemSkus(inDTO,pager);
			//根据输入的条件，反推出应该返回的条件
			result = this.searchService.getSearchConditions(inDTO);
			result.setItemDTOs(resultDg);
			
		} catch (Exception e) {
			LOGGER.error("执行方法【searchItem】报错:{}",e);
			throw new RuntimeException(e);
		}
		return result;
	}


	/**
	 * 
	 * <p>Discription:[获取类目属性字符串列表]</p>
	 * Created on 2015-4-22
	 * @param attributes
	 * @return
	 * @author:wangcs
	 */
	private List<String> getItemAttrStrList(String attributes) {
		List<String> list = new ArrayList<String>();
		if(StringUtils.isBlank(attributes)){
			return list;
		}
		String[] attrArray = attributes.split(";");
		for (String attrStr : attrArray) {
			list.add(attrStr);
		}
		return list;
	}

}
