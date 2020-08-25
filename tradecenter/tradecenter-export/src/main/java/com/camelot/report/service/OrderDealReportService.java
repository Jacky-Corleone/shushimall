package com.camelot.report.service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.OrderDealReportIn;
import com.camelot.report.dto.OrderDealReportOut;
/**
 * 经营状况分析 报表
 */

public interface OrderDealReportService {

    /**
     * 获取总的   成交金额、购买人数、成交商品数、下单量、成交转化率
     * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
     * @return
     */
    public OrderDealReportOut getOrderDealSum(OrderDealReportIn orderDealReportIn);

    
    /**
     * 获取   成交金额、购买人数、成交商品数、下单量、成交转化率 列表
     * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
     * @return
     */
    public DataGrid<OrderDealReportOut> getOrderDealListByPager(OrderDealReportIn orderDealReportIn,Pager<OrderDealReportOut> pager);
    
    /**
     * 获取   成交金额、购买人数、成交商品数、下单量、成交转化率 折线图
     * @param shopId  店铺id (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
     * @return
     */
    public DataGrid<OrderDealReportOut> getOrderDealListByLion(OrderDealReportIn orderDealReportIn) throws Exception;
    
}
