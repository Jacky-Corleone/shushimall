package com.camelot.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.service.IndexService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;
import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;
import com.camelot.sellercenter.notice.service.NoticeExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月5日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class IndexServiceImpl implements IndexService {

	@Resource
	private NoticeExportService noticeSevice;
	@Resource
	private MallBannerExportService mallBannerServer;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private MallRecAttrExportService mallRecAttrService;
	@Resource
	private ItemCategoryService categoryService;
	
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月5日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@Override
	public JSONArray findCategory() {
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(9999);
		DataGrid<ItemCategoryDTO> categoryes = this.categoryService.queryItemCategoryAllList(page);
		
		DataGrid<ItemCategoryDTO> rootCategorys = this.categoryService.queryItemCategoryList(Long.valueOf(0));
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO d:rootCategorys.getRows()){
			JSONObject category = JSON.parseObject(JSON.toJSONString(d));
			category.put("children", this.buildCategoryTree(d.getCategoryCid(), categoryes.getRows()));
			array.add(category);
		}
		return array;
	}
	
	private JSONArray buildCategoryTree(Long id,List<ItemCategoryDTO> dtos){
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO dto: dtos){
			if(dto.getCategoryParentCid() == id){
				JSONObject category = JSON.parseObject(JSON.toJSONString(dto));
				category.put("children", this.buildCategoryTree(dto.getCategoryCid(), dtos));
				array.add(category);
			}
		}
		return array;
	}
	

	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月6日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	
	@Override
	public JSONArray getFloor() {
		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setRecTypeDTO(1);
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(100);
		DataGrid<MallRecDTO> dg = mallRecService.queryMallRecList(mallRecDTO, page);
		JSONArray floors = new JSONArray();
		for(MallRecDTO d:dg.getRows()){
			JSONObject floor = JSON.parseObject(JSON.toJSONString(d));
			//楼层左侧广告词
			MallRecAttrQueryDTO mallRecAttrQueryDTO = new MallRecAttrQueryDTO();
			mallRecAttrQueryDTO.setRecType(4);
			page.setRows(10);
			DataGrid<MallRecAttrDTO> mallRecWords = this.mallRecAttrService.queryMallRecAttrList(page, mallRecAttrQueryDTO);
			floor.put("words", mallRecWords.getRows());
			//楼层左侧广告
			mallRecAttrQueryDTO.setRecType(2);
			page.setRows(1);
			DataGrid<MallRecAttrDTO> mallRecSales = this.mallRecAttrService.queryMallRecAttrList(page, mallRecAttrQueryDTO);
			floor.put("sales", mallRecSales.getRows());
			//楼层商品图片
			page.setRows(12);
			mallRecAttrQueryDTO.setRecType(1);
			DataGrid<MallRecAttrDTO> mallRecGoods = this.mallRecAttrService.queryMallRecAttrList(page, mallRecAttrQueryDTO);
			floor.put("goods", mallRecGoods.getRows());
			floors.add(floor);
		}
		return floors;
	}
	
	

}
