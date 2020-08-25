package com.camelot.searchcenter.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.searchcenter.dto.SearchBrand;
import com.camelot.searchcenter.dto.SearchCategory;
import com.camelot.searchcenter.dto.SearchItemAttr;
import com.camelot.searchcenter.dto.SearchItemAttrValue;
import com.camelot.searchcenter.dto.SearchItemSku;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.dto.SearchShopDTO;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopSearchExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

@Service("searchExportService")
public class SearchExportServiceImpl implements SearchExportService{
	private Logger logger = Logger.getLogger(SearchExportServiceImpl.class);

	@Resource
	private SearchItemExportService searchItemExportService;
	@Resource
	private ShopSearchExportService shopSearchExportService;
	@Resource
	private UserExportService userService;
	
	@SuppressWarnings({ "rawtypes"})
	public DataGrid<SearchShopDTO> searchShop(String keyword,Pager pager,Integer sort,Long buyerId,String areaCode) {
		logger.info("===========开始搜索店铺==============");
		// 根据关键词查询店铺
		ShopDTO shop = new ShopDTO();
		shop.setKeyword(keyword);
		shop.setRunStatus(1);
		if(sort!=null){
			shop.setCollation(sort);
		}
		ExecuteResult<DataGrid<ShopDTO>> erShop = this.shopSearchExportService.searchShop(shop, pager);
		DataGrid<SearchShopDTO> dg = new DataGrid<SearchShopDTO>();
		// 拼装返回数据 
		if( erShop.isSuccess() ){
			DataGrid<ShopDTO> dgShop = erShop.getResult();
			
			dg.setTotal(dgShop.getTotal());
			dg.setRows(new ArrayList<SearchShopDTO>());
			
			for( ShopDTO sd : erShop.getResult().getRows() ){
				SearchShopDTO ssd = new SearchShopDTO();
				ssd.setShopId(sd.getShopId());
				ssd.setShopName(sd.getShopName());
				ssd.setShopLogoUrl(sd.getLogoUrl());
				ssd.setServiceScore(sd.getScope());
				
				UserDTO user = this.userService.queryUserById(sd.getSellerId());
				ssd.setSellerName(user.getUname());
				
				// 查询店铺推荐商品
				ssd.setRecommendItems(this.getRecommendItems(sd.getShopId(),buyerId,areaCode));
				
				dg.getRows().add(ssd);
			}
		}
		logger.info("===========结束搜索店铺==============");
		return dg;
	}
	
	@Override
	public DataGrid<SearchShopDTO> searchShopByPlatformId(Integer platformId, String keyword, Pager pager,
			Integer sort, Long buyerId,String areaCode) {
		logger.info("===========开始搜索店铺==============");
		// 根据关键词查询店铺
		ShopDTO shop = new ShopDTO();
		shop.setKeyword(keyword);
		shop.setRunStatus(1);
		shop.setPlatformId(platformId);
		if(sort!=null){
			shop.setCollation(sort);
		}
		ExecuteResult<DataGrid<ShopDTO>> erShop = this.shopSearchExportService.searchShop(shop, pager);
		DataGrid<SearchShopDTO> dg = new DataGrid<SearchShopDTO>();
		// 拼装返回数据 
		if( erShop.isSuccess() ){
			DataGrid<ShopDTO> dgShop = erShop.getResult();
			
			dg.setTotal(dgShop.getTotal());
			dg.setRows(new ArrayList<SearchShopDTO>());
			
			for( ShopDTO sd : erShop.getResult().getRows() ){
				SearchShopDTO ssd = new SearchShopDTO();
				ssd.setShopId(sd.getShopId());
				ssd.setShopName(sd.getShopName());
				ssd.setShopLogoUrl(sd.getLogoUrl());
				ssd.setServiceScore(sd.getScope());
				ssd.setShopUrl(sd.getShopUrl());
				
				UserDTO user = this.userService.queryUserById(sd.getSellerId());
				ssd.setSellerName(user.getUname());
				
				// 查询店铺推荐商品
				ssd.setRecommendItems(this.getRecommendItems(sd.getShopId(),buyerId,areaCode));
				
				dg.getRows().add(ssd);
			}
		}
		logger.info("===========结束搜索店铺==============");
		return dg;
	}
	
