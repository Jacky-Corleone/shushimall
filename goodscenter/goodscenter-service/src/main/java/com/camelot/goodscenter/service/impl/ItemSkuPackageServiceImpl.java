package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dao.ItemSkuDAO;
import com.camelot.goodscenter.dao.ItemSkuPackageDAO;
import com.camelot.goodscenter.domain.ItemSku;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>套装商品的sku与被包含商品sku</p>
 * Created on 2016年2月17日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Service("itemSkuPackageService")
public class ItemSkuPackageServiceImpl implements ItemSkuPackageService {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemSkuPackageServiceImpl.class);
	
	@Resource
	private ItemSkuDAO itemSkuDAO;
	
	@Resource
	private ItemSkuPackageDAO itemSkuPackageDAO;
	
	@Resource
	private ItemExportService itemExportService;
	
	/**
	 * <p>插入一件套装商品 与 子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param packageSkuId 套装商品skuId
	 * @param subSkuIds 子skuId
	 * @author: 顾雨
	 */
	@Override
	public void add(Long packageSkuId, List<Long> subSkuIds) {
		ItemSku packageItemSku = itemSkuDAO.queryById(packageSkuId);
		ItemSkuPackageDTO packageToAdd =
				new ItemSkuPackageDTO(packageItemSku.getItemId(), packageSkuId, subSkuIds);
		itemSkuPackageDAO.add(packageToAdd);
	}

	/**
	 * <p>重载{@link #add(Long, List)}</p>
	 * Created on 2016年2月17日
	 * @param packageSkuId 套装商品skuId
	 * @param subSkuIds 子skuId 只能为String或long类型
	 * @author: 顾雨
	 */
	@Override
	public <T> void add(Long packageSkuId, T... subSkuIds) {
		if (String[].class.isInstance(subSkuIds)) {
			List<Long> subSkuIdsOfLong = new ArrayList<Long>();
			for (T skuId : subSkuIds) {
				subSkuIdsOfLong.add(Long.valueOf((String) skuId));
			}
			add(packageSkuId, subSkuIdsOfLong);
		} else if (Long[].class.isInstance(subSkuIds)) {
			add(packageSkuId, Arrays.asList((Long[])subSkuIds));
		} else {
			throw new RuntimeException("add failed, parameter subSkuIds can only be String[] or Long[]");
		}
	}
	
	/**
	 * <p>删除套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @author: 顾雨
	 */
	@Override
	public void delete(ItemSkuPackageDTO itemSkuPackage) {
		itemSkuPackageDAO.delete(itemSkuPackage);
	}

	/**
	 * <p>检索套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @return
	 * @author: 顾雨
	 */
	@Override
	public List<ItemSkuPackageDTO> getPackages(ItemSkuPackageDTO itemSkuPackage) {
		return itemSkuPackageDAO.getPackages(itemSkuPackage);
	}
	/**
	 * <p>添加套装商品的关联表</p>
	 * Created on 2016年2月23日
	 * @param itemSkuPackage
	 * @return
	 * @author: 王鹏
	 */
	@Override
	public void addItemSkuPackage(ItemSkuPackageDTO itemSkuPackage) {
		itemSkuPackageDAO.addItemSkuPackage(itemSkuPackage);
	}

	@Override
	public ExecuteResult<List<ItemDTO>> getPackageSkus(Long packageItemId, Long packageSkuId, String areaCode, Integer qty, Integer addSource, Map<Long,Long> subSkuMap) {
		logger.info("\n 方法[{}]，入参：[{}][{}][{}][{}][{}]", "ItemSkuPackageServiceImpl-getPackageSkus", packageItemId, packageSkuId, areaCode, qty, JSON.toJSONString(subSkuMap));
		ExecuteResult<List<ItemDTO>> result = new ExecuteResult<List<ItemDTO>>();
		try{
			if(packageItemId == null){
				result.addErrorMessage("套装商品ID为空");
	    		return result;
	    	}
			if(packageSkuId == null){
				result.addErrorMessage("套装商品SkuId为空");
				return result;
			}
			if(addSource == null){
				result.addErrorMessage("商品来源为空");
				return result;
			}
			// 单品集合
			List<ItemDTO> itemDTOs = new ArrayList<ItemDTO>();
			// 单品
			if(ItemAddSourceEnum.NORMAL.getCode() == addSource){
				// 查询组成该套装的单品信息
				ItemSkuPackageDTO itemSkuPackageDTO = new ItemSkuPackageDTO();
				itemSkuPackageDTO.setPackageItemId(packageItemId);
				itemSkuPackageDTO.setPackageSkuId(packageSkuId);
				itemSkuPackageDTO.setAddSource(addSource);
				List<ItemSkuPackageDTO> itemSkuPackageDTOs = this.getPackages(itemSkuPackageDTO);
				for (ItemSkuPackageDTO dto : itemSkuPackageDTOs) {
					// 当前单品的skuId和用户选择的skuId相同
//					if (subSkuMap != null && subSkuMap.get(dto.getSubItemId()) != null
//							&& subSkuMap.get(dto.getSubItemId()).equals(dto.getSubSkuId())) {
//					}
					Long subItemId = dto.getSubItemId();
					Long subSkuId = dto.getSubSkuId();
					ExecuteResult<ItemDTO> subItemResult = itemExportService.getItemById(subItemId);
					if (subItemResult.isSuccess() && subItemResult.getResult() != null
							&& subItemResult.getResult().getItemStatus() == 4) {
						ItemDTO subItemDTO = subItemResult.getResult();
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(areaCode);// 省市区编码
						itemShopCartDTO.setSkuId(subSkuId);// skuId
						itemShopCartDTO.setQty(qty);// 数量
						itemShopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(subItemId);// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemExportService.getSkuShopCart(itemShopCartDTO); // 调商品接口查sku
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
							List<ItemShopCartDTO> itemShopCartDTOs = new ArrayList<ItemShopCartDTO>();
							itemShopCartDTOs.add(skuItem.getResult());
							subItemDTO.setItemShopCartDTOs(itemShopCartDTOs);
							itemDTOs.add(subItemDTO);
						}
					}
				}
			} else if(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode() == addSource){ //辅料商品
				
			} else if(ItemAddSourceEnum.BASICSERVICE.getCode() == addSource){ //安装服务
				
			} else if(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode() == addSource){ //增值服务
				
			}
			result.setResult(itemDTOs);
		} catch(Exception e){
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "ItemSkuPackageServiceImpl-getPackageSkus", e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> updateItemSkuPackage(ItemSkuPackageDTO itemSkuPackageDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			this.itemSkuPackageDAO.update(itemSkuPackageDTO);
		}catch(Exception e){
			result.addErrorMessage("执行方法【updateItemSkuPackage】报错：{}"
					+ e.getMessage());
		}
		return result;
	}

}
