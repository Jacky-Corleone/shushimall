package com.camelot.usercenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserIntegralTrajectoryDAO;
import com.camelot.usercenter.domain.UserIntegralTrajectory;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;

/**
 * 
 * <p>Description: [用户积分信息]</p>
 * Created on 2015-12-7
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("userIntegralTrajectoryService")
public class UserIntegralTrajectoryService implements
		UserIntegralTrajectoryExportService {
	private final static Logger logger = LoggerFactory.getLogger(UserIntegralTrajectoryService.class);
	@Resource
	private UserIntegralTrajectoryDAO userIntegralTrajectoryDAO;
	@Override
	public ExecuteResult<UserIntegralTrajectoryDTO> addUserIntegralTrajectoryDTO(
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryService-addCentralPurchasingActivityDTO",JSON.toJSONString(userIntegralTrajectoryDTO));
		ExecuteResult<UserIntegralTrajectoryDTO> er = new ExecuteResult<UserIntegralTrajectoryDTO>();
		UserIntegralTrajectory userTrajectory = new UserIntegralTrajectory();
		BeanUtils.copyProperties(userIntegralTrajectoryDTO, userTrajectory);
		try {
			userIntegralTrajectoryDAO.insert(userTrajectory);
			userIntegralTrajectoryDTO.setId(userTrajectory.getId());
			er.setResult(userIntegralTrajectoryDTO);
		} catch (Exception e) {
			logger.error("UserIntegralTrajectoryService-addCentralPurchasingActivityDTO",e);
			er.addErrorMessage("保存用户积分信息失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> updateUserIntegralTrajectoryDTO(
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryService-updateCentralPurchasingActivityDTO",JSON.toJSONString(userIntegralTrajectoryDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		UserIntegralTrajectory userTrajectory = new UserIntegralTrajectory();
		BeanUtils.copyProperties(userIntegralTrajectoryDTO, userTrajectory);
		try {
			userIntegralTrajectoryDAO.update(userTrajectory);
		} catch (Exception e) {
			logger.error("UserIntegralTrajectoryService-addCentralPurchasingActivityDTO",e);
			er.addErrorMessage("更新用户积分信息失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> queryUserIntegralTrajectoryDTO(
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryService-queryCentralPurchasingActivityDTO",JSON.toJSONString(userIntegralTrajectoryDTO));
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = new ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>>();
		DataGrid<UserIntegralTrajectoryDTO> dg = new DataGrid<UserIntegralTrajectoryDTO>();
		UserIntegralTrajectory userTrajectory = new UserIntegralTrajectory();
		BeanUtils.copyProperties(userIntegralTrajectoryDTO, userTrajectory);
		List<UserIntegralTrajectory> list;
		try {
			list = userIntegralTrajectoryDAO.selectList(userTrajectory, page);
			long count = userIntegralTrajectoryDAO.selectCount(userTrajectory);
			List<UserIntegralTrajectoryDTO> backDTOList = new ArrayList<UserIntegralTrajectoryDTO>();
			for(UserIntegralTrajectory back:list){
				UserIntegralTrajectoryDTO backDTO = new UserIntegralTrajectoryDTO();
				BeanUtils.copyProperties(back, backDTO);
				backDTOList.add(backDTO);
			}
			dg.setRows(backDTOList);
			dg.setTotal(count);
		} catch (Exception e) {
			logger.error("UserIntegralTrajectoryService-queryCentralPurchasingActivityDTO",e);
			er.addErrorMessage("查询用户积分信息失败！");
		}
		er.setResult(dg);
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> queryUserIntegralByType(
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryService-queryUserIntegralByType",JSON.toJSONString(userIntegralTrajectoryDTO));
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = new ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>>();
		DataGrid<UserIntegralTrajectoryDTO> dg = new DataGrid<UserIntegralTrajectoryDTO>();
		UserIntegralTrajectory userTrajectory = new UserIntegralTrajectory();
		BeanUtils.copyProperties(userIntegralTrajectoryDTO, userTrajectory);
		List<UserIntegralTrajectory> list;
		try {
			list = userIntegralTrajectoryDAO.selectByType(userTrajectory, page);
			long count = userIntegralTrajectoryDAO.selectByTypeCount(userTrajectory);
			List<UserIntegralTrajectoryDTO> backDTOList = new ArrayList<UserIntegralTrajectoryDTO>();
			for(UserIntegralTrajectory back:list){
				UserIntegralTrajectoryDTO backDTO = new UserIntegralTrajectoryDTO();
				BeanUtils.copyProperties(back, backDTO);
				backDTOList.add(backDTO);
			}
			dg.setRows(backDTOList);
			dg.setTotal(count);
		} catch (Exception e) {
			logger.error("UserIntegralTrajectoryService-queryUserIntegralByType",e);
			er.addErrorMessage("查询用户积分信息失败！");
		}
		er.setResult(dg);
		return er;
	}

	@Override
	public ExecuteResult<Long> queryTotalIntegral(Long userId) {
		logger.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryService-queryTotalIntegral",JSON.toJSONString(userId));
		ExecuteResult<Long> er = new ExecuteResult<Long>();
		try {
			Long count = userIntegralTrajectoryDAO.selectTotalIntegral(userId);
			er.setResult(count);
		} catch (Exception e) {
			logger.error("UserIntegralTrajectoryService-queryTotalIntegral",e);
			er.addErrorMessage("查询用户总积分失败！");
		}
		return er;
	}

}
