package com.camelot.tradecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.domain.TradeApprovedOrders;
import com.camelot.tradecenter.domain.TradeOrders;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;

/**
 * 
 * <p>Description: [订单审核dao]</p>
 * Created on 2015-8-25
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface TradeApprovedOrdersDao extends BaseDAO<TradeApprovedOrders>{
	/**
	 * 
	 * <p>Discription:[查询所有非父级订单,parent_order_id!=0]</p>
	 * Created on 2015-8-25
	 * @param inDTO
	 * @param pager
	 * @return
	 * @author:[王鹏]
	 */
	List<TradeOrdersDTO> queryTradeOrders(@Param("entity") TradeOrdersQueryInDTO inDTO, @Param("page") Pager<TradeOrdersQueryInDTO> pager);
	/**
	 * 
	 * <p>Discription:[查询订单数量]</p>
	 * Created on 2015-8-25
	 * @param inDTO
	 * @return
	 * @author:[王鹏]
	 */
	Long queryTradeOrdersCount(@Param("entity") TradeOrdersQueryInDTO inDTO);
	/**
	 * 
	 * <p>Discription:[根据订单审核表中复制子订单数据]</p>
	 * Created on 2015-8-26
	 * @param orderId
	 * @author:[王鹏]
	 */
	public void insertOrdersFromApprovedOrdersByoid(@Param("orderId")String orderId);
	/**
	 * 
	 * <p>Discription:[往订单表中复制父订单数据]</p>
	 * Created on 2015-8-26
	 * @param parentOrderId
	 * @author:[王鹏]
	 */
	public void insertOrdersFromApprovedOrdersBypid(@Param("parentOrderId")String parentOrderId);
	/**
	 * 
	 * <p>Discription:[修改审核订单信息]</p>
	 * Created on 2015-8-26
	 * @author:[王鹏]
	 */
	public void modifyApprovedStatus(@Param("entity") TradeOrdersQueryInDTO inDTO);

}
