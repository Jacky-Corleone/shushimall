package com.camelot.usercenter.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.camelot.common.constant.GlobalConstant;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.usercenter.dao.UserAuditLogMybatisDAO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.service.UserAuditLogService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.openplatform.util.DateTimeUtil;
import com.camelot.openplatform.util.ExceptionUtils;
import com.camelot.openplatform.util.GeneralMsgCode;
/**
 * <p>
 * 业务逻辑实现类
 * </p>
 * 
 * @author
 * @createDate 
 **/
@Service("userAuditLogService")
public class UserAuditLogServiceImpl implements UserAuditLogService{
//	private final static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	@Resource
	private UserAuditLogMybatisDAO userAuditLogMybatisDAO;
	
	@Override
	public ExecuteResult<UserAuditLogDTO> saveUserAuditLogDTO(UserAuditLogDTO userAuditLog){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		userAuditLog.setCreateDt(new Date());
		userAuditLog.setLastUpdDt(new Date());
		userAuditLog.setDeletedFlag("0");
		
		userAuditLogMybatisDAO.insert(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	
	@Override
	public UserAuditLogDTO findUserAuditLogDTOById(long id){
		return userAuditLogMybatisDAO.selectById(id);
	}
	
	
	
	@Override
	public ExecuteResult<UserAuditLogDTO> alterById(UserAuditLogDTO userAuditLog){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		userAuditLogMybatisDAO.update(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	
	@Override
	public ExecuteResult<UserAuditLogDTO> removeById(long id) {
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		userAuditLogMybatisDAO.delete(id);
		return executeResult;
	}
	
	@Override
	public ExecuteResult<UserAuditLogDTO> createUserAuditLogDTO(UserAuditLogDTO userAuditLog){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		Date 	curDate = DateTimeUtil.getCurrentDateByTimeZone();
		
		userAuditLogMybatisDAO.insert(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> updateUserAuditLogDTO(UserAuditLogDTO userAuditLog){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		setLastUpdInfo(userAuditLog);
		userAuditLogMybatisDAO.update(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> deleteById(Long userAuditLogId){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		
		userAuditLogMybatisDAO.delete(userAuditLogId);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> deleteAll(List<Long> idList){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		userAuditLogMybatisDAO.deleteAll(idList);
		return executeResult;
	}
	
	@Override
	public long searchResultsCount(UserAuditLogDTO userAuditLog){
		long size = userAuditLogMybatisDAO.searchUserAuditLogDTOsCount(userAuditLog);
		return size;
	}
	@Override
	public UserAuditLogDTO getUserAuditLogDTOById(Long userAuditLogId){
		return userAuditLogMybatisDAO.selectById(userAuditLogId);
	}
	
	
	@Override
	public ExecuteResult<UserAuditLogDTO> updateSelective(UserAuditLogDTO userAuditLog){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		setLastUpdInfo(userAuditLog);
		userAuditLogMybatisDAO.updateSelective(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> updateSelectiveWithDateTimeCheck(UserAuditLogDTO userAuditLog,	Date prevUpdDt) throws CommonCoreException{
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		setLastUpdInfo(userAuditLog);
		int resInt=userAuditLogMybatisDAO.updateSelectiveWithDateTimeCheck(userAuditLog,prevUpdDt);
		if (0 == resInt) {
 			ExceptionUtils.throwCoreException(GeneralMsgCode.ILLEGAL_ARGUMENT, null, null, null);
 		}
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> updateAllWithDateTimeCheck(UserAuditLogDTO userAuditLog,Date prevUpdDt) throws CommonCoreException{
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		setLastUpdInfo(userAuditLog);
		int resInt=userAuditLogMybatisDAO.updateAllWithDateTimeCheck(userAuditLog,prevUpdDt);
 		if (0 == resInt) {
 			ExceptionUtils.throwCoreException(GeneralMsgCode.DATA_EXPIRED_ERROR, null, null, null);
 		}
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public List<UserAuditLogDTO> searchByCondition(UserAuditLogDTO userAuditLog){
		userAuditLog.setDeletedFlag(GlobalConstant.DEFUNCT_IND_FALSE_STRING);
		return userAuditLogMybatisDAO.searchByCondition(userAuditLog);
	}
	
	@Override
	public ExecuteResult<UserAuditLogDTO> updateSelectiveByIdList(UserAuditLogDTO userAuditLog,List<Long> idList){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		if(idList!=null&&idList.size()>0&& userAuditLog!=null){
			setLastUpdInfo(userAuditLog);
			userAuditLogMybatisDAO.updateSelectiveByIdList(userAuditLog,idList);
		}
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> updateAllByIdList(UserAuditLogDTO userAuditLog,List<Long> idList){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		if(idList!=null&&idList.size()>0&& userAuditLog!=null){
			setLastUpdInfo(userAuditLog);
			userAuditLogMybatisDAO.updateAllByIdList(userAuditLog,idList);
		}
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> defunctById(Long userAuditLogId){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		UserAuditLogDTO userAuditLog=new UserAuditLogDTO();
		userAuditLog.setDeletedFlag(GlobalConstant.DEFUNCT_IND_TRUE_STRING);
		userAuditLog.setId(userAuditLogId);
		userAuditLogMybatisDAO.updateSelective(userAuditLog);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	@Override
	public ExecuteResult<UserAuditLogDTO> defunctByIdList(List<Long> idList){
		ExecuteResult<UserAuditLogDTO> executeResult = new ExecuteResult<UserAuditLogDTO>();
		UserAuditLogDTO userAuditLog=new UserAuditLogDTO();
		userAuditLog.setDeletedFlag(GlobalConstant.DEFUNCT_IND_TRUE_STRING);
		userAuditLogMybatisDAO.updateSelectiveByIdList(userAuditLog,idList);
		executeResult.setResult(userAuditLog);
		return executeResult;
	}
	private void setLastUpdInfo(UserAuditLogDTO userAuditLog){
	
		Date 	curDate = DateTimeUtil.getCurrentDateByTimeZone();
		
		userAuditLog.setLastUpdDt(curDate);
	}

	@Override
	public DataGrid<UserAuditLogDTO> findListByCondition(Pager pager, UserAuditLogDTO userAuditLog) {
		return null;
	}

	

	@Override
	public DataGrid<UserAuditLogDTO> searchByConditionByPager(Pager<UserAuditLogDTO> pager, UserAuditLogDTO userAuditLog) {
		DataGrid<UserAuditLogDTO>  res=new DataGrid<UserAuditLogDTO>();
		List<UserAuditLogDTO> rows=userAuditLogMybatisDAO.selectList(userAuditLog, pager);
		long count=userAuditLogMybatisDAO.selectCount(userAuditLog);
		res.setRows(rows);
		res.setTotal(count);
		return res;
	}

	@Override
	public long searchByConditionCount(UserAuditLogDTO userAuditLog) {
		return 0;
	}
	
}
