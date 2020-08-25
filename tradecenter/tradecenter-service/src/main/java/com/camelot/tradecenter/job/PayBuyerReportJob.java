package com.camelot.tradecenter.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.report.service.PayBuyerReportJobService;

/**
 * 平台后台>运营分析>买家报表 定时任务
 */


public class PayBuyerReportJob {

	private static final Logger logger = LoggerFactory.getLogger(PayBuyerReportJob.class);
	
	@Resource
	private PayBuyerReportJobService payBuyerReportJobService;
	
	public void updateReportPayBuyer(){
		logger.info("\n 方法[{}]，入参：[{}]","PayBuyerReportJob-updateReportPayBuyer");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
		String yesterday=df.format(cal.getTime());
		//查询前一天注册的用户 并把相关字段插入到tradecenter.report_pay_buyer表
		payBuyerReportJobService.insertReportPayBuyerByUser(yesterday,yesterday);
		//从订单表里查询相关数据 插入到report_pay_buyer表
		payBuyerReportJobService.updateReportPayBuyerByOrder(yesterday.replace("-", ""));
	}
	
	
}
