package com.camelot.tradecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.domain.TradeOrders;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;

public interface TradeOrdersDAO extends BaseDAO<TradeOrders> {

	/**
	 * 
	 * <p>Discription:[获取订单号]</p>
	 * Created on 2015-3-10
	 * @param flag 如果是特殊用户，订单编号以flag开头
	 * @return
	 * @author:wangcs
	 */
	String getOrderId(@Param("flag") String flag);
	
	/**
	 * 
	 * <p>Discription:[查询所有非父级订单,parent_order_id!=0]</p>
	 * Created on 2015-3-12
	 * @param inDTO
	 * @param pager
	 * @return
	 * @author:wangcs
	 */
	List<TradeOrdersDTO> queryTradeOrders(@Param("entity") TradeOrdersQueryInDTO inDTO, @Param("page") Pager<TradeOrdersQueryInDTO> pager);

	/**
	 * 
	 * <p>Discription:[查询订单数量]</p>
	 * Created on 2015-3-12
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	Long queryTradeOrdersCount(@Param("entity") TradeOrdersQueryInDTO inDTO);

	/**
	 * 
	 * <p>Discription:[查询所有]</p>
	 * Created on 2015-3-24
	 * @param param
	 * @param object
	 * @return
	 * @author:wangcs
	 */
//	List<TradeOrdersDTO> queryAllTradeOrders(@Param("entity") TradeOrdersQueryInDTO param);

	/**
	 * 
	 * <p>Discription:[根据订单号查询订单]</p>
	 * Created on 2015-3-25
	 * @param orderId
	 * @return
	 * @author:wangcs
	 */
	TradeOrdersDTO queryTradeOrderById(@Param("orderId") String orderId);

	/**
	 * 
	 * <p>Discription:[修改子订单价格]</p>
	 * Created on 2015-4-14
	 * @param orderDTO
	 * @author:wangcs
	 */
	void updateSubOrderPrice(TradeOrdersDTO orderDTO);
	
	/**
	 * 
	 * <p>Discription:[延迟收货7天]</p>
	 * Created on 2015-4-16
	 * @param tradeOrdersDTO
	 * @author:yuht
	 */
	void updateDelayDelivery(TradeOrdersDTO tradeOrdersDTO);

	/**
	 * 
	 * <p>Discription:[延期付款定时任务]</p>
	 * Created on 2015-4-16
	 * @param tradeOrdersDTO
	 * @author:zhouzj
	 */
	List<TradeOrdersDTO> queryTradeOrdersForDelayPay(@Param("args") String args);
}
