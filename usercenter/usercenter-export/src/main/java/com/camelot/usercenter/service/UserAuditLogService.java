package com.camelot.usercenter.service;
import java.util.Date;
import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
/**
 * <p>业务逻辑接口</p>
 *  
 *  @author
 *  @createDate 
 **/
public interface UserAuditLogService {
	
	/**
	 * 添加
	 *
	 * @param userAuditLog
	 * @return 
	 */
	public ExecuteResult<UserAuditLogDTO> saveUserAuditLogDTO(UserAuditLogDTO userAuditLog);
	
	/**
	 * 根据ID查询
	 *
	 * @param id
	 * @return 
	 */
	public UserAuditLogDTO findUserAuditLogDTOById(long id);
	
	/**
	 * 根据条件查询信息
	 *
	 * @param pager
	 * @param userAuditLog
	 * @return 
	 */
	public DataGrid<UserAuditLogDTO> findListByCondition(Pager pager, UserAuditLogDTO userAuditLog);
	
	/**
	 * 根据ID修改,空值不参与修改
	 *
	 * @param item
	 * @return 
	 */
	public ExecuteResult<UserAuditLogDTO> alterById(UserAuditLogDTO userAuditLog);
	
	/**
	 * 根据ID删除
	 *
	 * @param idList
	 * @return 
	 */
	public ExecuteResult<UserAuditLogDTO> removeById(long id);
	
	public ExecuteResult<UserAuditLogDTO> createUserAuditLogDTO(UserAuditLogDTO userAuditLog);
	public ExecuteResult<UserAuditLogDTO> updateUserAuditLogDTO(UserAuditLogDTO userAuditLog);
	public ExecuteResult<UserAuditLogDTO> deleteById(Long userAuditLogId);
	public ExecuteResult<UserAuditLogDTO> deleteAll(List<Long> idList);
	public long searchResultsCount(UserAuditLogDTO userAuditLog);
	public UserAuditLogDTO getUserAuditLogDTOById(Long userAuditLogId);
	 
	public ExecuteResult<UserAuditLogDTO> updateSelective(UserAuditLogDTO userAuditLog);
	public ExecuteResult<UserAuditLogDTO> updateSelectiveWithDateTimeCheck(UserAuditLogDTO userAuditLog,Date prevUpdDt) throws CommonCoreException;
	public ExecuteResult<UserAuditLogDTO> updateAllWithDateTimeCheck(UserAuditLogDTO userAuditLog, Date prevUpdDt) throws CommonCoreException;
	public List<UserAuditLogDTO> searchByCondition(UserAuditLogDTO userAuditLog);
	public DataGrid<UserAuditLogDTO> searchByConditionByPager(Pager<UserAuditLogDTO> pager,UserAuditLogDTO userAuditLog);
	public long searchByConditionCount(UserAuditLogDTO userAuditLog);
	public ExecuteResult<UserAuditLogDTO> updateSelectiveByIdList(UserAuditLogDTO userAuditLog,List<Long> idList);
	public ExecuteResult<UserAuditLogDTO> updateAllByIdList(UserAuditLogDTO userAuditLog,List<Long> idList);
	public ExecuteResult<UserAuditLogDTO> defunctById(Long userAuditLogId);
	public ExecuteResult<UserAuditLogDTO> defunctByIdList(List<Long> idList);
	
}
