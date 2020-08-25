package com.camelot.tradecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.InvoiceDTO;

public interface InvoiceDAO extends BaseDAO<InvoiceDTO> {

	/**
	 * 
	 * <p>Description: [查询用户最近使用的发票信息]</p>
	 * Created on 2015年9月11日
	 * @param buyerId
	 * @return
	 * @author:[宋文斌]
	 */
	InvoiceDTO queryRecentInvoice(Long buyerId);

	/**
	 * 
	 * <p>Description: [添加订单发票信息]</p>
	 * Created on 2015年9月14日
	 * @param orderId
	 * @param invoiceId
	 * @author:[宋文斌]
	 */
	void addTradeOrderInvoice(@Param("orderId") String orderId, @Param("invoiceId") Long invoiceId);
	
	/**
	 * 
	 * <p>Description: [修改订单发票信息]</p>
	 * Created on 2015年9月14日
	 * @param orderId
	 * @param invoiceId
	 * @author:[宋文斌]
	 */
	void updateTradeOrderInvoice(@Param("orderId") String orderId, @Param("invoiceId") Long invoiceId);

	/**
	 * 
	 * <p>Description: [根据订单ID查询发票信息]</p>
	 * Created on 2015年9月14日
	 * @param orderId
	 * @return
	 * @author:[宋文斌]
	 */
	InvoiceDTO queryByOrderId(@Param("orderId")String orderId);

}
