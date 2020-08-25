package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserEnums.UserType;

import java.util.List;

public interface UserCenterOperaService {
	
	/**
	 * 
	 * <p>Discription:[插入 商家修改信息审核记录]</p>
	 * Created on 2015-3-20
	 * @param before
	 * @param after
	 * @param userModifyInfoId
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<String> insertModifyDetailByUserInfo(UserInfoDTO before,UserInfoDTO after,UserModifyInfoDTO modify);

    /**
     *  新增 用户修改明细记录
     * @param before
     * @param after
     * @param modify
     * @return
     */
    public ExecuteResult<String> insertModifyDetailByCompanyInfo(UserCompanyDTO before,UserCompanyDTO after,UserModifyInfoDTO modify);


    /**
     * 新增用户操作审核 申请
     * @param userAuditLog
     * @return
     */
	public ExecuteResult<UserAuditLogDTO> saveUserAuditLogDTO(UserAuditLogDTO userAuditLog,UserEnums.UserOperaType userOperaType);

    /**
     * 根据 条件查询用户操作审核信息
     * @param pager
     * @param userAuditLog
     * @return
     */
    public List<UserAuditLogDTO> findListByCondition( UserAuditLogDTO userAuditLog,UserEnums.UserOperaType userOperaType);

    /**
     * 子帐号申请 父账号 互换操作
     * @param sonUserId
     * @return
     */

    public ExecuteResult<UserDTO> changeParentAndSonAccount(UserDTO userDTO);



}
