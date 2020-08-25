package com.camelot.activity;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.activity.dao.ActivityRecordDao;
import com.camelot.activity.dao.ActivityStatementsDao;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [优惠活动结算单实现方法]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("activityStatementSerice")
public class ActivityStatementServiceImpl implements ActivityStatementSerice{
	private final static Logger logger = LoggerFactory.getLogger(ActivityStatementServiceImpl.class);
	@Resource
	private ActivityRecordDao activityRecordDao;
	
	@Resource
	private ActivityStatementsDao activityStatementsDao;
	
	@Override
	public ExecuteResult<String> addActivityStatement(ActivityStatementsDTO dto) {
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-addActivityStatement",JSON.toJSONString(dto));
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			activityStatementsDao.add(dto);
			result.setResult("");
            result.setResultMessage("success");
		}catch(Exception e){
			logger.error("ActivityStatementServiceImpl-addActivityStatement",e);
			result.addErrorMessage("添加优惠活动结算单失败！");
			
		}
		return result;
	}

	@Override
	public ExecuteResult<String> updateActivityStatement(
			ActivityStatementsDTO dto) {
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-updateActivityStatement",JSON.toJSONString(dto));
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			activityStatementsDao.update(dto);
			result.setResult("");
            result.setResultMessage("success");
		}catch (Exception e) {
			logger.error("ActivityStatementServiceImpl-addActivityStatement",e);
			result.addErrorMessage("更新优惠活动结算单失败！");
		}
		return result;
	}

	@Override
	public ExecuteResult<ActivityStatementsDTO> queryActivityStatementsByOrderId(
			String orderId) {
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-queryActivityStatementsByOrderId",JSON.toJSONString(orderId));
		ExecuteResult<ActivityStatementsDTO> result = new ExecuteResult<ActivityStatementsDTO>();
		try{
			if(StringUtils.isEmpty(orderId)){
				result.addErrorMessage("订单id不能为空。");
				return result;
			}
			ActivityStatementsDTO activityStatementsDTO=activityStatementsDao.queryByOrderId(orderId);
			result.setResult(activityStatementsDTO);
            result.setResultMessage("success");
		}catch (Exception e) {
			logger.error("ActivityStatementServiceImpl-queryActivityStatementsByOrderId",e);
			result.addErrorMessage("根据订单id查询优惠活动结算单失败！");
		}
		return result;
	}

	public ExecuteResult<DataGrid<ActivityStatementsDTO>> queryActivityStatementsDTO(ActivityStatementsDTO dto,Pager page){
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-queryActivityStatementsDTO",JSON.toJSONString(dto));
		ExecuteResult<DataGrid<ActivityStatementsDTO>> result = new ExecuteResult<DataGrid<ActivityStatementsDTO>>();
		try {
			DataGrid<ActivityStatementsDTO> dataGrid = new DataGrid<ActivityStatementsDTO>();
			List<ActivityStatementsDTO> list=activityStatementsDao.queryList(dto, page);
			if (list != null && list.size() > 0) {
				for (ActivityStatementsDTO statementsDTO : list) {
					BigDecimal totalDiscountAmount = statementsDTO.getTotalDiscountAmount();
					BigDecimal totalRefundAmount = statementsDTO.getTotalRefundAmount();
					if (totalDiscountAmount == null) {
						totalDiscountAmount = BigDecimal.ZERO;
					}
					if (totalRefundAmount == null) {
						totalRefundAmount = BigDecimal.ZERO;
					}
					statementsDTO.setTotalSettleAmount(totalDiscountAmount.subtract(totalRefundAmount));
				}
			}
			Long count=activityStatementsDao.queryCount(dto);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		}catch(Exception e){
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	@Override
	public ExecuteResult<String> addActivityRecord(ActivityRecordDTO dto) {
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-addActivityRecord",JSON.toJSONString(dto));
		ExecuteResult<String> result = new ExecuteResult<String>();
		try{
			activityRecordDao.add(dto);
			result.setResult("");
            result.setResultMessage("success");
		}catch (Exception e) {
			logger.error("ActivityStatementServiceImpl-addActivityRecord",e);
			result.addErrorMessage("增加活动记录失败！");
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ActivityRecordDTO>> queryActivityRecordDTO(
			ActivityRecordDTO dto, Pager page) {
		
		logger.info("\n 方法[{}]，入参：[{}]","ActivityStatementServiceImpl-queryActivityRecordDTO",JSON.toJSONString(dto));
		ExecuteResult<DataGrid<ActivityRecordDTO>> result = new ExecuteResult<DataGrid<ActivityRecordDTO>>();
		try {
			DataGrid<ActivityRecordDTO> dataGrid = new DataGrid<ActivityRecordDTO>();
			List<ActivityRecordDTO> list=activityRecordDao.queryList(dto, page);
			Long count=activityRecordDao.queryCount(dto);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		}catch(Exception e){
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
}
