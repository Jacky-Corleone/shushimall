package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;


/**
 * 
 * <p>Description: [店铺品牌]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopBrandExportService {
	/**
	 * 
	 * <p>Discription:[根据店铺ID  状态查询 店铺品牌]</p>
	 * Created on 2015-3-9
	 * @param shopBrandDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<ShopBrandDTO>> queryShopBrandList(Long shopId,Integer status);


	/**
	 * 
	 * <p>Discription:[添加店铺品牌]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addShopBrand(ShopBrandDTO shopBrandDTO);
	
	/**
	 * 
	 * <p>Discription:[查询店铺品牌店铺运行状态为开通]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopBrandDTO>> queryShopBrand(ShopBrandDTO shopBrandDTO,Pager page);
	/**
	 * 
	 * <p>Discription:[查询店铺品牌所有]</p>
	 * Created on 2015-3-11
	 * @param shopCategoryDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopBrandDTO>> queryShopBrandAll(ShopBrandDTO shopBrandDTO,Pager page);
	/**
	 * 
	 * <p>Discription:[店铺品牌状态修改]</p>
	 * Created on 2015-3-11
	 * @param Id
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopBrandStatus(ShopBrandDTO shopBrandDTO);
	/**
	 * 
	 * <p>Discription:[根据id查找品牌所有信息]</p>
	 * Created on 2015-7-29
	 * @param id
	 * @return
	 * @author:jiangpeng
	 */
	public List<ShopBrandDTO> selectBrandIdById(Long id);
	/**
	 * 
	 * <p>Discription:[根据BrandId修改曾经被驳回的品牌的status的值为-1]</p>
	 * Created on 2015-7-29
	 * @param brandId
	 * @return
	 * @author:jiangpeng
	 */
	public void updateStatusByIdAndBrandId(Long brandId);
	
	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的类目ID修改品牌的status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param cid
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> updateStatusByShopIdAndCid(Long shopId,Long cid);
	
	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的品牌ID修改品牌的status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param brandId
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> updateStatusByShopIdAndBrandId(Long shopId,Long brandId);

}
