package com.camelot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.CompanyPayJob;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface CompanyPayJobDAO  extends BaseDAO<CompanyPayJob>{
	
	/**
	 * 
	 * @param outTradeNo
	 * @return
	 */
	long queryByOutTrade(@Param("outTradeNo")String outTradeNo);
	
	/**
	 * 根据批次升序查询未处理的充值待支付订单（72小时内，10条）
	 * 
	 * @return
	 */
	List<CompanyPayJob> selectUnDeal();
	
	/**
	 * 根据ID处理
	 * 
	 * @param id
	 * @return
	 */
	int dealById(@Param("id")Long id);
	
	/**
	 * 根据对外交易号生成有效job
	 * 
	 * @param id
	 * @return
	 */
	int enableByOutTradeNo(@Param("outTradeNo")String outTradeNo);
}