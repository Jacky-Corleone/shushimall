package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.FavouriteCountDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ItemFavouriteExportService {
	
	/**
	 * 
	 * <p>Discription: 收藏商品</p>
	 * Created on 2015年3月13日
	 * @param favouriteDTO
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> add(ItemFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:查询个人收藏商品</p>
	 * Created on 2015年3月13日
	 * @param pager
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<ItemFavouriteDTO> datagrid(Pager pager, ItemFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription: 删除个人收藏商品 </p>
	 * Created on 2015年3月13日
	 * @param id
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> del(String id,String uid);
	
	/**
	 * 
	 * <p>Discription:[查询店铺收藏信息  收藏数量排序]</p>
	 * Created on 2015-4-20
	 * @param favourite
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<FavouriteCountDTO>> queryFavouriteCount(Long shopId,Pager pager);
}
