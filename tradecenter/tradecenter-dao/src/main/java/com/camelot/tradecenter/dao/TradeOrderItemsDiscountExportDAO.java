package com.camelot.tradecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;

public interface TradeOrderItemsDiscountExportDAO extends BaseDAO<TradeOrderItemsDiscountDTO>{
	
	public TradeOrderItemsDiscountDTO queryByOrderItemsId(@Param("orderItemsId") Integer orderItemsId);

}
