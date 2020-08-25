package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;
/**
 * 
 * <p>Description: [商家信息审核]</p>
 * Created on 2015-3-10
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserApplyAuditService {
	
	/**
	 * 
	 * <p>Discription:[根据条件 查询 商家 修改信心审核列表   applicantName 商家名称  applicantUserid 商家编号  applyStatus 审核状态]</p>
	 * Created on 2015-3-10
	 * @param companyName 商家名称
	 * @param shopId	商家编码
	 * @param pager
	 * @return
	 * @author:[liuqingshan]
	 */
		
	public DataGrid<UserModifyInfoDTO> findUserAuditListByCondition(UserModifyInfoDTO dto, Pager pager);
	
	 /**
	  * 
	  * <p>Discription:[根据商家申请修改的主表  查询申请明细信息 默认传UserModifyInfoDTO ID的值]</p>
	  * Created on 2015-3-10
	  * @param userModifyId
	  * @return
	  * @author:[liuqingshan]
	  */
	public DataGrid<UserModifyDetailDTO> findUserModifyDetailByCondition(UserModifyInfoDTO userModifyInfoDTO,Pager pager);
	
	/**
	 * 
	 * <p>Discription:[商家修改信息接口 传入参UserModifyInfoId 审核人ID 审核状态  审核理由]</p>
	 * Created on 2015-3-10
	 * @param userModifyId
	 * @param status
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<Integer> moifyUserApplyAudit(String UserModifyInfoId,String auditUserId, ComStatus status,String remark);
	
	

}
