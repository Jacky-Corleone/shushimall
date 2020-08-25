package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;



/**
 * 
 * <p>Description: [店铺经营类目]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCategoryExportService {
	/**
	 * 
	 * <p>Discription:[根据店铺ID查询店铺经营类目]</p>
	 * Created on 2015-3-9
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<ShopCategoryDTO>> queryShopCategoryList(Long shopId,Integer status);
	
	/**
	 * 
	 * <p>Discription:[添加平台经营类目]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addShopCategory(ShopCategoryDTO shopCategoryDTO);
	
	/**
	 * 
	 * <p>Discription:[查询店铺类目  店铺运行状态未开通]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopCategoryDTO>> queryShopCategory(ShopCategoryDTO shopCategoryDTO,Pager page);
	/**
	 * 
	 * <p>Discription:[查询店铺类目所有]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopCategoryDTO>> queryShopCategoryAll(ShopCategoryDTO shopCategoryDTO,Pager page);
	/**
	 * 
	 * <p>Discription:[店铺经营类目状态修改]</p>
	 * Created on 2015-3-11
	 * @param Id
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopCategoryStatus(ShopCategoryDTO shopCategoryDTO);
	/**
	 * 
	 * <p>Discription:[根据id查找类目所有信息]</p>
	 * Created on 2015-7-29
	 * @param Id
	 * @return
	 * @author:jiangpeng
	 */
	public List<ShopCategoryDTO> selectShopIdById(Long Id);
	/**
	 * 
	 * <p>Discription:[根据shopId修改曾经被驳回的类目的status的值为-1]</p>
	 * Created on 2015-7-29
	 * @param shopId
	 * @return
	 * @author:yuht
	 */
	public void updateStatusByIdAndShopId(Long shopId);

	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的类目ID修改status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param cid
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> updateStatusByShopIdAndCid(Long shopId,Long cid);

}
