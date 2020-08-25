package com.camelot.usercenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;

/**
 * 卖家实地认证审核DAO
 * 董其超
 * 2015年7月17日
 */
public interface FieldIdentificationAuditMybatisDAO extends BaseDAO<FieldIdentificationAuditDTO> {

	/**
	 * 根据卖家Id查询审核DTO
	 * @param auditDTO
	 * @return 审核DTO
	 */
	public FieldIdentificationAuditDTO queryAuditByUserId(@Param("userId") Long userId);

	/**
	 * 新增审核
	 * @param auditDTO
	 * @return 执行结果信息类
	 */
	public int addAudit(FieldIdentificationAuditDTO dto);

	/**
	 * 根据主键ID修改审核状态
	 * 董其超
	 * 2015年7月17日
	 */
	public int modifyAuditStatus(@Param("id") Long id, @Param("auditorId") String auditorId, @Param("auditRemark") String auditRemark, @Param("auditStatus") int auditStatus);
}
