package com.camelot.tradecenter.dao;

import java.util.List;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.SalesVolumeDTO;

public interface SalesVolumeDAO extends BaseDAO<SalesVolumeDTO> {

	/**
	 * 
	 * <p>Discription:[查询SKU销量]</p>
	 * Created on 2015-4-15
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	List<SalesVolumeDTO> querySkuSalesVolume(SalesVolumeDTO inDTO);

	/**
	 * 
	 * <p>Discription:[查询店铺销量]</p>
	 * Created on 2015-4-15
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	List<SalesVolumeDTO> queryShopSalesVolume(SalesVolumeDTO inDTO);

}
