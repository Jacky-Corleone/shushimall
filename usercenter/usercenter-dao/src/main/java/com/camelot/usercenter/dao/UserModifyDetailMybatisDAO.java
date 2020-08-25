package com.camelot.usercenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;


/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface UserModifyDetailMybatisDAO  extends BaseDAO<UserModifyDetailDTO>{
	
	public Integer updateTableColumnValue(UserModifyDetailDTO dto);
}