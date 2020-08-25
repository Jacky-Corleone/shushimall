package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;

/** 
 * <p>Description: 店铺收藏</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopFavouriteExportService {
	
	/**
	 * 
	 * <p>Discription:收藏</p>
	 * Created on 2015年3月14日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> add(ShopFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:收藏查询</p>
	 * Created on 2015年3月14日
	 * @param pager
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<ShopFavouriteDTO> datagrid(Pager pager,ShopFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:收藏删除</p>
	 * Created on 2015年3月14日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> del(String id,String uid);
}
