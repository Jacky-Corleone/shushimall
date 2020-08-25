package com.camelot.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserGrowthDAO;
import com.camelot.usercenter.dto.UserCreditLogDTO;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.camelot.usercenter.service.UserGrowthService;

/**
 * <p>Description: [成长值记录的调用接口的实现类]</p>
 * Created on 2015-12-8
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("userGrowthService")
public class UserGrowthServiceImpl implements UserGrowthService{
	
    private final static Logger logger = LoggerFactory.getLogger(UserGrowthServiceImpl.class);

	@Resource
	private UserGrowthDAO userGrowthDAO;
	/*
	 * 添加成长值记录
	 */
	@Override
	public void addUserGrowthInfo(UserGrowthDTO userGrowthDTO) {
		logger.info("方法[{}]，入参：[{}][{}]", "UserGrowthServiceImpl-addUserGrowthInfo", JSONObject.toJSON(userGrowthDTO));
		userGrowthDAO.addUserGrowthInfo(userGrowthDTO);
	}
	
	/*
	 * 根据用户id查询成长值记录
	 */
	@Override
	public ExecuteResult<DataGrid<UserGrowthDTO>> selectList(long userId,Pager pager){
		ExecuteResult<DataGrid<UserGrowthDTO>> result=new ExecuteResult<DataGrid<UserGrowthDTO>>();
		try {
			DataGrid<UserGrowthDTO> dataGrid=new DataGrid<UserGrowthDTO>();
			UserGrowthDTO userGrowthDTO=new UserGrowthDTO();
			userGrowthDTO.setUserId((int)userId);
			 List<UserGrowthDTO> list = userGrowthDAO.selectList(userGrowthDTO,pager);
			 Long count = userGrowthDAO.selectCount(userGrowthDTO);
			 dataGrid.setRows(list);
			 dataGrid.setTotal(count);
			 result.setResult(dataGrid);
			 result.setResultMessage("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException();
		}
		return result;
	}

}
