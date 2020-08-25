package com.camelot.storecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;

/** 
 * <p>Description: 店铺收藏</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopFavouriteDAO {

	/**
	 * 
	 * <p>Discription:收藏</p>
	 * Created on 2015年3月14日
	 * @param favourite
	 * @author:[Goma 郭茂茂]
	 */
	public void add(ShopFavouriteDTO favourite);
	
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
	public List<ShopFavouriteDTO> queryPage(@Param("page")Pager pager,@Param("entity")ShopFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:收藏查询总数</p>
	 * Created on 2015年3月14日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer queryPageCount(@Param("entity")ShopFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:删除</p>
	 * Created on 2015年3月14日
	 * @param id
	 * @param uid
	 * @author:[Goma 郭茂茂]
	 */
	public void del(@Param("id")String id,@Param("uid")String uid);
	
	public List<ShopFavouriteDTO> list(@Param("entity")ShopFavouriteDTO favourite);
}
