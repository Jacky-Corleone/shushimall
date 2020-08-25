package com.camelot.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.util.DateDealUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dao.ItemSkuSellReportDAO;
import com.camelot.report.dto.ItemSkuSellReportIn;
import com.camelot.report.dto.ItemSkuSellReportOut;
import com.camelot.report.service.ItemSkuSellReportService;

@Service("itemSkuSellReportService")
public class ItemSkuSellReportServiceImpl implements ItemSkuSellReportService{

	private final static Logger logger = LoggerFactory.getLogger(ItemSkuSellReportServiceImpl.class);
	@Resource
    private ItemSkuSellReportDAO itemSkuSellReportDAO;
	
	@Override
	public DataGrid<ItemSkuSellReportOut> getItemSkuSellListByPager(ItemSkuSellReportIn itemSkuSellReportIn,
			Pager<ItemSkuSellReportOut> pager) {
		logger.info("\n 方法[{}]，入参：[{}]","ItemSkuSellReportServiceImpl-getItemSkuSellListByPager",JSONObject.toJSONString(itemSkuSellReportIn));
		
		itemSkuSellReportIn.setStartDate(DateDealUtils.dateWithoutFormat(itemSkuSellReportIn.getStartDate()));
		itemSkuSellReportIn.setEndDate(DateDealUtils.dateWithoutFormat(itemSkuSellReportIn.getEndDate()));
		
		DataGrid<ItemSkuSellReportOut> res = new DataGrid<ItemSkuSellReportOut>();
		//查询数量
		Long count = itemSkuSellReportDAO.queryItemSkuSellReportCount(itemSkuSellReportIn);
		//查询列表
		List<ItemSkuSellReportOut> list = itemSkuSellReportDAO.queryItemSkuSellReportList(itemSkuSellReportIn, pager);
		if(itemSkuSellReportIn.getDateFormat()!=null && itemSkuSellReportIn.getDateFormat().length()>0){
			for (ItemSkuSellReportOut is : list) {
				if(!StringUtils.isEmpty(is.getDealDate())){
					is.setDealDate(DateDealUtils.getDateStrToStr(is.getDealDate(), "yyyyMMdd", itemSkuSellReportIn.getDateFormat()));
				}
			}
		}
		res.setTotal(count);
		res.setRows(list);
		return res;
	}
	@Override
	public DataGrid<ItemSkuSellReportOut> getItemSkuSellListByLion(ItemSkuSellReportIn itemSkuSellReportIn) throws Exception {
		
		logger.info("\n 方法[{}]，入参：[{}]","ItemSkuSellReportServiceImpl-getItemSkuSellListByLion",JSONObject.toJSONString(itemSkuSellReportIn));
		
		DataGrid<ItemSkuSellReportOut> res = new DataGrid<ItemSkuSellReportOut>();
		
		List<ItemSkuSellReportOut> list=new ArrayList<ItemSkuSellReportOut>();
		
		itemSkuSellReportIn.setStartDate(DateDealUtils.dateWithoutFormat(itemSkuSellReportIn.getStartDate()));
		itemSkuSellReportIn.setEndDate(DateDealUtils.dateWithoutFormat(itemSkuSellReportIn.getEndDate()));
		
		//间隔时间天数不能为空
		if(itemSkuSellReportIn!=null && itemSkuSellReportIn.getDayInterval()!=null && itemSkuSellReportIn.getDayInterval()>0){
			
			Map<String,ItemSkuSellReportOut> map=new LinkedHashMap<String,ItemSkuSellReportOut>();	// map 键：日期，值：ItemSkuSellReportOut类
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			
			String startDate=itemSkuSellReportIn.getStartDate();	//开始日期
			String endDate=itemSkuSellReportIn.getEndDate();		//结束日期
			
			if(endDate==null || endDate.length()==0){
				endDate=df.format(new Date());
				itemSkuSellReportIn.setEndDate(endDate);
			}
			Pager<ItemSkuSellReportOut> pager=new Pager<ItemSkuSellReportOut>();
			pager.setRows(itemSkuSellReportIn.getDayInterval());
			
			//获取日期list   倒序
			List<String> dateList = DateDealUtils.getListDateReduce(startDate, endDate, itemSkuSellReportIn.getDayInterval());
			
		    if(dateList!=null && dateList.size()>0){
		    	 //查询list
			    List<ItemSkuSellReportOut> queryOrderDealReportList = itemSkuSellReportDAO.queryItemSkuSellReportList(itemSkuSellReportIn, pager);
			    for (ItemSkuSellReportOut report : queryOrderDealReportList) {
			    	map.put(report.getDealDate(), report);
			    }
			  
			    //倒序循环日期list，这样结果集里的日期就是正序
			    for (int i = dateList.size()-1; i >= 0; i--) {
			    	String date=dateList.get(i);
			    	if(map.keySet().contains(date)){
			    		list.add(map.get(date));
			    	}else{
			    		list.add(new ItemSkuSellReportOut(date));
			    	}
				}
		    }
		   
		}
		
		if(itemSkuSellReportIn.getDateFormat()!=null && itemSkuSellReportIn.getDateFormat().length()>0){
			for (ItemSkuSellReportOut is : list) {
				is.setDealDate(DateDealUtils.getDateStrToStr(is.getDealDate(), "yyyyMMdd", itemSkuSellReportIn.getDateFormat()));
			}
		}
		
		res.setRows(list);
		res.setTotal(list.size()+0L);
		return res;
	}
	

}
