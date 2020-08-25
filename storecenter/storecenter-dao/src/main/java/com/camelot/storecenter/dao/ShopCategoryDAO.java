package com.camelot.storecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;

/** 
 * <p>Description: [店铺经营类目]</p>
 * Created on 2015年3月5日
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopCategoryDAO extends BaseDAO<ShopCategoryDTO> {

	/**
	 * 
	 * <p>Discription:[根据店铺id查询店铺经营类目]</p>
	 * Created on 2015-3-10
	 * @param shopId
	 * @return
	 * @author:yuht
	 */
	List<ShopCategoryDTO> selectByShopId(@Param("shopId")Long shopId,@Param("status")Integer status);
	/**
	 * 
	 * <p>Discription:[根据ID修改店铺经营类目状态]</p>
	 * Created on 2015-3-11
	 * @param id
	 * @return
	 * @author:yuht
	 */
	Integer modifyShopCategoryStatus(ShopCategoryDTO shopCategoryDTO);
	/**
	 * 
	 * <p>Discription:[查询店铺类目 所有]</p>
	 * Created on 2015-3-23
	 * @param shopCategoryDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	List<ShopCategoryDTO> selectListByConditionAll(@Param("entity")ShopCategoryDTO shopCategoryDTO,@Param("page") Pager page);
	
	/**
	 * 
	 * <p>Discription:[查询店铺类目条数  所有]</p>
	 * Created on 2015-3-23
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	Long selectCountByConditionAll(@Param("entity")ShopCategoryDTO shopCategoryDTO);
	/**
	 * 
	 * <p>Discription:[根据店铺ID删除类目]</p>
	 * Created on 2015-3-25
	 * @param id
	 * @author:yuht
	 */
	void deleteByShopId(Long id);
	/**
	 * 
	 * <p>Discription:[根据id查找类目所有信息]</p>
	 * Created on 2015-7-29
	 * @param Id
	 * @return
	 * @author:jiangpeng
	 */
	List<ShopCategoryDTO> selectShopIdById(Long Id);
	/**
	 * 
	 * <p>Discription:[根据shopId修改曾经被驳回的类目的status的值为-1]</p>
	 * Created on 2015-7-29
	 * @param shopId
	 * @return
	 * @author:yuht
	 */
	void updateStatusByIdAndShopId(Long shopId);
	
	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的类目ID修改status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param cid
	 * @author:[宋文斌]
	 */
	void updateStatusByShopIdAndCid(@Param("shopId") Long shopId,@Param("cid") Long cid);
	
}
