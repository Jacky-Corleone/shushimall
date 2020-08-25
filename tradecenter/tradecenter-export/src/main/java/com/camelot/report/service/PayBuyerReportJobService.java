package com.camelot.report.service;

/**
 * 买家报表定时任务service
 */

public interface PayBuyerReportJobService {
	
	/**
	 * 
	 * 把查出来的用户的信息到report_pay_buyer表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void insertReportPayBuyerByUser(String createTimeStart, String createTimeEnd);
	

	/**
	 * 从tradecenter.trade_orders表里更新订单的信息到report_pay_buyer表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void updateReportPayBuyerByOrder(String yesterday);
}
