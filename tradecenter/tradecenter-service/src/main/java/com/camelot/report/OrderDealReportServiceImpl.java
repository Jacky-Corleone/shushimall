package com.camelot.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.util.DateDealUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dao.OrderDealReportDAO;
import com.camelot.report.dto.OrderDealReportIn;
import com.camelot.report.dto.OrderDealReportOut;
import com.camelot.report.service.OrderDealReportService;

@Service("orderDealReportService")
public class OrderDealReportServiceImpl implements OrderDealReportService{

	private final static Logger logger = LoggerFactory.getLogger(OrderDealReportServiceImpl.class);
	@Resource
    private OrderDealReportDAO orderDealReportDAO;
	
	@Override
	public OrderDealReportOut getOrderDealSum(OrderDealReportIn orderDealReportIn) {
		orderDealReportIn.setStartDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getStartDate()));
		orderDealReportIn.setEndDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getEndDate()));
		
		logger.info("\n 方法[{}]，入参：[{}]","OrderDealReportServiceImpl-getOrderDealSum",JSONObject.toJSONString(orderDealReportIn));
		
		// 查询 成交金额 、 成交人数、 商品数  
		OrderDealReportOut payReport = orderDealReportDAO.getOrderDealPaySum(orderDealReportIn);
		// 查询 订单数、成交转化率
		OrderDealReportOut numReport =orderDealReportDAO.getOrderDealNumSum(orderDealReportIn);
		if(payReport!=null && numReport!=null){
			payReport.setOrderNum(numReport.getOrderNum());
			payReport.setPayConversion(numReport.getPayConversion());
			return payReport;
		}else{
			return new OrderDealReportOut();
		}
	}

	@Override
	public DataGrid<OrderDealReportOut> getOrderDealListByPager(OrderDealReportIn orderDealReportIn,Pager<OrderDealReportOut> pager) {
		logger.info("\n 方法[{}]，入参：[{}]","OrderDealReportServiceImpl-getOrderDealListByPager",JSONObject.toJSONString(orderDealReportIn));
		
		orderDealReportIn.setStartDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getStartDate()));
		orderDealReportIn.setEndDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getEndDate()));
		
		DataGrid<OrderDealReportOut> res = new DataGrid<OrderDealReportOut>();
		//查询数量
		Long count = orderDealReportDAO.queryOrderDealReportCount(orderDealReportIn);
		//查询列表
		List<OrderDealReportOut> list = orderDealReportDAO.queryOrderDealReportList(orderDealReportIn, pager);
		if(orderDealReportIn.getDateFormat()!=null && orderDealReportIn.getDateFormat().length()>0){
			for (OrderDealReportOut od : list) {
				if(od.getDealDate()!=null){
					od.setDealDate(DateDealUtils.getDateStrToStr(od.getDealDate(), "yyyyMMdd", orderDealReportIn.getDateFormat()));
				}
			}
		}
		
		res.setTotal(count);
		res.setRows(list);
		return res;
	}

	@Override
	public DataGrid<OrderDealReportOut> getOrderDealListByLion(OrderDealReportIn orderDealReportIn) throws Exception {
		logger.info("\n 方法[{}]，入参：[{}]","OrderDealReportServiceImpl-getOrderDealListByLion",JSONObject.toJSONString(orderDealReportIn));
		
		orderDealReportIn.setStartDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getStartDate()));
		orderDealReportIn.setEndDate(DateDealUtils.dateWithoutFormat(orderDealReportIn.getEndDate()));
		
		DataGrid<OrderDealReportOut> res = new DataGrid<OrderDealReportOut>();
		
		List<OrderDealReportOut> list=new ArrayList<OrderDealReportOut>();
		
		//间隔时间天数不能为空
		if(orderDealReportIn!=null && orderDealReportIn.getDayInterval()!=null && orderDealReportIn.getDayInterval()>0){
			
			Map<String,OrderDealReportOut> map=new LinkedHashMap<String,OrderDealReportOut>();	// map 键：日期，值：OrderDealReportOut类
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			
			String startDate=orderDealReportIn.getStartDate();	//开始日期
			String endDate=orderDealReportIn.getEndDate();		//结束日期
			
			if(endDate==null || endDate.length()==0){
				endDate=df.format(new Date());
				orderDealReportIn.setEndDate(endDate);
			}
			Pager<OrderDealReportOut> pager=new Pager<OrderDealReportOut>();
			pager.setRows(orderDealReportIn.getDayInterval());
			
			//获取日期list   倒序
			List<String> dateList = DateDealUtils.getListDateReduce(startDate, endDate, orderDealReportIn.getDayInterval());
			
		    if(dateList!=null && dateList.size()>0){
		    	 //查询list
			    List<OrderDealReportOut> queryOrderDealReportList = orderDealReportDAO.queryOrderDealReportList(orderDealReportIn, pager);
			    for (OrderDealReportOut report : queryOrderDealReportList) {
			    	map.put(report.getDealDate(), report);
			    }
			  
			    //倒序循环日期list，这样结果集里的日期就是正序
			    for (int i = dateList.size()-1; i >= 0; i--) {
			    	String date=dateList.get(i);
			    	if(map.keySet().contains(date)){
			    		list.add(map.get(date));
			    	}else{
			    		list.add(new OrderDealReportOut(date));
			    	}
				}
		    }
		   
		}
		if(orderDealReportIn.getDateFormat()!=null && orderDealReportIn.getDateFormat().length()>0){
			for (OrderDealReportOut od : list) {
				if(od.getDealDate()!=null){
					od.setDealDate(DateDealUtils.getDateStrToStr(od.getDealDate(), "yyyyMMdd", orderDealReportIn.getDateFormat()));
				}
			}
		}
		
		res.setRows(list);
		res.setTotal(list.size()+0L);
		return res;
	}

}
