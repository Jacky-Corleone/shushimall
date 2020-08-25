package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemBrandDAO;
import com.camelot.goodscenter.dao.ItemCategoryDAO;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.dao.ItemPictureDAO;
import com.camelot.goodscenter.dao.SearchDAO;
import com.camelot.goodscenter.domain.Item;
import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemPriceService;
import com.camelot.goodscenter.service.SearchService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

@Service
public class SearchServiceImpl implements SearchService {

	@Resource
	private SearchDAO searchDAO;
	@Resource
	private ItemPriceService itemPriceService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemBrandDAO itemBrandDAO;
	@Resource
	private ItemCategoryDAO itemCategoryDAO;
	@Resource
	private ItemPictureDAO itemPictureDAO;
	
	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<ItemSkuDTO> queryItemSkus(SearchInDTO inDTO, Pager pager) throws Exception {
		DataGrid<ItemSkuDTO> dg = new DataGrid<ItemSkuDTO>();
		List<ItemSkuDTO> itemSkus = this.searchDAO.queryItemSkus(inDTO,pager);
		long count = this.searchDAO.queryItemSkusCount(inDTO, null);
		PriceQueryParam param = new PriceQueryParam();
		for (ItemSkuDTO itemSkuDTO : itemSkus) {
			param.setAreaCode(inDTO.getAreaCode());
			param.setItemId(itemSkuDTO.getItemId());
			param.setQty(1);
			param.setShopId(itemSkuDTO.getShopId());
			param.setSkuId(itemSkuDTO.getSkuId());
			param.setBuyerId(inDTO.getBuyerId());
			param.setSellerId(itemSkuDTO.getSellerId());
			//SKU价格
			itemSkuDTO.setSkuPrice(this.itemPriceService.getSkuShowPrice(param));
			//SKU询价
			itemSkuDTO.setSkuInquiryPirce(this.itemPriceService.getInquiryPrice(param));
			//SKU属性
			itemSkuDTO.setAttributes(this.itemCategoryService.queryCatAttrByKeyVals(itemSkuDTO.getSkuAttributeStr()).getResult());
			//SKU照片
			itemSkuDTO.setPicUrl(this.itemMybatisDAO.querySkuPics(itemSkuDTO.getSkuId()).get(0).getPicUrl());
			//商品照片
			itemSkuDTO.setItemPicUrl(this.itemPictureDAO.queryItemPicsById(itemSkuDTO.getItemId()).get(0).getPictureUrl());
		}
		dg.setRows(itemSkus);
		dg.setTotal(count);
		return dg;
	}

	@Override
	public SearchOutDTO getSearchConditions(SearchInDTO inDTO) {
		SearchOutDTO result = new SearchOutDTO();
		List<Item> itemIds = this.searchDAO.queryConditionIds(inDTO);
		List<String> attrStrs = new ArrayList<String>();
		List<Long> brandIds = new ArrayList<Long>();
		List<Long> catIds = new ArrayList<Long>();
		for (Item item : itemIds) {
			//整理品牌ID
			if(!brandIds.contains(item.getBrand())){
				brandIds.add(item.getBrand());
			}
			//整理类目ID
			if(!catIds.contains(item.getCid())){
				catIds.add(item.getCid());
			}
			//整理属性
			if(!attrStrs.contains(item.getAttributes())){
				attrStrs.add(item.getAttributes());
			}
		}
		//品牌
		List<ItemBrandDTO> brands = this.itemBrandDAO.queryBrandByIds(brandIds);
		//类目
		List<ItemCatCascadeDTO> cats = this.itemCategoryService.queryParentCategoryList(catIds.toArray(new Long[]{})).getResult();
		//商品非高级非销售属性
		List<ItemAttr> attrs = this.getItemAttrs(attrStrs);
		//商品高级非销售属性
		List<ItemAttr> seniorAttrs = this.getSeniorItemAttrs(attrStrs);
		
		result.setBrands(brands);
		result.setCategories(cats);
		result.setAttributes(attrs);
		result.setSeniorAttributes(seniorAttrs);
		return result;
	}

	/**
	 * 
	 * <p>Discription:[获取搜索用的非高级类目属性]</p>
	 * Created on 2015-3-26
	 * @param attrStrs
	 * @return
	 * @author:wangcs
	 */
	private List<ItemAttr> getItemAttrs(List<String> attrStrs) {
		List<ItemAttr> attrList = new ArrayList<ItemAttr>();
		List<String> attrIds = new ArrayList<String>();
		List<String> attrValIds = new ArrayList<String>();
		for (String attributesStr : attrStrs) {
			if(!StringUtils.isBlank(attributesStr)){
				String[] keyVals = attributesStr.split(";");
				String[] strs = null;
				for (String str : keyVals) {
					strs = str.split(",");
					for(String keyVal : strs){
						String[] kvs = keyVal.split(":");
						if(!attrIds.contains(kvs[0])){
							attrIds.add(kvs[0]);
						}
						if(!attrValIds.contains(kvs[1])){
							attrValIds.add(kvs[1]);
						}
					}
					
				}
			}
		}
		if(attrIds==null || attrIds.size()<=0){
			return attrList;
		}
		attrList = this.itemCategoryDAO.queryItemAttrListByIsSenior(attrIds, 0);//非高级类目属性
		for (ItemAttr itemAttr : attrList) {
			List<ItemAttrValue> valueList = this.itemCategoryDAO.queryItemAttrValueList(itemAttr.getId(), attrValIds);
			itemAttr.setValues(valueList);
		}
		return attrList;
	}
	/**
	 * 
	 * <p>Discription:[获取高级类目属性]</p>
	 * Created on 2015年12月24日
	 * @param attrStrs
	 * @return
	 * @author:[王鹏]
	 */
	private List<ItemAttr> getSeniorItemAttrs(List<String> attrStrs) {
		List<ItemAttr> attrList = new ArrayList<ItemAttr>();
		List<String> attrIds = new ArrayList<String>();
		List<String> attrValIds = new ArrayList<String>();
		for (String attributesStr : attrStrs) {
			if(!StringUtils.isBlank(attributesStr)){
				String[] keyVals = attributesStr.split(";");
				String[] strs = null;
				for (String str : keyVals) {
					strs = str.split(",");
					for(String keyVal : strs){
						String[] kvs = keyVal.split(":");
						if(!attrIds.contains(kvs[0])){
							attrIds.add(kvs[0]);
						}
						if(!attrValIds.contains(kvs[1])){
							attrValIds.add(kvs[1]);
						}
					}
					
				}
			}
		}
		if(attrIds==null || attrIds.size()<=0){
			return attrList;
		}
		attrList = this.itemCategoryDAO.queryItemAttrListByIsSenior(attrIds, 1);//非高级类目属性
		for (ItemAttr itemAttr : attrList) {
			List<ItemAttrValue> valueList = this.itemCategoryDAO.queryItemAttrValueList(itemAttr.getId(), attrValIds);
			itemAttr.setValues(valueList);
		}
		return attrList;
	}
}
