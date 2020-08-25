package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>Description: [商品属性服务接口]</p>
 * Created on 2015-5-28
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemAttributeExportService {

	/**
	 * 
	 * <p>Discription:[添加商品属性]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttr> addItemAttribute(ItemAttr itemAttr);
	
	/**
	 * 
	 * <p>Discription:[删除商品属性]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttr> deleteItemAttribute(ItemAttr itemAttr);
	
	/**
	 * 
	 * <p>Discription:[修改商品属性]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttr> modifyItemAttribute(ItemAttr itemAttr);
	
	
	/**
	 * 
	 * <p>Discription:[添加商品属性值]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttrValue> addItemAttrValue(ItemAttrValue itemAttrValue);
	
	/**
	 * 
	 * <p>Discription:[删除商品属性值]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttrValue> deleteItemAttrValue(ItemAttrValue itemAttrValue);
	
	/**
	 * 
	 * <p>Discription:[修改商品属性值]</p>
	 * Created on 2015-5-28
	 * @param itemAttr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttrValue> modifyItemAttrValue(ItemAttrValue itemAttrValue);
	/**
	 * 
	 * <p>Discription:[复制属性 属性值 然后返回]</p>
	 * Created on 2015-6-8
	 * @param inDTO sellerId 卖家ID 必填 ;shopId 商家ID 必填 ;
		cid 平台类目ID 必填;
		attrType 属性类型:1:销售属性;2:非销售属性 必填;
	 * @param operator 1 商家 2 平台
	 * @return
	 * @author:[yuht]
	 */
	public ExecuteResult<List<ItemAttr>> addItemAttrValueBack(CatAttrSellerDTO inDTO,int operator);
	
}
