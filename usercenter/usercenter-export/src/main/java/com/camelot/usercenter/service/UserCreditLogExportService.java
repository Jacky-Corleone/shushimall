package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserCreditLogDTO;

public interface UserCreditLogExportService {

	/**
	 * 
	 * <p>Discription:[根据积分id查询积分明细]</p>
	 * Created on 2015-4-14
	 * @param creditId
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<UserCreditLogDTO>> queryUserCreditLogList(Long creditId,Pager pager);
	
}
