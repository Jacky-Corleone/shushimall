package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;


/**
 * 供客户端调用的远程接口
 * 
 * @author
 * 
 */
public interface UserModifyDetailService {
	/**
	 * 
	 * <p>Discription:[根据输入条件 分页查询 用户申请修改明细]</p>
	 * Created on 2015-3-14
	 * @param userModifyDetailDTO
	 * @param pager
	 * @return
	 * @author:[liuqingshan]
	 */
	public DataGrid<UserModifyDetailDTO> getUserModifyDetailList(UserModifyDetailDTO userModifyDetailDTO,Pager pager); 
	
	/**
	 * 
	 * <p>Discription:[根据 modifyinfoId查询修改的明细 并修改每个业务表 传入参数modifyID 审核理由 审核人]</p>
	 * Created on 2015-3-14
	 * @param modifyInfoId
	 * @return
	 * @author:[liuqingshan]
	 */
	public boolean updateModfiyAllTablesAndCoumn(Long modifyInfoId,String remark,String reviewUserId);
	
	
	
}