	/**
	 * 
	 * <p>Discription: 通过店铺ID查询店铺推荐商品</p>
	 * Created on 2015年3月13日
	 * @param shopId
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<SearchItemSku> getRecommendItems(Long shopId,Long buyerId,String areaCode){
		List<SearchItemSku> items = new ArrayList<SearchItemSku>();
		
		SearchInDTO inDTO = new SearchInDTO();
		inDTO.setShopId(shopId);
		inDTO.setBuyerId(buyerId);
		if(StringUtils.isBlank(areaCode)){
			inDTO.setAreaCode("0");
		}else{
			inDTO.setAreaCode(areaCode);
		}
		@SuppressWarnings("rawtypes")
		Pager p = new Pager();
		p.setPage(1);
		p.setRows(5);
		DataGrid<ItemSkuDTO>  dgItems = this.searchItemExportService.searchItem(inDTO,p).getItemDTOs();
		SearchItemSku recItem = null;
		for( ItemSkuDTO item:dgItems.getRows() ){
			recItem = new SearchItemSku();
			BeanUtils.copyProperties(item, recItem);
			items.add(recItem);
		}
		return items;
	}
	
	
//	public SearchItemOutDTO searchItem(SearchItemInDTO inDto) {
//		SearchInDTO inDTO = this.getInDTO(inDto);
//		SearchOutDTO queryResult = searchService.searchItem(inDTO);
//		SearchItemOutDTO result = this.getSearchItemOutDTO(queryResult);
//		return result;
//	}

	/**
	 * 
	 * <p>Discription:[商品中心]</p>
	 * Created on 2015-3-6
	 * @param queryResult
	 * @return
	 * @author:wangcs
	 */
//	private SearchItemOutDTO getSearchItemOutDTO(SearchOutDTO queryResult) {
//		SearchItemOutDTO re = new SearchItemOutDTO();
//		re.setAttributes(this.getSearchAttrList(queryResult.getAttributes()));
//		re.setBrands(this.getBrandList(queryResult.getBrands()));
//		re.setCategories(this.getCatList(queryResult.getCategories()));
//		re.setItems(this.getSearchItems(queryResult.getItems()));
//		return re;
//	}
	

	/**
	 * 
	 * <p>Discription:[把从商品中心获取的类目数据转换成搜索中心的类目数据]</p>
	 * Created on 2015-3-6
	 * @param cats
	 * @return
	 * @author:wangcs
	 */
	private List<SearchCategory> getCatList(List<ItemCatCascadeDTO> cats) {
		List<SearchCategory> result = new ArrayList<SearchCategory>();
		SearchCategory sc = null;
		for (ItemCatCascadeDTO cat : cats) {
			sc = new SearchCategory();
			sc.setCategoryId(cat.getCid());
			sc.setCategoryName(cat.getCname());
			this.setChildCats(sc,cat.getChildCats(),2);
			result.add(sc);
		}
		return result;
	}

	private void setChildCats(SearchCategory sc, List<ItemCatCascadeDTO> childCats,int lev) {
		List<SearchCategory> secCats = new ArrayList<SearchCategory>();
		SearchCategory scat = null;
		if(childCats != null){
			for (ItemCatCascadeDTO cat : childCats) {
				scat = new SearchCategory();
				scat.setCategoryId(cat.getCid());
				scat.setCategoryName(cat.getCname());
				setChildCats(scat, cat.getChildCats(), --lev);
				secCats.add(scat);
			}
		}
		sc.setChildCats(secCats);
		if(lev==0){
			return;
		}
	}

	/**
	 * 
	 * <p>Discription:[从商品中心获取的品牌信息转换成搜索中心的品牌]</p>
	 * Created on 2015-3-6
	 * @param brands
	 * @return
	 * @author:wangcs
	 */
	private List<SearchBrand> getBrandList(List<ItemBrandDTO> brands) {
		List<SearchBrand> list = new ArrayList<SearchBrand>();
		SearchBrand sb = null;
		for (ItemBrandDTO b : brands) {
			sb = new SearchBrand();
			sb.setBrandId(b.getBrandId());
			sb.setBrandName(b.getBrandName());
			sb.setBrandLogoUrl(b.getBrandLogoUrl());
			list.add(sb);
		}
		return list;
	}

	/**
	 * 
	 * <p>Discription:[从商品中心查询的结果转换成搜索中心的数据]</p>
	 * Created on 2015-3-6
	 * @param attributes 属性值
	 * @return
	 * @author:wangcs
	 */
	private List<SearchItemAttr> getSearchAttrList(List<ItemAttr> attrs) {
		List<SearchItemAttr> list = new ArrayList<SearchItemAttr>();
		for (ItemAttr attr : attrs) {
			SearchItemAttr sa = new SearchItemAttr();
			sa.setId(attr.getId());
			sa.setName(attr.getName());
			sa.setValues(this.getAttrValues(attr.getValues()));
            if(sa.getValues().size() >1)
			    list.add(sa);
		}
        Collections.sort(list);
        return list;
	}

