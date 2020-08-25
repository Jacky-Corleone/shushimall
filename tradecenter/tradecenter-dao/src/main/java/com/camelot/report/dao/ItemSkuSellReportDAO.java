package com.camelot.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.report.dto.ItemSkuSellReportIn;
import com.camelot.report.dto.ItemSkuSellReportOut;

public interface ItemSkuSellReportDAO extends BaseDAO<ItemSkuSellReportOut>{
	
	/**
	 * 
	 * 分页查询 销售额、销售量
	 * @param shopId  店铺id (不能为空)
	 * @param skuId  skuid (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return List<ItemSkuSellReportOut>
	 */
	public List<ItemSkuSellReportOut> queryItemSkuSellReportList (@Param("reportIn") ItemSkuSellReportIn itemSkuSellReportIn,
			@Param("page") Pager<ItemSkuSellReportOut> pager);
	
	/**
	 * 
	 * <p>Discription:[查询分页的总数]</p>
	 * @param shopId  店铺id (不能为空)
	 * @param skuId  skuid (不能为空)
     * @param startDate  开始日期 yyyyMMdd	(如果为空，传null或"")
     * @param endDate  结束日期   yyyyMMdd	(如果为空，传null或"")
	 * @return List<OrderDealReport>
	 */
	public Long queryItemSkuSellReportCount (@Param("reportIn") ItemSkuSellReportIn itemSkuSellReportIn);
}
