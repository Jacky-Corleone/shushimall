package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;



/**
 * 
 * <p>Description: [查询用户审核记录 ]</p>
 * Created on 2015-3-14
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserModifyInfoService {
	
	/**
	 * 
	 * <p>Discription:[查询商家 信息 修改记录]</p>
	 * Created on 2015-3-23
	 * @param userModifyInfoDTO
	 * @param pager
	 * @return
	 * @author:[liuqingshan]
	 */
	public DataGrid<UserModifyInfoDTO> getUserModifyInfoList(UserModifyInfoDTO userModifyInfoDTO,Pager pager); 
	
	public Integer updateModifyInfoSelective(UserModifyInfoDTO userModifyInfoDTO);
}
