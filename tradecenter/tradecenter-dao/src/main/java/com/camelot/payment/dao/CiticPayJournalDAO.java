package com.camelot.payment.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.CiticPayJournal;

/**
 * <p>中信支付记录数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface CiticPayJournalDAO  extends BaseDAO<CiticPayJournal>{
	
	/**
	 * 查询支付成功的对外交易号（对外交易号/父级对外交易号）
	 * 
	 * @return
	 */
	CiticPayJournal selectByTradeNo(@Param("tradeNo")String tradeNo);
	
	/**
	 * 根据对外交易号修改
	 * 
	 * @param citicPayJournal
	 * @return
	 */
	int updateByOutTradeNo (CiticPayJournal citicPayJournal);
}