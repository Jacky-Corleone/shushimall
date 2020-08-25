package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.enums.UserEnums.FieldIdentificationAuditStatus;

/**
 * 实地认证审核服务类
 * @author Klaus
 */
public interface FieldIdentificationAuditService {

	/**
	 * 新增审核
	 * @param auditDTO
	 * @return 执行结果信息类
	 */
	public ExecuteResult<String> addAudit(FieldIdentificationAuditDTO auditDTO);
	/**
	 * 根据用户ID查找审核DTO
	 * 董其超
	 * 2015年7月17日
	 */
	public FieldIdentificationAuditDTO queryAuditByUserId(Long UserId);
	/**
	 * 根据条件查询审核DTO
	 * 董其超
	 * 2015年7月17日
	 */
	public DataGrid<FieldIdentificationAuditDTO> findAuditListByCondition(FieldIdentificationAuditDTO auditDTO, FieldIdentificationAuditStatus status,Pager<FieldIdentificationAuditDTO> pager);
	/**
	 * 根据主键ID修改审核状态
	 * 董其超
	 * 2015年7月17日
	 */
	public ExecuteResult<String> modifyAuditStatus(Long id, String auditorId, String auditRemark, int auditStatus);

}
