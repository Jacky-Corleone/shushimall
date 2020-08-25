package com.camelot.maketcenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dao.CentralPurchasingRefOrderDAO;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.service.CentralPurchasingRefOrderExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>
 * Description: [集采订单]
 * </p>
 * Created on 2015年12月17日
 * 
 * @author <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("centralPurchasingRefOrderExportService")
public class CentralPurchasingRefOrderExportServiceImpl implements CentralPurchasingRefOrderExportService {
	private final static Logger logger = LoggerFactory.getLogger(CentralPurchasingRefOrderExportServiceImpl.class);
	@Resource
	private CentralPurchasingRefOrderDAO centralPurchasingRefOrderDAO;

	@Override
	public ExecuteResult<Boolean> addCentralPurchasingRefOrder(CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingRefOrderExportServiceImpl-addCentralPurchasingRefOrder",JSON.toJSONString(centralPurchasingRefOrderDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			centralPurchasingRefOrderDAO.add(centralPurchasingRefOrderDTO);
		} catch(Exception e) {
			logger.error("CentralPurchasingRefOrderExportServiceImpl-addCentralPurchasingRefOrder",e);
			er.addErrorMessage("查询集采订单失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> queryCentralPurchasingRefOrder(
			CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingRefOrderExportServiceImpl-queryCentralPurchasingRefOrder",JSON.toJSONString(centralPurchasingRefOrderDTO));
		ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> er = new ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>>();
		try {
			DataGrid<CentralPurchasingRefOrderDTO> resultPager = new DataGrid<CentralPurchasingRefOrderDTO>();
			List<CentralPurchasingRefOrderDTO> centralPurchasingRefOrderDTOs = centralPurchasingRefOrderDAO.queryList(centralPurchasingRefOrderDTO, page);
			long size = centralPurchasingRefOrderDAO.queryCount(centralPurchasingRefOrderDTO);
			resultPager.setRows(centralPurchasingRefOrderDTOs);
			resultPager.setTotal(size);
			er.setResult(resultPager);
		} catch(Exception e) {
			logger.error("CentralPurchasingRefOrderExportServiceImpl-queryCentralPurchasingRefOrder",e);
			er.addErrorMessage("查询集采订单失败！");
		}
		return er;
	}

}
