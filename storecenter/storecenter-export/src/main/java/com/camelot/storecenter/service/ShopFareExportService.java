package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFareDTO;

/** 
 * <p>Description: [店铺运费的接口]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopFareExportService {
	/**
	 * <p>Discription:[根据运送地址和店铺id查询店铺运费信息]</p>
	 * Created on 2015年3月25日
	 * @param dto
	 * @return
	 * @author:[运费]
	 */
	public ExecuteResult<ShopFareDTO> queryShopFareByFareRegion(ShopFareDTO dto);
	/**
	 * <p>Discription:[查询输入的地址是否已存在，若存在返回已存在的地址]</p>
	 * Created on 2015年3月23日
	 * @param sellerId
	 * @param shopId
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> queryBysellerIdAndShopId(ShopFareDTO dto);
	/**
	 * <p>Discription:[根据id复制模板]</p>
	 * Created on 2015年3月18日
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> copyShopFare(Long id);
    /**
     * <p>Discription:[根据条件查询店铺费用列表]</p>
     * Created on 2015年3月17日
     * @param dto
     * @param page
     * @return
     * @author:[杨芳]
     */
	public DataGrid<ShopFareDTO> queryShopFareList(ShopFareDTO dto,Pager<ShopFareDTO> page);
	
	/**
	 * <p>Discription:[根据id修改店铺费用的信息]</p>
	 * Created on 2015年3月17日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> modifyShopFareById(ShopFareDTO dto);
	
	/**
	 * <p>Discription:[根据id删除店铺费用]</p>
	 * Created on 2015年3月17日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> deleteShopFareById(Long id);
	
	/**
	 * <p>Discription:[添加店铺费用]</p>
	 * Created on 2015年3月17日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> addShopFare(ShopFareDTO dto);
}
