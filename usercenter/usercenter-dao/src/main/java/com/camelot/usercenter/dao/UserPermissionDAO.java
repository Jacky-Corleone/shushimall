package com.camelot.usercenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.UserPermissionDTO;

public interface UserPermissionDAO  extends BaseDAO<UserPermissionDTO>{

	void deleteByType(@Param("userId")Long userId, @Param("modularType")Long modularType);

}
