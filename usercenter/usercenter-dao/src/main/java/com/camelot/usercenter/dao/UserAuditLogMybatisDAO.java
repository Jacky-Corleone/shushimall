package com.camelot.usercenter.dao;

import java.util.Date;
import java.util.List;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface UserAuditLogMybatisDAO  extends BaseDAO<UserAuditLogDTO>{
	public List<UserAuditLogDTO> getAll();
	public List<UserAuditLogDTO> searchUserAuditLogs(@Param("pager") Pager<UserAuditLogDTO> pager, @Param("userAuditLogDTO") UserAuditLogDTO userAuditLog);
	public Long searchUserAuditLogDTOsCount(@Param("userAuditLogDTO") UserAuditLogDTO userAuditLog);
	public void deleteAll(@Param("idList")List<Long> idList);
	
	public int updateSelective(@Param("userAuditLog") UserAuditLogDTO userAuditLog);
	public int updateAllWithDateTimeCheck(@Param("userAuditLogDTO") UserAuditLogDTO userAuditLog,@Param("prevUpdDt")Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("userAuditLogDTO") UserAuditLogDTO userAuditLog,@Param("prevUpdDt")Date prevUpdDt);
	public List<UserAuditLogDTO> searchByCondition(@Param("entity") UserAuditLogDTO userAuditLog);


	public long updateSelectiveByIdList(@Param("userAuditLogDTO") UserAuditLogDTO userAuditLog,@Param("idList")List<Long> idList);
	public long updateAllByIdList(@Param("userAuditLogDTO") UserAuditLogDTO userAuditLog,@Param("idList")List<Long> idList);
}