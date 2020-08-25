package com.camelot.mall.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;

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
	@Resource
	private MallThemeService mallThemeService;
	
	@Override
	public JSONArray findCategory() {
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(9999);
		DataGrid<ItemCategoryDTO> categoryes = this.categoryService.queryItemCategoryAllList(page);
		LOG.debug("DUBBO获取所有类目："+JSON.toJSONString(categoryes));
		DataGrid<ItemCategoryDTO> rootCategorys = this.categoryService.queryItemCategoryList(Long.valueOf(0));
		
		
		page.setRows(10);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(4);
		MallThemeDTO mallThemeDTO = new MallThemeDTO();//查询类目子站信息
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallThemeDTO> queryMallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO, "1", null);
		List<MallThemeDTO> lists=queryMallThemeList.getRows();
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO d:rootCategorys.getRows()){
			List<MallThemeDTO> mallThems=new ArrayList<MallThemeDTO>();
			JSONObject category = JSON.parseObject(JSON.toJSONString(d));
			category.put("children", this.buildCategoryTree(d.getCategoryCid(), categoryes.getRows()));
			for(ItemCategoryDTO dto: categoryes.getRows()){
				if( dto.getCategoryParentCid().compareTo(d.getCategoryCid()) == 0 ){
					for(MallThemeDTO them:lists){
						if(them.getcId().compareTo(dto.getCategoryCid())==0){
							them.setcName(dto.getCategoryCName());
							mallThems.add(them);
						}
					}
				}
			}
			category.put("mallThems", mallThems);
			mallAdQueryDTO.setCid(d.getCategoryCid());
			DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
			category.put("bunners", dg.getRows());
			
			MallAdQueryDTO dto = new MallAdQueryDTO();
			dto.setAdType(4);
			dto.setCid(d.getCategoryCid());
			dto.setStatus(1);
			DataGrid<MallAdDTO> dgAd = mallAdvertisService.queryMallAdList(page,dto);
			category.put("mallAdvertis", dgAd.getRows());
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

	@Override
	public JSONArray findSonCategory(Long cid,Integer themId) {
		DataGrid<ItemCategoryDTO> secondCategorys = this.categoryService.queryItemCategoryList(cid);
        LOG.debug("DUBBO获取对应类目下的三级类目："+JSON.toJSONString(secondCategorys));
		JSONArray array = new JSONArray();
		for(ItemCategoryDTO d:secondCategorys.getRows()){
			JSONObject category = JSON.parseObject(JSON.toJSONString(d));
			category.put("children", this.buildCategoryAttribute(d.getCategoryCid(),themId));
			Pager page = new Pager();
			page.setPage(1);
			page.setRows(9999);
			MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
			mallAdQueryDTO.setStatus(1);
			mallAdQueryDTO.setAdType(4);
			mallAdQueryDTO.setThemeId(themId);
			mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
			mallAdQueryDTO.setCid(d.getCategoryCid());
			DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
			category.put("bunners", dg.getRows());
			array.add(category);
		}
		return array;
	}
	
	private JSONArray buildCategoryAttribute(Long cid,Integer themId){
		JSONArray array = new JSONArray();
		DataGrid<CategoryAttrDTO> res=categoryService.queryCategoryAttrList(cid, 2);
		for(CategoryAttrDTO dto: res.getRows()){
			   if(dto.getIsSenior()==0){//获取非高级属性
				   JSONObject category = JSON.parseObject(JSON.toJSONString(dto));
					array.add(category);
			   }
		}
		return array;
	}

}
