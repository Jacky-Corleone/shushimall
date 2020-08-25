package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: 商品收藏</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ItemFavouriteDAO {

	/**
	 * 
	 * <p>Discription: 收藏</p>
	 * Created on 2015年3月13日
	 * @param favourite
	 * @author:[Goma 郭茂茂]
	 */
	public void add(ItemFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription: 分页查询</p>
	 * Created on 2015年3月13日
	 * @param pager
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@SuppressWarnings("rawtypes")
	public List<ItemFavouriteDTO> queryPage(@Param("page")Pager pager,@Param("entity")ItemFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:分页查询总数量</p>
	 * Created on 2015年3月13日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer queryPageCount(@Param("entity")ItemFavouriteDTO favourite);
	
	/**
	 * 
	 * <p>Discription:删除</p>
	 * Created on 2015年3月13日
	 * @param id
	 * @param uid
	 * @author:[Goma 郭茂茂]
	 */
	public void del(@Param("id")String id,@Param("uid")String uid);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年3月14日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public List<ItemFavouriteDAO> list(@Param("entity")ItemFavouriteDTO favourite);
	/**
	 * 
	 * <p>Discription:[根据店铺ID查询店铺收藏数量汇总信息]</p>
	 * Created on 2015-4-20
	 * @param shopId
	 * @param pager
	 * @return
	 * @author:yuht
	 */
	public List<ItemFavouriteDTO> queryFavouriteCount(@Param("shopId")Long shopId,@Param("pager") Pager pager);
	/**
	 * 
	 * <p>Discription:[根据店铺ID查询店铺收藏数量汇总信息数量]</p>
	 * Created on 2015-4-20
	 * @param shopId
	 * @return
	 * @author:yuht
	 */
	public Long queryCountFavouriteCount(@Param("shopId")Long shopId);
}
