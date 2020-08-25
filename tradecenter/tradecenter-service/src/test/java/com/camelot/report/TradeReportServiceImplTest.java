package com.camelot.report;

import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.dto.ShopReportDTO;
import com.camelot.report.service.TradeReportService;

public class TradeReportServiceImplTest extends TestCase {
    private TradeReportService tradeReportService;
    private ApplicationContext ctx;
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("test.xml");
        tradeReportService = (TradeReportService) ctx.getBean("tradeReportService");

    }

    public void testGetShopReportList() throws Exception {
        ShopReportDTO shopReportDTO=new ShopReportDTO();
        shopReportDTO.setShopName("铺");
        shopReportDTO.setPassTimeStart("20150623");
        shopReportDTO.setPassTimeEnd("20150623");
        DataGrid<ShopReportDTO> res =tradeReportService.getShopReportList(shopReportDTO, new Pager<ShopReportDTO>());
        Assert.assertNotNull(res);
    }

    public void testQueryShopCustomerList() throws Exception {
        ReportQueryDTO queryDto=new ReportQueryDTO();
        queryDto.setShopId(2000000262L);
        DataGrid<ShopCustomerReportDTO> res= tradeReportService.queryShopCustomerList(queryDto, new Pager());
        Assert.assertNotNull(res);
    }
    
    public void testGetCustomerReport() throws Exception {
    	PayBuyerInfo payBuyerInfo=new PayBuyerInfo();
    	payBuyerInfo.setUserName("123");
    	payBuyerInfo.setCreateTimeStart("20150623");
    	payBuyerInfo.setCreateTimeEnd("20150623");
    	Pager<PayBuyerInfo> pager=new Pager<PayBuyerInfo>();
    	//pager.setRows(5);
    	DataGrid<PayBuyerInfo> res= tradeReportService.getCustomerReport(payBuyerInfo, pager);
    	Assert.assertNotNull(res);
    }
}