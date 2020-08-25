package com.camelot.tradecenter.service;

import java.util.Date;
import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;

public interface TradeOrderExportService {
	
	/**
	 * 
	 * <p>Discription:[提交订单]</p>
	 * Created on 2015-3-10
	 * @param order
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<TradeOrderCreateDTO> createOrder(TradeOrderCreateDTO dto);
	
	/**
	 * 
	 * <p>Discription:[根据条件分页查询订单]</p>
	 * Created on 2015-3-10
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<DataGrid<TradeOrdersDTO>> queryOrders(TradeOrdersQueryInDTO inDTO,Pager<TradeOrdersQueryInDTO> pager);
	
	/**
	 * 
	 * <p>Discription:[查询订单数量]</p>
	 * Created on 2015-3-12
	 * @param inDTO 
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<Long> queryOrderQty(TradeOrdersQueryInDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[修改订单状态]</p>
	 * Created on 2015-3-23
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<String> modifyOrderStatus(String orderId,Integer orderStatus);
	
	/**
	 * 
	 * <p>Discription:[删除订单]</p>
	 * Created on 2015-3-25
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<String> deleteOrderById(String orderId);
	
	/**
	 * 
	 * <p>Discription:[根据ID 获取订单]</p>
	 * Created on 2015-3-25
	 * @param orderId
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<TradeOrdersDTO> getOrderById(String orderId);
	

	
	
	/**
	 * 
	 * <p>Discription:[修改订单的支付状态]</p>
	 * Created on 2015-3-25
	 * @param inDTO 订单号  支付状态  支付类型必填
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<String> modifyOrderPayStatus(TradeOrdersDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[订单改价]</p>
	 * Created on 2015-4-10
	 * @return
	 * @author:wangcs
	 */
	ExecuteResult<String> modifyOrderPrice(TradeOrdersDTO orderDTO);

	/**
	 * 
	 * <p>Discription:[
	 * 确认到时的订单自动确认收货
	 * ]</p>
	 * Created on 2015-4-13
	 * @author:wangcs
	 */
	void confirmOrderAuto();
	
	/**
	 * 
	 * <p>Discription:[延迟收货  向后延迟7天]</p>
	 * Created on 2015-4-22
	 * @param orderId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> delayDelivery(String orderId);

    /**
     * 根据条件更改订单信息
     * @param orderDTO
     * @return
     */
    public  ExecuteResult<TradeOrdersDTO> updateTradeOrdersDTOSelective(TradeOrdersDTO orderDTO);
    
    /**
     * 
     * <p>Discription:[
     * 	删除订单，物理删除，用作数据回滚
     * ]</p>
     * Created on 2015-4-20
     * @param orderId
     * @return
     * @author:wangcs
     */
    public ExecuteResult<String> deleteTradeOrders(String orderId);

    /**
     * 
     * <p>Discription:[修改订单评价状态]</p>
     * Created on 2015-4-22
     * @param inDTO  卖家评价和买家评价有且只有一个不为空，订单号不能为空; 买家评价时自动修改订单状态
     * @return
     * @author:wangcs
     */
    public ExecuteResult<String> modifyEvaluationStatus(TradeOrdersDTO inDTO);
    
    /**
     * 
     * <p>Discription:[修改订单物流信息]</p>
     * Created on 2015-5-19
     * @param inDTO 订单号不能为空  物流公司字段不能为空
     * @return
     * @author:wangcs
     */
    public ExecuteResult<String> modifyLogisticsInfo(TradeOrdersDTO inDTO);
    
    
    
    
	
	/**
	 * 根据协议单号查询，总共购买个数
	 *
	 */
 
    public	ExecuteResult<List<TradeOrderItemsDTO>> countNumber(String contractNo);
    
	/**
	 * 根据协议单号查询，总共购买个数
	 *
	 */
    public	ExecuteResult<List<TradeOrderItemsDTO>> countCost(String contractNo);

   /**
    * 
    * 订单 默认好评
    *  
    */
    public void updateOrderDefaultEvaluate(Date date);
    
}
