package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>套装商品的sku与被包含商品sku关系表DAO</p>
 * <p>功能：增删改查</p>
 * Created on 2016年2月16日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface ItemSkuPackageDAO extends BaseDAO<ItemSkuPackageDTO>{
	/**
	 * 插入一件套装商品 与 子sku的关联
	 * <p></p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 套装商品与子商品关联关系DTO
	 * @author: 顾雨
	 */
	void add(ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * 添加套装商品
	 * @param itemSkuPackage
	 */
	void addItemSkuPackage(ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * <p>删除套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @author: 顾雨
	 */
	void delete(@Param("itemSkuPackage")ItemSkuPackageDTO itemSkuPackage);
	
	/**
	 * <p>检索套装sku和其子sku的关联</p>
	 * Created on 2016年2月17日
	 * @param itemSkuPackage 删除条件
	 * @return
	 * @author: 顾雨
	 */
	List<ItemSkuPackageDTO> getPackages(@Param("itemSkuPackage")ItemSkuPackageDTO itemSkuPackage);
	
}
