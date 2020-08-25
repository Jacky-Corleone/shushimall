package com.camelot.goodscenter.dao;

import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemAttributeValueDAO extends BaseDAO<ItemAttrValue> {
	
	/**
	 * 
	 * <p>Description: [根据商品属性删除商品的属性值]</p>
	 * Created on 2015年8月14日
	 * @param id
	 * @return
	 * @author:[宋文斌]
	 */
	public void deleteByAttrId(Long attr_id);
}
