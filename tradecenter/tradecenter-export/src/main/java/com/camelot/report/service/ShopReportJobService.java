package com.camelot.report.service;

import java.util.Date;

/**
 * 店铺报表定时任务service
 */

public interface ShopReportJobService {
	
	/**
	 * 
	 * 把查出来的用户的信息到report_sell_shop表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void insertReportShopReportByShop(Date yesterdayDate,Date todayDate);
	

	/**
	 * 从tradecenter.trade_orders表里更新订单的信息到report_sell_shop表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void updateReportShopReportByOrder(String yesterday);
}
