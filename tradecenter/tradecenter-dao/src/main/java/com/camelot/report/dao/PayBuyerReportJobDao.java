package com.camelot.report.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.report.dto.PayBuyerInfo;


public interface PayBuyerReportJobDao extends BaseDAO<PayBuyerInfo>{
	
	/**
	 * 
	 * 把查出来的用户的信息到report_pay_buyer表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void insertReportPayBuyerByUser(@Param("payBuyerInfo")PayBuyerInfo payBuyerInfo);

	/**
	 * 从tradecenter.trade_orders表里更新订单的信息到report_pay_buyer表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void updateReportPayBuyerByOrder(@Param("dealDate")String dealDate);
}
