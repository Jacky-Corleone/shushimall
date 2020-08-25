package com.camelot.payment.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.Transations;

public interface TransationsDAO extends BaseDAO<Transations>{
	
	/**
	 * 批量添加
	 * @param orderNo
	 * @return
	 */
	int adds(@Param("listTransations")List<Transations> listTransations);
	
	/**
	 * 根据订单号批量处理订单（父单和子单）
	 * @param orderNo
	 * @return
	 */
	int updatesByParentId(Transations transations);
	
	/**
	 * 根据订单号修改
	 * 
	 * @param orderNo
	 * @return
	 */
	int updateByOrderId(Transations transations);
	
	/**
	 * 根据订单号修改对外交易号，其实此处是生成对外交易号，之前的对外交易号为0
	 * @param orderId
	 * @return
	 */
	int updateOutTradeNoByOrderId(@Param("outTradeNo") String outTradeNo ,@Param("orderId") String orderId);
	
	
	/**
	 * 根据对外交易号更改价格
	 * 
	 * @param orderNo
	 * @return
	 */
	int updateAmount(Transations transations);
	
	/**
	 * 根据订单号查询有效的支付订单记录对象
	 * @param orderNo
	 * @return
	 */
	Transations selectTransByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 根据对外订单号查询有效的支付订单记录对象
	 * @param orderNo
	 * @return
	 */
	Transations selectTransByOutTrade(@Param("outTradeNo") String outTradeNo);
	
	/**
	 * 根据对外订单号组查询有效的子支付订单记录对象
	 * 
	 * @param orderNo
	 * @return
	 */
	List<Transations> selectChildTransByParentNo(@Param("orderParentNo")String orderParentNo);
	
	/**
	 * 生成对外交易流水号
	 * @param outTradeNo
	 * @return
	 */
	String selectOutTranNo(@Param("payType")String payType);
}