	/**
	 * 
	 * <p>Discription:[从商品中心获取的属性值转换成搜索中心的属性值]</p>
	 * Created on 2015-3-6
	 * @param values 属性值列表
	 * @return
	 * @author:wangcs
	 */
	private List<SearchItemAttrValue> getAttrValues(List<ItemAttrValue> values) {
		List<SearchItemAttrValue> list = new ArrayList<SearchItemAttrValue>();
		SearchItemAttrValue sav = null;
		for (ItemAttrValue av : values) {
			sav = new SearchItemAttrValue();
			sav.setId(av.getId());
			sav.setName(av.getName());
			list.add(sav);
		}
		return list;
	}

	/**
	 * 
	 * <p>Discription:[拼装查询调取商品接口入参]</p>
	 * Created on 2015-3-6
	 * @param inDto
	 * @return
	 * @author:wangcs
	 */
//	private SearchInDTO getInDTO(SearchItemInDTO in) {
//		SearchInDTO result = new SearchInDTO();
//		result.setBrandIds(in.getBrandIds());
//		result.setCid(in.getCid());
//		result.setKeyword(in.getKeyword());
//		return result;
//	}

	
	
	/***  ===========================华丽的分割线===================================  ***/
	
	
	@Override
	public SearchItemSkuOutDTO searchItemSku(SearchItemSkuInDTO inDTO) {
		logger.info("===========开始搜索商品==============");
		SearchItemSkuOutDTO result = new SearchItemSkuOutDTO();
		SearchInDTO itemParam = new SearchInDTO();
		itemParam.setKeyword(inDTO.getKeyword());
		itemParam.setAttributes(inDTO.getAttributes());
		itemParam.setCid(inDTO.getCid());
		itemParam.setAreaCode(inDTO.getAreaCode());
		itemParam.setBrandIds(inDTO.getBrandIds());
		itemParam.setOrderSort(inDTO.getOrderSort());
		itemParam.setBuyerId(inDTO.getBuyerId());
		itemParam.setAddSources(inDTO.getAddSources());
		itemParam.setHouseType(inDTO.getHouseType());
		itemParam.setMinPrice(inDTO.getMinPrice());
		itemParam.setMaxPrice(inDTO.getMaxPrice());
		if(StringUtils.isBlank(inDTO.getAreaCode())){
			itemParam.setAreaCode("0");//TODO SKU价格都是全国的
		}else{
			itemParam.setAreaCode(inDTO.getAreaCode());//TODO SKU价格区域价
		}
		itemParam.setPlatformId(inDTO.getPlatformId());//平台id
		SearchOutDTO itemOutDTO = this.searchItemExportService.searchItem(itemParam, inDTO.getPager());
		result.setAttributes(this.getSearchAttrList(itemOutDTO.getAttributes()));
		result.setBrands(this.getBrandList(itemOutDTO.getBrands()));
		result.setCategories(this.getCatList(itemOutDTO.getCategories()));
		result.setItemSkus(this.getItemSkus(itemOutDTO,inDTO));
		result.setSeniorAttributes(this.getSearchAttrList(itemOutDTO.getSeniorAttributes()));
		logger.info("===========结束搜索商品==============");
		return result;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015-3-20
	 * @param itemDTOs
	 * @return
	 * @author:wangcs
	 */
	private DataGrid<SearchItemSku> getItemSkus(SearchOutDTO itemOutDTO,SearchItemSkuInDTO inDTO) {
		DataGrid<SearchItemSku> dg = new DataGrid<SearchItemSku>();
		dg.setTotal(itemOutDTO.getItemDTOs().getTotal());
		List<SearchItemSku> sItems = new ArrayList<SearchItemSku>();
		SearchItemSku si = null;
		for (ItemSkuDTO sku : itemOutDTO.getItemDTOs().getRows()) {
				si = new SearchItemSku();
				si.setAd(sku.getAd());
				si.setBrand(sku.getBrand());
				si.setBrandName(sku.getBrandName());
				si.setCid(sku.getCid());
				si.setDescribeUrl(sku.getDescribeUrl());
				si.setHasPrice(sku.getHasPrice());
				si.setItemId(sku.getItemId());
				si.setItemName(sku.getItemName());
				si.setItemStatus(sku.getItemStatus());
				si.setPicUrl(sku.getPicUrl());
				si.setProductId(sku.getProductId());
				si.setSellerId(sku.getSellerId());
				si.setShopCid(sku.getShopCid());
				si.setShopId(sku.getShopId());
				si.setSkuId(sku.getSkuId());
				si.setSkuInventory(sku.getSkuInventory());
				si.setSkuPrice(sku.getSkuPrice());
				si.setSkuInquiryPirce(sku.getSkuInquiryPirce());
				si.setAttributes(this.getSearchAttrList(sku.getAttributes()));
				si.setSkuScope(sku.getSkuScope());
				sItems.add(si);
		}
		dg.setRows(sItems);
		return dg;
	}

}
