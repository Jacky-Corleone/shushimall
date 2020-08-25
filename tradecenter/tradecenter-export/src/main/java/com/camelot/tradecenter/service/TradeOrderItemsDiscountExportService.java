package com.camelot.tradecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;

/**
 * 订单商品优惠对外暴露的接口
 * 
 * @author 王东晓
 *
 */
public interface TradeOrderItemsDiscountExportService {
	
	/**
	 * 
	 * <p>Discription:[插入订单商品的优惠信息]</p>
	 * Created on 2016-01-06
	 * @param tradeOrdersDTO
	 * @return
	 * @author:[王东晓]
	 */
    public ExecuteResult<String> createOrderItemsDiscount(TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO);
    
    /**
	 * 
	 * <p>Discription:[根据订单商品表ID查询订单商品的优惠信息]</p>
	 * Created on 2016-01-06
	 * @param tradeOrdersDTO
	 * @return
	 * @author:[王东晓]
	 */
    public ExecuteResult<TradeOrderItemsDiscountDTO> queryOrderItemsDiscountById(Integer orderItemsId);
    
    /**
	 * 
	 * <p>Discription:[根据订单商品查询订单商品的优惠信息]</p>
	 * Created on 2016-01-06
	 * @param tradeOrdersDTO
	 * @return
	 * @author:[王东晓]
	 */
    public ExecuteResult<DataGrid<TradeOrderItemsDiscountDTO>> queryOrderItemsDiscountByCondition(TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO,Pager<TradeOrderItemsDiscountDTO> pager);
}
