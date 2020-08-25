package com.camelot.tradecenter;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.service.InvoiceExportService;

/**
 * 
 * <p>
 * Description: [发票测试]
 * </p>
 * Created on 2015年9月11日
 * 
 * @author <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class InvoiceExportServiceImplAllTest {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceExportServiceImplAllTest.class);

	private InvoiceExportService invoiceExportService = null;
	ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		invoiceExportService = (InvoiceExportService) ctx.getBean("invoiceExportService");
	}

	/**
	 * 
	 * <p>
	 * Description: [添加发票]
	 * </p>
	 * Created on 2015年9月13日
	 * 
	 * @author:[宋文斌]
	 */
	@Test
	public void addInvoiceTest() {
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO.setInvoice(3);
		// invoiceDTO.setInvoiceTitle("北京柯莱特");
		invoiceDTO.setCompanyName("北京柯莱特科技有限公司");
		invoiceDTO.setTaxpayerCode("123456");
		invoiceDTO.setRegisteredAddress("北京市朝阳区");
		invoiceDTO.setRegisteredPhone("13555558888");
		invoiceDTO.setBankName("民生银行");
		invoiceDTO.setBankAccount("6222300101321712");
		invoiceDTO.setNormalContent("明细");
		invoiceDTO.setConsigneeName("camelot");
		invoiceDTO.setConsigneeMobile("13888886666");
		invoiceDTO.setProvinceId(null);
		invoiceDTO.setCityId(1101);
		invoiceDTO.setCountyId(null);
		invoiceDTO.setFullAddress("北京市 市辖区 东城区 xxxxxxxxxxxxxx");
		invoiceDTO.setDetailAddress("xxxxxxxxxxxxxx");
		invoiceDTO.setBuyerId(1000000853L);
		invoiceDTO.setCreateTime(new Date());
		invoiceDTO.setBusinessLicensePicUrl(new String[] { "/2015/7/23/386a3604-9ea0-4c6d-afe7-8ea81e51a66e.png" });
		invoiceDTO.setTaxRegistrationCertificatePicUrl(new String[] { "/2015/7/23/a61838df-f2e0-4884-a7d3-34d08887fd00.jpg" });
		invoiceDTO.setGeneralTaxpayerPicUrl(new String[] { "/2015/7/23/a61838df-f2e0-4884-a7d3-34d08887fd00.jpg" });
		ExecuteResult<InvoiceDTO> result = this.invoiceExportService.addInvoice(invoiceDTO);
		Assert.assertEquals(true, result.isSuccess());
	}

	@Test
	public void queryInvoicesTest() {
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		DataGrid<InvoiceDTO> dataGrid = invoiceExportService.queryInvoices(invoiceDTO, null);
		logger.info("");
	}

	@Test
	public void queryRecentInvoiceTest() {
		ExecuteResult<InvoiceDTO> result = invoiceExportService.queryRecentInvoice(1000000863L);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void addTradeOrderInvoiceTest() {
		invoiceExportService.addTradeOrderInvoice("1", 1L);
	}
	
	@Test
	public void updateTradeOrderInvoiceTest() {
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO.setBuyerId(1000000863L);
		invoiceDTO.setOrderId("201509150230001");
		invoiceDTO.setInvoice(2);// 普通发票
		invoiceDTO.setInvoiceTitle("测试title");
		// invoiceExportService.updateTradeOrderInvoice(invoiceDTO);
	}
}
