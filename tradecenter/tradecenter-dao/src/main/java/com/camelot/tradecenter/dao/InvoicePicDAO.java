package com.camelot.tradecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.InvoicePicDTO;

public interface InvoicePicDAO extends BaseDAO<InvoicePicDTO> {
	
	/**
	 * 
	 * <p>Description: [根据发票ID查询]</p>
	 * Created on 2015年9月17日
	 * @param invoiceId
	 * @return
	 * @author:[宋文斌]
	 */
	List<InvoicePicDTO> queryByInvoiceId(@Param("invoiceId") Long invoiceId);
}
