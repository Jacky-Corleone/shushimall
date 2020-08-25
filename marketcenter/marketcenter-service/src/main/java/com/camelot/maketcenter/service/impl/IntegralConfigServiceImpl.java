package com.camelot.maketcenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.centralPurchasing.domain.IntegralConfig;
import com.camelot.maketcenter.dao.IntegralConfigDAO;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
@Service("integralConfigService")
public class IntegralConfigServiceImpl implements IntegralConfigExportService {
	private final static Logger logger = LoggerFactory.getLogger(IntegralConfigServiceImpl.class);
	@Resource
	private IntegralConfigDAO integralConfigDAO;
	@Override
	public ExecuteResult<Boolean> addIntegralConfigDTO(
			List<IntegralConfigDTO> integralConfigDTOs) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-addIntegralConfigDTO",JSON.toJSONString(integralConfigDTOs));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try{
			for (int i = 0; i < integralConfigDTOs.size(); i++) {
				IntegralConfig integralConfig = new IntegralConfig();
				BeanUtils.copyProperties(integralConfigDTOs.get(i), integralConfig);
				integralConfigDAO.add(integralConfig);
			}
			}catch(Exception e){
				logger.error("updateIntegralConfigDTO",e);
				er.addErrorMessage("添加积分配置失败");
			}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> updateIntegralConfigDTO(
			List<IntegralConfigDTO> integralConfigDTOs) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-updateIntegralConfigDTO",JSON.toJSONString(integralConfigDTOs));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try{
		for (int i = 0; i < integralConfigDTOs.size(); i++) {
			IntegralConfig integralConfig = new IntegralConfig();
			BeanUtils.copyProperties(integralConfigDTOs.get(i), integralConfig);
			integralConfigDAO.update(integralConfig);
		}
		}catch(Exception e){
			logger.error("updateIntegralConfigDTO",e);
			er.addErrorMessage("更新积分配置失败");
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<IntegralConfigDTO>> queryIntegralConfigDTO(
			IntegralConfigDTO integralConfigDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-queryIntegralConfigDTO",JSON.toJSONString(integralConfigDTO));
		ExecuteResult<DataGrid<IntegralConfigDTO>> er = new ExecuteResult<DataGrid<IntegralConfigDTO>>();
		DataGrid<IntegralConfigDTO> dg = new DataGrid<IntegralConfigDTO>();
		IntegralConfig integralConfig = new IntegralConfig();
		List<IntegralConfigDTO> backList = new ArrayList<IntegralConfigDTO>();
		BeanUtils.copyProperties(integralConfigDTO, integralConfig);
		try {
			List<IntegralConfig> list = integralConfigDAO.queryList(integralConfig, page);
			Long count = integralConfigDAO.queryCount(integralConfig);
			for(IntegralConfig config:list){
				IntegralConfigDTO backDTO = new IntegralConfigDTO();
				BeanUtils.copyProperties(config, backDTO);
				backList.add(backDTO);
			}
			dg.setRows(backList);
			dg.setTotal(count);
			er.setResult(dg);
		} catch (Exception e) {
			logger.error("updateIntegralConfigDTO",e);
			er.addErrorMessage("查询积分配置失败");
		}
		
		return er;
	}
	@Override
	public ExecuteResult<DataGrid<IntegralConfigDTO>> queryOneType(
			IntegralConfigDTO integralConfigDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-queryIntegralConfigDTO",JSON.toJSONString(integralConfigDTO));
		ExecuteResult<DataGrid<IntegralConfigDTO>> er = new ExecuteResult<DataGrid<IntegralConfigDTO>>();
		DataGrid<IntegralConfigDTO> dg = new DataGrid<IntegralConfigDTO>();
		IntegralConfig integralConfig = new IntegralConfig();
		List<IntegralConfigDTO> backList = new ArrayList<IntegralConfigDTO>();
		BeanUtils.copyProperties(integralConfigDTO, integralConfig);
		try {
			List<IntegralConfig> list = integralConfigDAO.queryOneType(integralConfig, page);
			Long count = integralConfigDAO.queryCount(integralConfig);
			for(IntegralConfig config:list){
				IntegralConfigDTO backDTO = new IntegralConfigDTO();
				BeanUtils.copyProperties(config, backDTO);
				backList.add(backDTO);
			}
			dg.setRows(backList);
			dg.setTotal(count);
			er.setResult(dg);
		} catch (Exception e) {
			logger.error("updateIntegralConfigDTO",e);
			er.addErrorMessage("查询积分配置失败");
		}
		
		return er;
	}

	@Override
	public IntegralConfigDTO queryIntegralConfigDTOById(Long id) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-queryIntegralConfigDTOById",JSON.toJSONString(id));
		IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
		IntegralConfig integralConfig = integralConfigDAO.queryById(id);
		if(null == integralConfig){
			return null;
		}else{
			BeanUtils.copyProperties(integralConfig, integralConfigDTO);
			return integralConfigDTO;
		}
	}

	@Override
	public ExecuteResult<List<IntegralConfigDTO>> queryIntegralConfigDTOByMoney(
			BigDecimal money,Long platformId) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-queryIntegralConfigDTOByMoney",JSON.toJSONString(money));
		ExecuteResult<List<IntegralConfigDTO>> er = new ExecuteResult<List<IntegralConfigDTO>>();
		List<IntegralConfigDTO> backList = new ArrayList<IntegralConfigDTO>();
		try {
			List<IntegralConfig> list = integralConfigDAO.queryByMoney(money,platformId);
			for(IntegralConfig integralConfig:list){
				IntegralConfigDTO backDTO = new IntegralConfigDTO();
				BeanUtils.copyProperties(integralConfig, backDTO);
				backList.add(backDTO);
			}
		} catch (Exception e) {
			logger.error("queryIntegralConfigDTOByMoney",e);
			er.addErrorMessage("查询失败");
		}
		er.setResult(backList);
		return er;
	}
	
	@Override
	public ExecuteResult<Integer> deleteByType(IntegralConfigDTO integralConfigDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","IntegralConfigServiceImpl-queryIntegralConfigDTOByMoney",JSON.toJSONString(integralConfigDTO));
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		Integer result =null;
		IntegralConfig integralConfig = new IntegralConfig();
		BeanUtils.copyProperties(integralConfigDTO, integralConfig);
		try {
			result = integralConfigDAO.deleteByType(integralConfig);
		} catch (Exception e) {
			logger.error("queryIntegralConfigDTOByMoney",e);
			er.addErrorMessage("查询失败");
		}
		er.setResult(result);
		return er;
	}

}
