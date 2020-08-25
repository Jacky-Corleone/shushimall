package com.camelot.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dao.InvoiceDAO;
import com.camelot.tradecenter.dao.InvoicePicDAO;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.dto.InvoicePicDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * 
 * <p>
 * Description: [发票功能接口实现]
 * </p>
 * Created on 2015年9月11日
 * 
 * @author <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("invoiceExportService")
public class InvoiceExportServiceImpl implements InvoiceExportService {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceExportServiceImpl.class);

	@Resource
	private InvoiceDAO invoiceDAO;
	@Resource
	private InvoicePicDAO invoicePicDAO;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private AddressBaseService addressBaseService;

	@Override
	public ExecuteResult<InvoiceDTO> addInvoice(InvoiceDTO dto) {
		logger.info("\n 方法[{}]，入参：[{}]", "InvoiceExportServiceImpl-addInvoice", JSONObject.toJSONString(dto));
		ExecuteResult<InvoiceDTO> result = new ExecuteResult<InvoiceDTO>();
		try {
			String proviceName = "";
			String cityName = "";
			String countryName = "";
			String detailAddress = "";
			if (dto.getProvinceId() != null) {
				ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService.queryNameById(dto.getProvinceId());
				proviceName = erProvice.getResult().getName();
			}
			if (dto.getCityId() != null) {
				ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService.queryNameById(dto.getCityId());
				cityName = erCity.getResult().getName();
			}
			if (dto.getCountyId() != null) {
				ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService.queryNameById(dto.getCountyId());
				countryName = erCountry.getResult().getName();
			}
			if (dto.getDetailAddress() != null) {
				detailAddress = dto.getDetailAddress();
			}
			String fullAddress = proviceName + " " + cityName + " " + countryName + " " + detailAddress;
			dto.setFullAddress(fullAddress);
			invoiceDAO.add(dto);
			if (dto.getInvoice() != null && dto.getInvoice() == 3) {// 增值税发票
				// 插入图片
				if (dto.getBusinessLicensePicUrl() != null && dto.getBusinessLicensePicUrl().length > 0) {// 1营业执照
					for (String picUrl : dto.getBusinessLicensePicUrl()) {
						InvoicePicDTO invoicePicDTO = new InvoicePicDTO();
						invoicePicDTO.setInvoiceId(dto.getId());
						invoicePicDTO.setPicType(1);
						invoicePicDTO.setPicUrl(picUrl);
						invoicePicDAO.add(invoicePicDTO);
					}
				}
				if (dto.getTaxRegistrationCertificatePicUrl() != null
						&& dto.getTaxRegistrationCertificatePicUrl().length > 0) {// 2税务登记证
					for (String picUrl : dto.getTaxRegistrationCertificatePicUrl()) {
						InvoicePicDTO invoicePicDTO = new InvoicePicDTO();
						invoicePicDTO.setInvoiceId(dto.getId());
						invoicePicDTO.setPicType(2);
						invoicePicDTO.setPicUrl(picUrl);
						invoicePicDAO.add(invoicePicDTO);
					}
				}
				if (dto.getGeneralTaxpayerPicUrl() != null && dto.getGeneralTaxpayerPicUrl().length > 0) {// 3一般纳税人证明
					for (String picUrl : dto.getGeneralTaxpayerPicUrl()) {
						InvoicePicDTO invoicePicDTO = new InvoicePicDTO();
						invoicePicDTO.setInvoiceId(dto.getId());
						invoicePicDTO.setPicType(3);
						invoicePicDTO.setPicUrl(picUrl);
						invoicePicDAO.add(invoicePicDTO);
					}
				}
			}
			result.setResult(dto);
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-addInvoice", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "InvoiceExportServiceImpl-addInvoice", JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public DataGrid<InvoiceDTO> queryInvoices(InvoiceDTO inDTO, Pager<InvoiceDTO> pager) {
		DataGrid<InvoiceDTO> resultPager = new DataGrid<InvoiceDTO>();
		List<InvoiceDTO> invoiceDTOs = invoiceDAO.queryList(inDTO, pager);
		if (invoiceDTOs != null && invoiceDTOs.size() > 0) {
			for (InvoiceDTO invoiceDTO : invoiceDTOs) {
				if (invoiceDTO.getInvoice() == 3) {// 只有增值税发票才有图片
					List<InvoicePicDTO> invoicePicDTOs = invoicePicDAO.queryByInvoiceId(invoiceDTO.getId());
					invoiceDTO.setInvoicePicDTOs(invoicePicDTOs);
					this.buildRelationship(invoiceDTO);
				}
			}
		}
		long size = invoiceDAO.queryCount(inDTO);
		resultPager.setRows(invoiceDTOs);
		resultPager.setTotal(size);
		logger.info("\n 方法[{}]，出参：[{}]", "InvoiceExportServiceImpl-queryInvoices", JSONObject.toJSONString(resultPager));
		return resultPager;
	}

	@Override
	public ExecuteResult<InvoiceDTO> queryByOrderId(String orderId) {
		logger.info("\n 方法[{}]，入参：[{}]", "InvoiceExportServiceImpl-queryByOrderId", orderId);
		ExecuteResult<InvoiceDTO> executeResult = new ExecuteResult<InvoiceDTO>();
		try {
			InvoiceDTO invoiceDTO = invoiceDAO.queryByOrderId(orderId);
			if (invoiceDTO.getInvoice() == 3) {// 只有增值税发票才有图片
				List<InvoicePicDTO> invoicePicDTOs = invoicePicDAO.queryByInvoiceId(invoiceDTO.getId());
				invoiceDTO.setInvoicePicDTOs(invoicePicDTOs);
				this.buildRelationship(invoiceDTO);
			}
			executeResult.setResult(invoiceDTO);
		} catch (Exception e) {
			executeResult.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-queryByOrderId", e);
		}
		return executeResult;
	}

	@Override
	public ExecuteResult<InvoiceDTO> queryRecentInvoice(Long buyerId) {
		logger.info("\n 方法[{}]，入参：[{}]", "InvoiceExportServiceImpl-queryRecentInvoice", buyerId);
		ExecuteResult<InvoiceDTO> result = new ExecuteResult<InvoiceDTO>();
		try {
			InvoiceDTO invoiceDTO = null;
			// 查询最近的一个订单
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			inDTO.setBuyerId(buyerId);
			Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>(1,1);
			ExecuteResult<DataGrid<TradeOrdersDTO>> er = this.tradeOrderExportService.queryOrders(inDTO, pager);
			if (er.isSuccess() && er.getResult() != null && er.getResult().getRows() != null
					&& er.getResult().getRows().size() > 0) {
				TradeOrdersDTO recentTradeOrdersDTO = er.getResult().getRows().get(0);
				// 获取最近一个订单的发票类型
				int invoice = recentTradeOrdersDTO.getInvoice();
				// 普通发票或增值税发票
				if (invoice == 2 || invoice == 3) {
					invoiceDTO = invoiceDAO.queryByOrderId(recentTradeOrdersDTO.getOrderId());
					if (invoiceDTO != null && invoiceDTO.getInvoice() == 3) {// 只有增值税发票才有图片
						List<InvoicePicDTO> invoicePicDTOs = invoicePicDAO.queryByInvoiceId(invoiceDTO.getId());
						invoiceDTO.setInvoicePicDTOs(invoicePicDTOs);
						this.buildRelationship(invoiceDTO);
					}
				}
			}
			result.setResult(invoiceDTO);
			result.setResultMessage("查询成功");
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-queryRecentInvoice", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "InvoiceExportServiceImpl-queryRecentInvoice", JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<String> addTradeOrderInvoice(String orderId, Long invoiceId) {
		logger.info("\n 方法[{}]，入参：[{},{}]", "InvoiceExportServiceImpl-addTradeOrderInvoice", orderId, invoiceId);
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			invoiceDAO.addTradeOrderInvoice(orderId, invoiceId);
			result.setResult("添加成功");
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-addTradeOrderInvoice", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "InvoiceExportServiceImpl-addTradeOrderInvoice",
				JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<InvoiceDTO> queryById(Long invoiceId) {
		logger.info("\n 方法[{}]，入参：[{}]", "InvoiceExportServiceImpl-queryById", invoiceId);
		ExecuteResult<InvoiceDTO> executeResult = new ExecuteResult<InvoiceDTO>();
		try {
			InvoiceDTO invoiceDTO = invoiceDAO.queryById(invoiceId);
			if (invoiceDTO.getInvoice() == 3) {// 只有增值税发票才有图片
				List<InvoicePicDTO> invoicePicDTOs = invoicePicDAO.queryByInvoiceId(invoiceDTO.getId());
				invoiceDTO.setInvoicePicDTOs(invoicePicDTOs);
				this.buildRelationship(invoiceDTO);
			}
			executeResult.setResult(invoiceDTO);
		} catch (Exception e) {
			executeResult.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-queryById", e);
		}
		return executeResult;
	}
	
	@Override
	public ExecuteResult<InvoiceDTO> updateTradeOrderInvoice(InvoiceDTO invoiceDTO) {
		ExecuteResult<InvoiceDTO> executeResult = new ExecuteResult<InvoiceDTO>();
		try {
			if (invoiceDTO.getBuyerId() == null || invoiceDTO.getOrderId() == null) {
				executeResult.addErrorMessage("参数为空");
			} else {
				// 查询订单是否存在
				TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
				inDTO.setBuyerId(invoiceDTO.getBuyerId());
				inDTO.setOrderId(invoiceDTO.getOrderId());
				ExecuteResult<Long> result = tradeOrderExportService.queryOrderQty(inDTO);
				if (result.isSuccess() && result.getResult().longValue() > 0) {
					// 新增发票
					this.addInvoice(invoiceDTO);
					// 更新订单和发票的关联关系
					this.invoiceDAO.updateTradeOrderInvoice(invoiceDTO.getOrderId(), invoiceDTO.getId());
					executeResult.setResult(invoiceDTO);
				} else {
					executeResult.addErrorMessage("订单不存在");
				}
			}
		} catch (Exception e) {
			executeResult.addErrorMessage(e.toString());
			logger.error("\n 方法[{}]，异常：[{}]", "InvoiceExportServiceImpl-updateTradeOrderInvoice", e);
		}
		return executeResult;
	}
	
	/**
	 * 
	 * <p>Description: [把增值税发票对应的三种图片填充到DTO对应数组中,方便使用]</p>
	 * Created on 2015年9月17日
	 * @param invoiceDTOs
	 * @author:[宋文斌]
	 */
	private void buildRelationship(InvoiceDTO invoiceDTO){
		if (invoiceDTO.getInvoice() == 3 && invoiceDTO.getInvoicePicDTOs() != null
				&& invoiceDTO.getInvoicePicDTOs().size() > 0) {
			// 营业执照
			List<String> businessLicensePicUrls = new ArrayList<String>();
			// 税务登记证
			List<String> taxRegistrationCertificatePicUrls = new ArrayList<String>();
			// 一般纳税人证明
			List<String> generalTaxpayerPicUrls = new ArrayList<String>();
			for (InvoicePicDTO invoicePicDTO : invoiceDTO.getInvoicePicDTOs()) {
				if (invoicePicDTO.getPicType() == 1) {// 营业执照
					businessLicensePicUrls.add(invoicePicDTO.getPicUrl());
				} else if (invoicePicDTO.getPicType() == 2) {// 税务登记证
					taxRegistrationCertificatePicUrls.add(invoicePicDTO.getPicUrl());
				} else if (invoicePicDTO.getPicType() == 3) {// 一般纳税人证明
					generalTaxpayerPicUrls.add(invoicePicDTO.getPicUrl());
				}
			}
			invoiceDTO.setBusinessLicensePicUrl(businessLicensePicUrls.toArray(new String[businessLicensePicUrls.size()]));
			invoiceDTO.setTaxRegistrationCertificatePicUrl(taxRegistrationCertificatePicUrls.toArray(new String[taxRegistrationCertificatePicUrls.size()]));
			invoiceDTO.setGeneralTaxpayerPicUrl(generalTaxpayerPicUrls.toArray(new String[generalTaxpayerPicUrls.size()]));
		}
	}

}
