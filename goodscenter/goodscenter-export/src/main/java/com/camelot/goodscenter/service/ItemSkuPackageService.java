package com.camelot.goodscenter.service;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>套装商品的sku与被包含商品sku</p>
 * Created on 2016年2月17日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface ItemSkuPackageService {
	
	/**
	 * <p>插入一件套装商品 与 子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param packageSkuId 套装商品skuId
	 * @param subSkuIds 子skuId
	 * @author: 顾雨
	 */
	void add(Long packageSkuId, List<Long> subSkuIds);
	
	/**
	 * <p>添加套装商品的关联表</p>
	 * Created on 2016年2月23日
	 * @param itemSkuPackage
	 * @return
	 * @author: 王鹏
	 */
	void addItemSkuPackage(ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * <p>重载{@link #add(Long, List)}</p>
	 * Created on 2016年2月17日
	 * @param packageSkuId
	 * @param subSkuIds
	 * @author: 顾雨
	 */
	<T> void add(Long packageSkuId, T... subSkuIds);
	
	/**
	 * <p>删除套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @author: 顾雨
	 */
	void delete(ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * <p>检索套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @return
	 * @author: 顾雨
	 */
	List<ItemSkuPackageDTO> getPackages(ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * 
	 * <p>Discription:[获取套装商品的单品信息]</p>
	 * Created on 2016年2月26日
	 * @param packageItemId 套装商品Id（必须）
	 * @param packageSkuId 套装SkuId（必须）
	 * @param areaCode 区域编码（必须）
	 * @param qty 套装商品数量（必须）
	 * @param addSource 1：普通商品 2：平台上传 3：套装商品 4：基础服务商品 5：增值服务商品 6：辅助材料商品
	 * @param valueAddedMap 已选增值服务（key=增值服务的itemId，value=增值服务的skuId）（可选）
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<List<ItemDTO>> getPackageSkus(Long packageItemId, Long packageSkuId, String areaCode, Integer qty, Integer addSource, Map<Long,Long> subSkuMap);

	/**
	 * 更新组合商品关联表
	 * @param itemSkuPackageDTO
	 * @return
	 */
	public ExecuteResult<String> updateItemSkuPackage(ItemSkuPackageDTO itemSkuPackageDTO);
}
