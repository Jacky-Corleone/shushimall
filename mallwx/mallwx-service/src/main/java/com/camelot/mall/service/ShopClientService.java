package com.camelot.mall.service;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.mall.dto.ShopInfoInRedisDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;

public interface ShopClientService {
	/**
	 * 组装一、二、三级类目名称列表
	 * @param result
	 * @return
	 */
	public Map<String, String> buildCategoryNameList(List<ItemCatCascadeDTO> result);
	
	/**
	 * 构建店铺已有的经营类目id字符串
	 * @param scids
	 * @return
	 */
	public String buildExistsCids(Long[] scids);
	
	/**
	 * 从redis取出的对象中获取品牌id数组
	 * @param shopInfoRedis
	 * @return
	 */
	public Long[] getBrandIds(ShopInfoInRedisDTO shopInfoRedis);
	
	//从redis取出的对象中获取类目id数组
	public Long[] getCids(ShopInfoInRedisDTO shopInfoRedis);
	
	/**
	 * 组装店铺的平台类目id数组
	 */
	public Long[] buildShopCategoryIds(List<ShopCategoryDTO> list);
	
	/**
	 * 组装品牌id
	 */
	public Long[] buildBrandIds(ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList);
	
	/**
	 * 批量保存类目数据
	 * @param addCategoryCids
	 * @param userId
	 * @param shopId
	 */
	public void addShopCategory(String addCategoryCids, Long userId, Long shopId);
	
	/**
	 * 批量保存品牌数据
	 * @param addBrandIds
	 * @param userId
	 * @param shopId
	 */
	public void addShopBrand(String addBrandIds, Long userId, Long shopId);
	
	/**
	 * 新增店铺经营的类目信息
	 * @param addCategoryCids 类目id集合字符串
	 * @param userId 当前登录用户id
	 * @param shopId 当前登录卖家的店铺id
	 */
	public List<ShopCategoryDTO> buildShopCategoryList(String addCategoryCids, Long userId, Long shopId);
	
	/**
	 * 新增店铺经营的品牌信息
	 * @param addBrandIds 品牌ID集合字符串，格式为 ：cid:brandId,cid:brandId
	 * @param userId 当前登录用户id
	 * @param shopId 当前登录卖家的店铺id
	 */
	public List<ShopBrandDTO> buildShopBrandList(String addBrandIds, Long userId, Long shopId);
}
