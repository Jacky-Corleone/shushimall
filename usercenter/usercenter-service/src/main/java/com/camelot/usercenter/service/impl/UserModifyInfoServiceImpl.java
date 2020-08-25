package com.camelot.usercenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserModifyInfoMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.service.UserModifyInfoService;

@Service("userModifyInfoService")
public class UserModifyInfoServiceImpl implements UserModifyInfoService{
	private final static Logger logger = LoggerFactory.getLogger(UserModifyInfoService.class);
	@Resource
	UserModifyInfoMybatisDAO userModifyInfoMybatisDAO;
	
	@SuppressWarnings("rawtypes") 
	@Override
	public DataGrid<UserModifyInfoDTO> getUserModifyInfoList(UserModifyInfoDTO userModifyInfoDTO,Pager pager) {
		
		DataGrid<UserModifyInfoDTO>  res=new DataGrid<UserModifyInfoDTO>();
		List<UserModifyInfoDTO> userModlifyList= userModifyInfoMybatisDAO.selectList(userModifyInfoDTO, pager);
		Long count=userModifyInfoMybatisDAO.selectCount(userModifyInfoDTO);
		res.setRows(userModlifyList);
		res.setTotal(count);
		logger.debug("getUserModifyInfoList " +res);
		return res;
	}

	@Override
	public Integer updateModifyInfoSelective(UserModifyInfoDTO userModifyInfoDTO) {
		
		return userModifyInfoMybatisDAO.update(userModifyInfoDTO);
	}
	
	
}
