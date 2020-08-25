package com.camelot.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.util.DateDealUtils;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dao.PayBuyerInfoDao;
import com.camelot.report.dao.ShopReportDao;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.dto.ShopReportDTO;
import com.camelot.report.service.TradeReportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

@Service("tradeReportService")
public class TradeReportServiceImpl implements TradeReportService {
	private final static Logger logger = LoggerFactory.getLogger(TradeReportServiceImpl.class);
	
    @Resource
    private TradeOrderExportService tradeOrderExportService;

    @Resource
    private UserExportService userExportService;

    @Resource
    private ShopReportDao shopReportDao;
    
    @Resource
    private PayBuyerInfoDao payBuyerInfoDao;


    @Override
    public DataGrid<ShopReportDTO> getShopReportList(ShopReportDTO shopReportDTO, Pager<ShopReportDTO> pager) {
    	logger.info("\n 方法[{}]，入参：[{}]","TradeReportServiceImpl-getShopReportList",JSONObject.toJSONString(shopReportDTO),
				JSONObject.toJSONString(pager));
    	shopReportDTO.setPassTimeStart(DateDealUtils.dateWithoutFormat(shopReportDTO.getPassTimeStart()));
    	shopReportDTO.setPassTimeEnd(DateDealUtils.dateWithoutFormat(shopReportDTO.getPassTimeEnd()));
    	
        DataGrid<ShopReportDTO> res = new DataGrid<ShopReportDTO>();
        Long count = shopReportDao.queryCount(shopReportDTO);
        List<ShopReportDTO> list = shopReportDao.queryList(shopReportDTO, pager);
        res.setRows(list);
        res.setTotal(count);
        return res;
    }

    @Override
    public DataGrid<ShopCustomerReportDTO> queryShopCustomerList(ReportQueryDTO queryDto, Pager pager) {
    	logger.info("\n 方法[{}]，入参：[{}]","TradeReportServiceImpl-queryShopCustomerList",JSONObject.toJSONString(queryDto),
				JSONObject.toJSONString(pager));
    	
        DataGrid<ShopCustomerReportDTO> res = new DataGrid<ShopCustomerReportDTO>();
        List<ShopCustomerReportDTO> resList = new ArrayList<ShopCustomerReportDTO>();

        if (queryDto != null && queryDto.getShopId() != null) {
            TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
            inDTO.setShopId(queryDto.getShopId());
            Pager<TradeOrdersQueryInDTO> orderPager = new Pager<TradeOrdersQueryInDTO>();
            pager.setRows(99999);
            ExecuteResult<DataGrid<TradeOrdersDTO>> orderData = tradeOrderExportService.queryOrders(inDTO, orderPager);
            if (orderData != null && orderData.getResult() != null) {
                DataGrid<TradeOrdersDTO> orderDataGrid = orderData.getResult();
                if (orderDataGrid != null) {
                    List<TradeOrdersDTO> orderList = orderDataGrid.getRows();
                    List<String> idList = new ArrayList<String>();
                    if (orderList != null && orderList.size() > 0) {
                        for (TradeOrdersDTO item : orderList) {
                            idList.add(item.getBuyerId().toString());
                        }
                    }
                    if (idList != null && idList.size() > 0) {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setUname(queryDto.getUserName());
                        userDTO.setCompanyName(queryDto.getCompanyName());
                        userDTO.setCompanyAddr(queryDto.getCompanyAddress());
                        if(queryDto.getCreateBegin()!=null){
                            userDTO.setCreateTimeBegin(DateUtils.format(queryDto.getCreateBegin(), DateUtils.YMD_DASH));
                        }
                        if(queryDto.getCreateEnd()!=null){
                            userDTO.setCreateTimeEnd(DateUtils.format(DateUtils.dayOffset(queryDto.getCreateEnd(), 1), DateUtils.YMD_DASH));
                        }
                        DataGrid<UserDTO> userData = userExportService.queryUserListByCondition(userDTO, null, idList, pager);
                        if (userData != null && userData.getRows() != null && userData.getRows().size() > 0) {
                            for (UserDTO userItem : userData.getRows()) {
                                ShopCustomerReportDTO reportDTO = new ShopCustomerReportDTO();
                                reportDTO.setCompanyName(userItem.getCompanyName());
                                reportDTO.setAddress(userItem.getCompanyAddr());
                                reportDTO.setUserName(userItem.getUname());
                                reportDTO.setNikeName(userItem.getNickname());
                                reportDTO.setUserId(userItem.getUid());
                                reportDTO.setCreateDt(userItem.getCreated());
                                resList.add(reportDTO);
                            }
                        }
                        res.setTotal(userData.getTotal());
                    }
                }
            }
            res.setRows(resList);
        } else {
            throw new RuntimeException("店铺ID不能为空");

        }
        return res;
    }

    @Override
    public DataGrid<PayBuyerInfo> getCustomerReport(PayBuyerInfo payBuyerInfo, Pager<PayBuyerInfo> pager) {
    	logger.info("\n 方法[{}]，入参：[{}]","TradeReportServiceImpl-getCustomerReport",JSONObject.toJSONString(payBuyerInfo),
				JSONObject.toJSONString(pager));
    	payBuyerInfo.setCreateTimeStart(DateDealUtils.dateWithoutFormat(payBuyerInfo.getCreateTimeStart()));
    	payBuyerInfo.setCreateTimeEnd(DateDealUtils.dateWithoutFormat(payBuyerInfo.getCreateTimeEnd()));
    	
        DataGrid<PayBuyerInfo> res = new DataGrid<PayBuyerInfo>();

        Long count = payBuyerInfoDao.queryCount(payBuyerInfo);
        List<PayBuyerInfo> list = payBuyerInfoDao.queryList(payBuyerInfo, pager);
        
        res.setRows(list);
        res.setTotal(count);
        return res;
    }
}
