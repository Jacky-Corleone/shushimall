package com.camelot.usercenter.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.usercenter.dto.indto.UserCreditAddIn;

public interface UserCreditExportService {
	
	
	/**
	 * 
	 * <p>Discription:[根据用户查询积分]</p>
	 * Created on 2015-4-14
	 * @param userId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<UserCreditDTO> getUserCreditByUserId(Long userId);

	/**
	 * 
	 * <p>Discription:[用户积分修改]</p>
	 * Created on 2015-4-14
	 * @param userCreditAddIn
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addUserCredit(UserCreditAddIn userCreditAddIn);
}
