package com.camelot.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.mall.service.FavouriteService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.ShopFavouriteExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月14日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class FavouriteServiceImpl implements FavouriteService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ShopFavouriteExportService shopFavouriteService;
	@Resource
	private ItemFavouriteExportService itemFavouriteService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private UserExportService userService;
	
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年3月14日
	 * @param pager
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<JSONObject> shops(Pager pager, ShopFavouriteDTO favourite) {
		DataGrid<JSONObject> dg = new DataGrid<JSONObject>();
		DataGrid<ShopFavouriteDTO> dgShops = this.shopFavouriteService.datagrid(pager, favourite);
		
		LOG.debug("DUBBO 查询收藏店铺："+JSON.toJSONString(dgShops));
		
		List<JSONObject> shops = new ArrayList<JSONObject>();
		
		for( ShopFavouriteDTO sf: dgShops.getRows() ){
			
			Integer sellerId = sf.getSellerId();
			UserDTO user = this.userService.queryUserById(sellerId);
			sf.setSellerName(user.getUname());
			
			JSONObject shop = JSON.parseObject(JSON.toJSONString(sf));
			
			ItemQueryInDTO item = new ItemQueryInDTO();
			item.setShopIds(new Long[]{Long.valueOf(sf.getShopId())});
			item.setItemStatus(4);
			
			Pager page = new Pager(1, 5);
			DataGrid<ItemQueryOutDTO> items = this.itemService.queryItemList(item, page);
			shop.put("items", items.getRows());
			
			shops.add(shop);
		}
		
		dg.setTotal(dgShops.getTotal());
		dg.setRows(shops);
		
		return dg;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<JSONObject> products(Pager pager, ItemFavouriteDTO favourite,String regionId) {
		DataGrid<JSONObject> rlt = new DataGrid<JSONObject>();
		
		DataGrid<ItemFavouriteDTO> dg = this.itemFavouriteService.datagrid(pager, favourite);
		List<JSONObject> rows = new ArrayList<JSONObject>();
		ItemShopCartDTO skuDto = new ItemShopCartDTO();
		for( ItemFavouriteDTO dto: dg.getRows() ){
			
			JSONObject row = JSONObject.parseObject(JSON.toJSONString(dto));
			
			skuDto.setAreaCode(regionId);
			skuDto.setShopId(Long.valueOf(dto.getShopId()));
			skuDto.setItemId(Long.valueOf(dto.getItemId()));
			skuDto.setSkuId(Long.valueOf(dto.getSkuId()));
			
			ExecuteResult<ItemShopCartDTO> er = this.itemService.getSkuShopCart(skuDto);
			ItemShopCartDTO iscd = er.getResult();
			
			if( er.isSuccess() && iscd != null ){
				row.put("picUrl", iscd.getSkuPicUrl());
				row.put("itemName", iscd.getItemName());
				row.put("price", iscd.getSkuPrice());
			}
			
			rows.add(row);
		}
		
		rlt.setTotal(dg.getTotal());
		rlt.setRows(rows);
		
		return rlt;
	}

}
