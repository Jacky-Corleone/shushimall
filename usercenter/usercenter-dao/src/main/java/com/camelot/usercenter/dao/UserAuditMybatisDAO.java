package com.camelot.usercenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.UserAuditDTO;

/**
 * <p>用户审核数据交互接口</p>
 *  
 * @author - learrings
 * @createDate - 2015-3-6
 **/
public interface UserAuditMybatisDAO  extends BaseDAO<UserAuditDTO>{
	public UserAuditDTO getUserAuditByUserId(@Param("uid")Long uid);
	
}