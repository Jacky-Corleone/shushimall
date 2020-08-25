package com.camelot.tradecenter.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * 默认评价定时任务
 */


public class OrderDefaultEvaluateJob{

	private static final Logger logger = LoggerFactory.getLogger(OrderDefaultEvaluateJob.class);
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	public void updateOrderDefaultEvaluate(){
		logger.info("\n 方法[{}]，入参：[{}]","OrderDefaultEvaluateJob-updateOrderDefaultEvaluate");
		logger.info("======默认好评定时任务开始=================");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Date date = cal.getTime();
		
		//------------测试一小时start------------
        
        /*Calendar calendarTest = Calendar.getInstance();
        calendarTest.set(Calendar.HOUR_OF_DAY, calendarTest.get(Calendar.HOUR_OF_DAY) - 1);
        date = calendarTest.getTime();*/
        //-------------测试一小时end----------
		
		
		tradeOrderExportService.updateOrderDefaultEvaluate(date);
		
		logger.info("======默认评价定时任务结束=================");
	}
	
	
}
