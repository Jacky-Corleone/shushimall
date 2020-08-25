package com.camelot.tradecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.InvoiceDTO;

/**
 * 
 * <p>Description: [发票]</p>
 * Created on 2015年9月11日
 * @author  <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface InvoiceExportService {
	/**
	 * 
	 * <p>
	 * Description: [新增发票信息]
	 * </p>
	 * Created on 2015年9月10日
	 * 
	 * @param dto
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<InvoiceDTO> addInvoice(InvoiceDTO dto);
	
	/**
	 * 
	 * <p>Description: [添加订单发票]</p>
	 * Created on 2015年9月14日
	 * @param orderId 订单ID
	 * @param invoiceId 发票ID
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<String> addTradeOrderInvoice(String orderId,Long invoiceId);

	/**
	 * 
	 * <p>
	 * Description: [根据条件分页查询发票信息]
	 * </p>
	 * Created on 2015年9月10日
	 * 
	 * @param inDTO
	 * @param pager
	 * @return
	 * @author:[宋文斌]
	 */
	DataGrid<InvoiceDTO> queryInvoices(InvoiceDTO inDTO, Pager<InvoiceDTO> pager);
	
	/**
	 * 
	 * <p>Description: [查询用户最近使用的发票信息]</p>
	 * Created on 2015年9月11日
	 * @param buyerId
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<InvoiceDTO> queryRecentInvoice(Long buyerId);
	
	/**
	 * 
	 * <p>Description: [根据ID查询发票信息]</p>
	 * Created on 2015年9月14日
	 * @param invoiceId
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<InvoiceDTO> queryById(Long invoiceId);

	/**
	 * 
	 * <p>Description: [根据订单ID查询发票信息]</p>
	 * Created on 2015年9月14日
	 * @param orderId
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<InvoiceDTO> queryByOrderId(String orderId);

	/**
	 * 
	 * <p>Description: [修改订单发票信息]</p>
	 * Created on 2015年9月18日
	 * @param invoiceDTO
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<InvoiceDTO> updateTradeOrderInvoice(InvoiceDTO invoiceDTO);
}
