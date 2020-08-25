package com.camelot.usercenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.UserCompanyDTO;

/**
 * <p>用户公司信息（买家）数据交互接口</p>
 *  
 * @author - learrings
 * @createDate - 2015-3-7
 **/
public interface UserCompanyMybatisDAO  extends BaseDAO<UserCompanyDTO>{
	
	/**
	 * 根据用户ID查询公司信息
	 * 
	 * @param uid - 用户ID
	 * @return
	 */
	public UserCompanyDTO selectByUId(@Param("uid")Long uid);
	/**
	 * 
	 * <p>Discription:[修改 公司信息的状态]</p>
	 * Created on 2015-3-11
	 * @param dto
	 * @return
	 * @author:[liuqingshan]
	 */
	public Integer updateUserCompanyStatus(UserCompanyDTO dto);
}