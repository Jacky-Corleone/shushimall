package com.camelot.tradecenter.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.report.service.ShopReportJobService;

/**
 * 平台后台>运营分析>店铺报表 定时任务
 */


public class ShopReportJob{

	private static final Logger logger = LoggerFactory.getLogger(ShopReportJob.class);
	
	@Resource
	private ShopReportJobService shopReportJobService;
	
	public void updateReportShop(){
		logger.info("\n 方法[{}]，入参：[{}]","ShopReportJob-updateReportShop");
		
		Calendar cal = Calendar.getInstance();
		Date todayDate=cal.getTime();
		cal.add(Calendar.DATE, -1);
		Date yesterdayDate=cal.getTime();
		SimpleDateFormat df =new SimpleDateFormat("yyyyMMdd");
		String yesterday=df.format(cal.getTime());
		//查询前一天开通的店铺 并把相关字段插入到tradecenter.report_sell_shop表
		shopReportJobService.insertReportShopReportByShop(yesterdayDate, todayDate);
		//从订单表里查询相关数据 插入到report_sell_shop表
		shopReportJobService.updateReportShopReportByOrder(yesterday);
	}
	
	
}
