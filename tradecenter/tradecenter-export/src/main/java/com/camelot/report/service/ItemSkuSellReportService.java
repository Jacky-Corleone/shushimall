package com.camelot.report.service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.ItemSkuSellReportIn;
import com.camelot.report.dto.ItemSkuSellReportOut;
/**
 * 经营状况分析 报表
 */

public interface ItemSkuSellReportService {
    
    /**
     * 获取   销售额、销售量 列表及数量
     * @param shopId  店铺id (不能为空)
     * @param shopId  skuid (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
     * @return
     */
    public DataGrid<ItemSkuSellReportOut> getItemSkuSellListByPager(ItemSkuSellReportIn itemSkuSellReportIn,Pager<ItemSkuSellReportOut> pager);
    
    /**
     * 获取  销售额、销售量 折线图
     * @param shopId  店铺id (不能为空)
     * @param shopId  skuid (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
     * @return
     */
    public DataGrid<ItemSkuSellReportOut> getItemSkuSellListByLion(ItemSkuSellReportIn itemSkuSellReportIn) throws Exception;
    
}
