package com.camelot.tradecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dao.TradeOrderItemsPackageDAO;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.service.TradeOrderItemsPackageExportService;

/**
 * 
 * <p>Description: [套装商品订单记录服务接口]</p>
 * Created on 2016年2月25日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Service("tradeOrderItemsPackageExportService")
public class TradeOrderItemsPackageExportServiceImpl implements TradeOrderItemsPackageExportService {

    private static final Logger logger = LoggerFactory.getLogger(TradeOrderItemsPackageExportServiceImpl.class);

    @Resource
    private TradeOrderItemsPackageDAO tradeOrderItemsPackageDAO;

    public ExecuteResult<TradeOrderCreateDTO> createOrder(TradeOrderCreateDTO dto) {
    	logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderExportServiceImpl-createOrder", JSONObject.toJSONString(dto));
        logger.info("==============开始创建订单===================");
        ExecuteResult<TradeOrderCreateDTO> result = new ExecuteResult<TradeOrderCreateDTO>();
        try {
            
            result.setResult(dto);
            
        } catch (Exception e) {
            logger.error("执行方法【createOrder】报错：{}", e);
            result.addErrorMessage("执行方法【createOrder】报错：" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            logger.info("==============结束创建订单===================");
        }
        return result;
    }

	@Override
	public ExecuteResult<String> add(TradeOrderItemsPackageDTO dto) {
		logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderItemsPackageExportServiceImpl-add", JSONObject.toJSONString(dto));
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			tradeOrderItemsPackageDAO.add(dto);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeOrderItemsPackageExportServiceImpl-add", e);
            throw new RuntimeException(e);
        }
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> queryTradeOrderItemsPackageDTOs(
			TradeOrderItemsPackageDTO inDTO, Pager<TradeOrderItemsPackageDTO> pager) {
		logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderItemsPackageExportServiceImpl-queryTradeOrderItemsPackageDTOs", JSONObject.toJSONString(inDTO));
		ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> result = new ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>>();
		try{
			DataGrid<TradeOrderItemsPackageDTO> resultPager = new DataGrid<TradeOrderItemsPackageDTO>();
			List<TradeOrderItemsPackageDTO> itemsPackageDTOs = tradeOrderItemsPackageDAO.queryList(inDTO, pager);
			long size = tradeOrderItemsPackageDAO.queryCount(inDTO);
			resultPager.setRows(itemsPackageDTOs);
			resultPager.setTotal(size);
			result.setResult(resultPager);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeOrderItemsPackageExportServiceImpl-queryTradeOrderItemsPackageDTOs", e);
            throw new RuntimeException(e);
        }
		return result;
	}

	@Override
	public ExecuteResult<TradeOrderItemsPackageDTO> queryById(Long id) {
		logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderItemsPackageExportServiceImpl-queryById", id);
		ExecuteResult<TradeOrderItemsPackageDTO> result = new ExecuteResult<TradeOrderItemsPackageDTO>();
		try{
			TradeOrderItemsPackageDTO dto = tradeOrderItemsPackageDAO.queryById(id);
			result.setResult(dto);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeOrderItemsPackageExportServiceImpl-queryById", e);
            throw new RuntimeException(e);
        }
		return result;
	}
}
