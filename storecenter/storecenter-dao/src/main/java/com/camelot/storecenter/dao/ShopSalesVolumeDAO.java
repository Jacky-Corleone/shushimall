package com.camelot.storecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.storecenter.dto.ShopSalesVolumeDTO;

public interface ShopSalesVolumeDAO {

	/**
	 * 
	 * <p>Discription:[删除所有数据]</p>
	 * Created on 2015-4-16
	 * @author:wangcs
	 */
	public void deleteAll();
	
	/**
	 * 
	 * <p>Discription:[添加店铺销量]</p>
	 * Created on 2015-4-16
	 * @param inList
	 * @author:wangcs
	 */
	public void insertSaleVolume(@Param("inList") List<ShopSalesVolumeDTO> inList);
}
