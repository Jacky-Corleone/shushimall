package com.camelot.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserCreditLogDAO;
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.usercenter.dto.UserCreditLogDTO;
import com.camelot.usercenter.service.UserCreditLogExportService;
@Service("userCreditLogExportService")
public class UserCreditLogServiceImpl implements UserCreditLogExportService{
	private final static Logger logger = LoggerFactory.getLogger(UserCreditLogServiceImpl.class);
	@Resource
	UserCreditLogDAO userCreditLogDAO;
	@Override
	public ExecuteResult<DataGrid<UserCreditLogDTO>> queryUserCreditLogList(Long creditId, Pager pager) {
		ExecuteResult<DataGrid<UserCreditLogDTO>> result=new ExecuteResult<DataGrid<UserCreditLogDTO>>();
		
		try {
			DataGrid<UserCreditLogDTO> dataGrid=new DataGrid<UserCreditLogDTO>();
			UserCreditLogDTO userCreditLogDTO=new UserCreditLogDTO();
			userCreditLogDTO.setCreditId(creditId);
			 List<UserCreditLogDTO> list = userCreditLogDAO.selectList(userCreditLogDTO,pager);
			 Long count = userCreditLogDAO.selectCount(userCreditLogDTO);
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
