package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemAttrValueItemDAO extends BaseDAO<ItemAttrValueItemDTO>{

	/**
	 * 
	 * <p>Discription:[根据商品ID修改状态为2删除]</p>
	 * Created on 2015-5-27
	 * @param itemId
	 * @author:[yuht]
	 */
	void updatestatus(Long itemId);
	/**
	 * 
	 * <p>Discription:[根据属性值ID修改状态为2删除]</p>
	 * Created on 2015-5-27
	 * @param valueId
	 * @author:[yuht]
	 */
	void updatestatusbyValueId(Long valueId);
	
	/**
	 * 
	 * <p>Discription:[查询属性]</p>
	 * Created on 2015-6-2
	 * @param itemAttrValueItemDTO
	 * @return
	 * @author:[yuht]
	 */
	List<ItemAttr> queryAttrList(@Param("entity")ItemAttrValueItemDTO itemAttrValueItemDTO);
	/**
	 * 
	 * <p>Discription:[查询属性值]</p>
	 * Created on 2015-6-2
	 * @param itemAttrValueItemDTO
	 * @return
	 * @author:[yuht]
	 */
	List<ItemAttrValue> queryValueList(@Param("entity")ItemAttrValueItemDTO itemAttrValueItemDTO);

}
