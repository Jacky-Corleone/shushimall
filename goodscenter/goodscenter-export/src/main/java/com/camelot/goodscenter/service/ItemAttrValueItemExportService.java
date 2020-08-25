package com.camelot.goodscenter.service;


import java.util.List;

import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * 
 * <p>Description: [属性 属性值 和商品关系]</p>
 * Created on 2015-5-26
 * @author yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemAttrValueItemExportService {

	/**
	 * 
	 * <p>Discription:[属性 属性值 和商品关系添加]</p>
	 * Created on 2015-5-26
	 * @param itemAttrValueItemList
	 * @return
	 * @author:[yuht]
	 */
	public ExecuteResult<String> addItemAttrValueItem(List<ItemAttrValueItemDTO> itemAttrValueItemList);
	
	
	/**
	 * 
	 * <p>Discription:[属性 属性值 和商品关系修改]</p>
	 * Created on 2015-5-26
	 * @param itemAttrValueItemList
	 * @return
	 * @author:[yuht]
	 */
	public ExecuteResult<String> modifyItemAttrValueItem(List<ItemAttrValueItemDTO> itemAttrValueItemList);
	
	
	/**
	 * 
	 * <p>Discription:[属性 属性值 和商品关系删除]</p>
	 * Created on 2015-5-26
	 * @param valueId valueId属性值ID 
	 * @return
	 * @author:[yuht]
	 */
	public ExecuteResult<String> deleteItemAttrValueItem(Long... valueId);
	
	
	/**
	 * 
	 * <p>Discription:[属性 属性值 和商品关系查询]</p>
	 * Created on 2015-5-26
	 * @param itemAttrValueItemDTO
	 * @return
	 * @author:[yuht]
	 */
	public ExecuteResult<List<ItemAttr>>  queryItemAttrValueItem(ItemAttrValueItemDTO itemAttrValueItemDTO);
	
}
