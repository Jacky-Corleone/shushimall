package com.camelot.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.report.dto.OrderDealReportIn;
import com.camelot.report.dto.OrderDealReportOut;

public interface OrderDealReportDAO extends BaseDAO<OrderDealReportOut>{
	
	
	/**
	 * 
	 * 查询 成交金额 、 成交人数、 商品数  
	 * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return OrderDealReport
	 */
	public OrderDealReportOut getOrderDealPaySum(@Param("reportIn") OrderDealReportIn orderDealReportIn);

	/**
	 * 
	 * 查询 订单数、成交转化率
	 * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return OrderDealReport
	 */
	public OrderDealReportOut getOrderDealNumSum(@Param("reportIn") OrderDealReportIn orderDealReportIn);
	
	
	/**
	 * 
	 * 分页查询 成交金额 、 成交人数、 商品数 、订单数、成交转化率
	 * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return List<OrderDealReport>
	 */
	public List<OrderDealReportOut> queryOrderDealReportList (@Param("reportIn") OrderDealReportIn orderDealReportIn,
			@Param("page") Pager<OrderDealReportOut> pager);
	
	/**
	 * 
	 * <p>Discription:[查询分页的总数]</p>
	 * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return List<OrderDealReport>
	 */
	public Long queryOrderDealReportCount (@Param("reportIn") OrderDealReportIn orderDealReportIn);
}
