package com.camelot.mall.home;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月14日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface FavouriteService {

	@SuppressWarnings("rawtypes")
	public DataGrid<JSONObject> shops(Pager pager,ShopFavouriteDTO favourite);
	
	@SuppressWarnings("rawtypes")
	public DataGrid<JSONObject> products(Pager pager,ItemFavouriteDTO favourite,String regionId);
	
	/**
	 * 
	 * <p>Discription:[查询收藏商品 促销,满减,包邮信息]</p>
	 * Created on 2015年12月16日
	 * @param pager
	 * @param favourite
	 * @param regionId
	 * @return
	 * @author: 尹归晋
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,Object> productsMarketing(Pager pager,ItemFavouriteDTO favourite,String regionId);
}