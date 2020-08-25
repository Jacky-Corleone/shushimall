package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemSalesVolumeDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemSalesVolumeDAO extends BaseDAO<ItemSalesVolumeDTO> {

	/**
	 * 
	 * <p>Discription:[更新销量]</p>
	 * Created on 2015-4-15
	 * @param inList
	 * @author:wangcs
	 */
	void addList(@Param("inList") List<ItemSalesVolumeDTO> inList);

	/**
	 * 
	 * <p>Discription:[删除所有]</p>
	 * Created on 2015-4-15
	 * @author:wangcs
	 */
	void deleteAll();

}
