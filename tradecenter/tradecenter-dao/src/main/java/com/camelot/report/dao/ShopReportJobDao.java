package com.camelot.report.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.report.dto.ShopReportDTO;


public interface ShopReportJobDao extends BaseDAO<ShopReportDTO>{
	
	/**
	 * 
	 * 把查出来的店铺的信息到report_sell_shop表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void insertReportShopReportByShop(@Param("shopReportDTO")ShopReportDTO shopReportDTO);

	/**
	 * 从tradecenter.trade_orders表里更新订单的信息到report_sell_shop表
	 * @param dealDate  日期  yyyyMMdd
	 */
	public void updateReportShopReportByOrder(@Param("dealDate")String dealDate);
}
