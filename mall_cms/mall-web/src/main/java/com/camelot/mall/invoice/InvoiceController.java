package com.camelot.mall.invoice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.util.WebUtil;

/**
 * 
 * <p>Description: [发票]</p>
 * Created on 2015年9月16日
 * @author  <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private InvoiceExportService invoiceExportService;

	/**
	 * 
	 * <p>Description: [保存发票信息]</p>
	 * Created on 2015年9月16日
	 * @param invoiceDTO
	 * @param request
	 * @param response
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/save")
	@ResponseBody
	public ExecuteResult<InvoiceDTO> saveInvoice(InvoiceDTO invoiceDTO, HttpServletRequest request,
			HttpServletResponse response) {
		Long uid = WebUtil.getInstance().getUserId(request);
		invoiceDTO.setBuyerId(uid);
		ExecuteResult<InvoiceDTO> er = this.invoiceExportService.addInvoice(invoiceDTO);
		return er;
	}
	
	/**
	 * 
	 * <p>Description: [修改发票信息]</p>
	 * Created on 2015年9月18日
	 * @param invoiceDTO
	 * @param request
	 * @param response
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/updateTradeOrderInvoice")
	@ResponseBody
	public ExecuteResult<InvoiceDTO> updateTradeOrderInvoice(InvoiceDTO invoiceDTO, HttpServletRequest request,
			HttpServletResponse response) {
		Long uid = WebUtil.getInstance().getUserId(request);
		invoiceDTO.setBuyerId(uid);
		ExecuteResult<InvoiceDTO> er = this.invoiceExportService.updateTradeOrderInvoice(invoiceDTO);
		return er;
	}
}
