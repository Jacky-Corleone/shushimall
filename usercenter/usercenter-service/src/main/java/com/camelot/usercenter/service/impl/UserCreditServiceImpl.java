package com.camelot.usercenter.service.impl;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dao.UserCreditDAO;
import com.camelot.usercenter.dao.UserCreditLogDAO;
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.usercenter.dto.UserCreditLogDTO;
import com.camelot.usercenter.dto.indto.UserCreditAddIn;
import com.camelot.usercenter.service.UserCreditExportService;

@Service("userCreditExportService")
public class UserCreditServiceImpl implements UserCreditExportService{

	private final static Logger logger = LoggerFactory.getLogger(UserCreditServiceImpl.class);
	@Resource
	UserCreditDAO userCreditDAO;
	@Resource
	UserCreditLogDAO userCreditLogDAO;
	@Override
	public ExecuteResult<UserCreditDTO> getUserCreditByUserId(Long userId) {
		ExecuteResult<UserCreditDTO> result=new ExecuteResult<UserCreditDTO>();
		
		try {
			UserCreditDTO userCreditDTO=new UserCreditDTO();
			userCreditDTO.setUserId(userId);
			UserCreditDTO userCredit = userCreditDAO.selectById(userId);
			result.setResult(userCredit);
			result.setResultMessage("success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException();
		}
		
		return result;
	}
	
	@Override
	public ExecuteResult<String> addUserCredit(UserCreditAddIn userCreditAddIn) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			UserCreditDTO userCredit = userCreditDAO.selectById(userCreditAddIn.getUserId());
			if(userCredit==null){
				//如果用户积分表为空 新增一条  在插入记录表
				UserCreditDTO ucd=new UserCreditDTO();
				ucd.setUserId(userCreditAddIn.getUserId());
				ucd.setCredit(userCreditAddIn.getCredit());
				userCreditDAO.insert(ucd);
				
				UserCreditLogDTO ucld=new UserCreditLogDTO();
				ucld.setCredit(userCreditAddIn.getCredit());
				ucld.setCreditId(ucd.getId());
				ucld.setSorceType(userCreditAddIn.getSorceType());
				ucld.setDescription(userCreditAddIn.getDescription());
				userCreditLogDAO.insert(ucld);
			}else{
				//如果用户积分表不为空  修改积分 添加记录表
				UserCreditDTO ucd=new UserCreditDTO();
				ucd.setUserId(userCreditAddIn.getUserId());
				ucd.setCredit(userCreditAddIn.getCredit()+userCredit.getCredit());
				userCreditDAO.update(ucd);
				
				UserCreditLogDTO ucld=new UserCreditLogDTO();
				ucld.setCredit(userCreditAddIn.getCredit());
				ucld.setCreditId(userCredit.getId());
				ucld.setSorceType(userCreditAddIn.getSorceType());
				ucld.setDescription(userCreditAddIn.getDescription());
				userCreditLogDAO.insert(ucld);
			}
			result.setResultMessage("success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException();
		}
		
		return result;
	}
	
	
	

}
