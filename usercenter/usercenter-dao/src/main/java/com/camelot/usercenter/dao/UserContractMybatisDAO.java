package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;

/**
 * <p>用户合同数据交互接口</p>
 *  
 * @author - learrings
 * @createDate - 2015-3-6
 **/
public interface UserContractMybatisDAO  extends BaseDAO<UserContractDTO>{
	
	public List<UserContractDTO> selectByUId(@Param("uid")Long uid);
}