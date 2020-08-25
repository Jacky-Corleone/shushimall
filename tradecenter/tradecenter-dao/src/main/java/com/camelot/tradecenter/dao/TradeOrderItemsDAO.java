package com.camelot.tradecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.domain.TradeOrderItems;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

public interface TradeOrderItemsDAO extends BaseDAO<TradeOrderItems>{

	/**
	 * 
	 * <p>Discription:[批量添加订单行项目]</p>
	 * Created on 2015-3-10
	 * @param orderId
	 * @param items
	 * @author:wangcs
	 */
	void addItemsBatch(@Param("orderId") String orderId, @Param("items") List<TradeOrderItemsDTO> items);

	/**
	 * 
	 * <p>Discription:[根据订单ID查询订单行项目]</p>
	 * Created on 2015-3-12
	 * @param orderId
	 * @return
	 * @author:wangcs
	 */
	List<TradeOrderItemsDTO> queryItemsByOrderId(@Param("orderId") String orderId);
	
	/**
	 * 
	 * <p>Discription:[根据协议单号查询购买数量]</p>
	 * Created on 2015-3-12
	 * @param orderId
	 * @return
	 * @author:wangcs
	 */
	
	List<TradeOrderItemsDTO> countNumber(@Param("contractNo") String contractNo);
	
	/**
	 * 
	 * <p>Discription:[根据协议单号查询购买总]</p>
	 * Created on 2015-3-12
	 * @param orderId
	 * @return
	 * @author:wangcs
	 */
	
	List<TradeOrderItemsDTO> countCost(@Param("contractNo") String contractNo);


	/**
	 * 
	 * <p>Discription:[删除订单，用于数据回滚]</p>
	 * Created on 2015-4-20
	 * @param orders
	 * @author:wangcs
	 */
	void deleteOrders(@Param("orders") List<TradeOrdersDTO> orders);

}
