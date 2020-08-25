package com.camelot.report.service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.dto.ShopReportDTO;
/**
 * 店铺报表
 */

public interface TradeReportService {

    /**
     * 店铺报表
     * @param queryDto
     * @return
     */
    public DataGrid<ShopReportDTO> getShopReportList(ShopReportDTO shopReportDTO,Pager<ShopReportDTO> pager);


    /**
     * 店铺会员列表 传入参 店铺ID必传
     * @param queryDto
     * @param pager
     * @return
     */
    public DataGrid<ShopCustomerReportDTO> queryShopCustomerList(ReportQueryDTO queryDto,Pager pager);


    /**
     * 买家报表
     * @param queryDto
     * @param pager
     * @return
     */
    public DataGrid<PayBuyerInfo> getCustomerReport(PayBuyerInfo payBuyerInfo,Pager<PayBuyerInfo> pager);

}
